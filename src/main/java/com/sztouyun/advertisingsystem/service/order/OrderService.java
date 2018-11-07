package com.sztouyun.advertisingsystem.service.order;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.support.QueryBase;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimePath;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.OrderMapper;
import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;
import com.sztouyun.advertisingsystem.model.order.*;
import com.sztouyun.advertisingsystem.repository.order.OrderDetailRepository;
import com.sztouyun.advertisingsystem.repository.order.OrderInfoRepository;
import com.sztouyun.advertisingsystem.repository.order.OrderMaterialRepository;
import com.sztouyun.advertisingsystem.repository.order.OrderOperationLogRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.common.CodeGenerationService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.order.*;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreDailyUsage;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreOrderRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreOrderViewModel;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService extends BaseService {
    @Value("${store.ad.position.max.count}")
    private Integer storePositionMaxCount;

    private final QOrderInfo qOrderInfo = QOrderInfo.orderInfo;
    private final QOrderMaterial qOrderMaterial = QOrderMaterial.orderMaterial;
    private final QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final QOrderDetailDate qOrderDetailDate = QOrderDetailDate.orderDetailDate;
    private final QOrderDetailStore qOrderDetailStore = QOrderDetailStore.orderDetailStore;
    private final QOrderOperationLog qOrderOperationLog = QOrderOperationLog.orderOperationLog;
    @Autowired
    private CodeGenerationService codeGenerationService;
    @Autowired
    private OrderInfoRepository orderInfoRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMaterialRepository orderMaterialRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private StoreService storeService;
    @Autowired
    private OrderOperationLogRepository orderOperationLogRepository;

    public OrderInfo createOrder(CreateOrderViewModel viewModel){
        if(orderInfoRepository.exists(qOrderInfo.partnerId.eq(viewModel.getPartnerId()).and(qOrderInfo.thirdPartId.eq(viewModel.getThirdPartId()))))
            throw new BusinessException("订单已存在");

        List<Date> dates = new ArrayList<>();
        List<String> storeIds = new ArrayList<>();
        for (var orderDetailViewModel:viewModel.getOrderDetails()){
            dates.addAll(orderDetailViewModel.getDates());
            storeIds.addAll(orderDetailViewModel.getStoreIds());
        }
        if(dates.isEmpty())
            throw new BusinessException("最少投放一天");
        if(storeIds.isEmpty())
            throw new BusinessException("最少投放一个设备");

        dates = dates.stream().distinct().collect(Collectors.toList());
        storeIds = storeIds.stream().distinct().collect(Collectors.toList());
        List<String> availableStoreIds = storeService.getAvailableStoreIds(storeIds);
        storeIds.removeAll(availableStoreIds);
        if(!storeIds.isEmpty())
            throw new BusinessException("以下设备不可用："+String.join(",",storeIds));

        List<Date> finalDates = dates;
        var dailyMap = getUnavailableStoreDailyUsage(viewModel.getOrderMaterial().getAdvertisementPositionCategory(), date->date.in(finalDates),availableStoreIds);
        return createOrder(viewModel,finalDates,availableStoreIds,dailyMap);
    }

    /**
     * 查询放到外面，将保存逻辑放到一个事务里面，防止并发情况下死锁
     * @param viewModel
     * @param dailyMap
     * @return
     */
    @Transactional
    public OrderInfo createOrder(CreateOrderViewModel viewModel,List<Date> dates,List<String> storeIds,Map<Long,List<String>> dailyMap){
        OrderInfo orderInfo = new OrderInfo();
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<OrderDetailDate> orderDetailDates = new ArrayList<>();
        List<OrderDetailStore> orderDetailStores = new ArrayList<>();
        for (OrderDetailViewModel orderDetailViewModel:viewModel.getOrderDetails()){
            OrderDetail orderDetail = new OrderDetail(orderInfo.getId());
            orderDetailViewModel.getDates().stream().distinct().forEach(date -> {
                List<String> unavailableStoreIds = dailyMap.get(date.getTime());
                if(unavailableStoreIds !=null && !unavailableStoreIds.isEmpty()){
                    unavailableStoreIds.forEach(unavailableStoreId ->{
                        if(orderDetailViewModel.getStoreIds().contains(unavailableStoreId))
                            throw new BusinessException(unavailableStoreId+"在"+ DateUtils.getDateFormat(date)+"库存不足");
                    } );
                }
                orderDetailDates.add(new OrderDetailDate(orderDetail.getId(),date));
            });
            List<String>  orderDetailStoreIds =orderDetailViewModel.getStoreIds().stream().distinct().collect(Collectors.toList());
            orderDetailStoreIds.forEach(storeId ->{
                orderDetailStores.add(new OrderDetailStore(orderDetail.getId(),storeId));
            });
            orderDetail.setStoreCount(orderDetailStoreIds.size());
            orderDetails.add(orderDetail);
        }

        orderInfo.setStartTime(Linq4j.asEnumerable(dates).min());
        orderInfo.setEndTime(Linq4j.asEnumerable(dates).max());
        orderInfo.setOrderAmount(0);
        orderInfo.setName("");
        orderInfo.setTotalDays(dates.size());
        orderInfo.setTotalStoreCount(storeIds.size());
        orderInfo.setCode(codeGenerationService.generateCode(CodeTypeEnum.ORDER));
        orderInfo.setPartnerId(viewModel.getPartnerId());
        orderInfo.setThirdPartId(viewModel.getThirdPartId());
        orderInfo.setOrderStatus(OrderStatusEnum.PendingPublishing.getValue());
        orderInfoRepository.save(orderInfo);

        OrderMaterial orderMaterial = ApiBeanUtils.copyProperties(viewModel.getOrderMaterial(),OrderMaterial.class);
        orderMaterial.setOrderId(orderInfo.getId());
        orderMaterial.setAdvertisementPositionCategory(viewModel.getOrderMaterial().getAdvertisementPositionCategory());
        orderMaterialRepository.save(orderMaterial);

        orderMapper.saveOrderDetails(orderDetails);
        orderMapper.saveOrderDetailDates(orderDetailDates);
        orderMapper.saveOrderDetailStores(orderDetailStores);
        orderOperationLogRepository.save(new OrderOperationLog(orderInfo.getId(),OrderOperationEnum.Create.getValue()));
        return orderInfo;
    }

    /**
     * 获取哪些门店的哪些天的广告位满了
     * @return
     */
    public Map<Long,List<String>> getUnavailableStoreDailyUsage(Integer positionCategory,Function<DateTimePath<Date>,Predicate> dateFilter, List<String> storeIds){
        List<StoreDailyUsage> storeDailyUsages=  orderDetailRepository.findAll(q-> {
            var query = q.select(Projections.bean(StoreDailyUsage.class,qOrderDetailDate.date.as("date"),qOrderDetailStore.storeId.as("storeId")))
                    .from(qOrderDetail)
                    .innerJoin(qOrderDetail.orderInfo, qOrderInfo)
                    .on(qOrderInfo.orderStatus.in(OrderStatusEnum.PendingPublishing.getValue(),OrderStatusEnum.PublishingAuditing.getValue(),OrderStatusEnum.PendingDelivery.getValue(),OrderStatusEnum.Delivering.getValue()))
                    .innerJoin(qOrderInfo.orderMaterials, qOrderMaterial)
                    .on(qOrderMaterial.advertisementPositionCategory.eq(positionCategory))
                    .innerJoin(qOrderDetail.orderDetailDates, qOrderDetailDate)
                    ;
            if(dateFilter !=null){
                query = query.on(dateFilter.apply(qOrderDetailDate.date));
            }
            query = query.innerJoin(qOrderDetail.orderDetailStores, qOrderDetailStore);
            if(storeIds != null && !storeIds.isEmpty()){
                query = query.on(qOrderDetailStore.storeId.in(storeIds));
            }

            return query.groupBy(qOrderDetailDate.date, qOrderDetailStore.storeId)
                    .having(qOrderDetail.id.count().goe(storePositionMaxCount));
            }
        );

        Map<Long,List<String>> dailyMap = new HashMap<>();
        storeDailyUsages.forEach(storeDailyUsage ->{
            Long time =storeDailyUsage.getDate().getTime();
            if(dailyMap.containsKey(time)){
                List<String> ids= dailyMap.get(time);
                List<String> newIds = new ArrayList<>(ids);
                newIds.add(storeDailyUsage.getStoreId());
                dailyMap.put(time,newIds);
            }else {
                dailyMap.put(storeDailyUsage.getDate().getTime(), Collections.singletonList(storeDailyUsage.getStoreId()));
            }
        });
        return dailyMap;
    }

    public OrderInfo getOrder(String partnerId,String thirdPartId){
        if(StringUtils.isEmpty(partnerId) || StringUtils.isEmpty(thirdPartId))
            throw new BusinessException("订单不存在");

        OrderInfo orderInfo = orderInfoRepository.findOne(qOrderInfo.partnerId.eq(partnerId).and(qOrderInfo.thirdPartId.eq(thirdPartId)));
        if(orderInfo == null)
            throw new BusinessException("订单不存在");
        return orderInfo;
    }

    public OrderInfo getOrder(String orderId) {
        if(StringUtils.isEmpty(orderId))
            throw new BusinessException("订单不存在");
        OrderInfo orderInfo = orderInfoRepository.findOneAuthorized(qOrderInfo.id.eq(orderId));
        if(orderInfo == null)
            throw new BusinessException("订单不存在或者权限不足");
        return orderInfo;
    }

    public Page<OrderInfo> queryOrderList(OrderBasePageInfoViewModel viewModel) {

        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize());
        return orderInfoRepository.findAllAuthorized(q-> {
            var query = q.select(qOrderInfo)
                    .from(qOrderInfo)
                    .innerJoin(qOrderInfo.orderMaterials, qOrderMaterial);
            buildFilter(query,viewModel);
            if(viewModel.getOrderStatus()!=null){
                query=query.on(qOrderInfo.orderStatus.eq(viewModel.getOrderStatus()));
            }
            query.orderBy(qOrderInfo.createdTime.desc());
            return query;
        },pageable);
    }


    public List<OrderStatisticsViewModel> getOrderStatistics(OrderBasePageInfoViewModel viewModel) {
        return  orderInfoRepository.findAllAuthorized(q-> {
                    var query = q.select(Projections.bean(OrderStatisticsViewModel.class,qOrderInfo.orderStatus.as("status"),qOrderInfo.id.countDistinct().as("count")))
                            .from(qOrderInfo)
                            .innerJoin(qOrderInfo.orderMaterials, qOrderMaterial);
                    buildFilter(query,viewModel);
                    return query.groupBy(qOrderInfo.orderStatus);
                }
        );
    }


    private void buildFilter(QueryBase query, OrderBasePageInfoViewModel viewModel){
        if(!StringUtils.isEmpty(viewModel.getCode())){
            query=query.where(qOrderInfo.code.contains(viewModel.getCode()));
        }
        if(!StringUtils.isEmpty(viewModel.getName())){
            query=query.where(qOrderInfo.name.contains(viewModel.getName()));
        }
        if(!StringUtils.isEmpty(viewModel.getPartnerId())){
            query=query.where(qOrderInfo.partnerId.eq(viewModel.getPartnerId()));
        }
        if(viewModel.getAdvertisementPositionType()!=null){
            query=query.where(qOrderMaterial.advertisementPositionType.eq(viewModel.getAdvertisementPositionType()));
        }
        if(viewModel.getStartTime()!=null){
            query=query.where(qOrderInfo.createdTime.goe(viewModel.getStartTime()));
        }
        if(viewModel.getEndTime()!=null){
            query=query.where(qOrderInfo.createdTime.lt(new LocalDate(viewModel.getEndTime()).plusDays(1).toDate()));
        }
    }


    public Page<StoreOrderViewModel> getOrderStoreList(Pageable pageable, StoreOrderRequest request) {
        if(!orderInfoRepository.existsAuthorized(qOrderInfo.id.eq(request.getOrderId())))
            throw new BusinessException("订单ID无效或权限不足");
        return pageResult(orderMapper.getOrderStoreList(request), pageable,orderMapper.getOrderStoreListCount(request) );
    }

	 public Page<DailyStoreCountItem> getDailyStoreCount(DailyDeliveryStoreStatisticRequest request){
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
         return pageResult(orderMapper.getDailyStoreCountStatistic(request),pageable,getOrder(request.getId()).getTotalDays());
    }

    public List<OrderMaterial> getOrderDetails(String id) {
        BooleanBuilder predicate =new BooleanBuilder();
        predicate.and(qOrderMaterial.orderId.eq(id));
        return  orderMaterialRepository.findAll(predicate, new JoinDescriptor().innerJoin(qOrderMaterial.orderInfo).leftJoin(qOrderMaterial.partnerMaterial));
    }

    public Page<OrderOperationLog> getOrderOperationLog(OrderOperationLogsRequest request){
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
	     if(!orderInfoRepository.exists(qOrderInfo.id.eq(request.getId())))
	         throw new BusinessException("订单ID无效");
       return orderOperationLogRepository.findAll(q->q.select(qOrderOperationLog).from(qOrderOperationLog).where(orderOperationFilter(request)).orderBy(qOrderOperationLog.createdTime.desc()),pageable);
    }

    private BooleanBuilder orderOperationFilter(OrderOperationLogsRequest request){
        BooleanBuilder predicate =new BooleanBuilder();
        predicate.and(qOrderOperationLog.orderId.eq(request.getId()));
        if(request.getStartTime()!=null){
            predicate.and(qOrderOperationLog.createdTime.goe(request.getStartTime()));
        }
        if(request.getEndTime()!=null){
            predicate.and(qOrderOperationLog.createdTime.lt(new LocalDate(request.getEndTime()).plusDays(1).toDate()));
        }
        OrderOperationStatusEnum operationStatusEnum= EnumUtils.toEnum(request.getOperationStatus(),OrderOperationStatusEnum.class);
        if(!operationStatusEnum.equals(OrderOperationStatusEnum.All)){
            predicate.and(qOrderOperationLog.operation.eq(operationStatusEnum.getOperation()).and(qOrderOperationLog.successed.eq(operationStatusEnum.getSuccessed())));
        }
        return  predicate;
    }
}
