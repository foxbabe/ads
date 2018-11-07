package com.sztouyun.advertisingsystem.service.monitor;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.PartnerAdvertisementMonitorStatisticMapper;
import com.sztouyun.advertisingsystem.model.common.MonitorStatusEnum;
import com.sztouyun.advertisingsystem.model.monitor.PartnerAdvertisementMonitorStatistic;
import com.sztouyun.advertisingsystem.model.monitor.QPartnerAdvertisementMonitorStatistic;
import com.sztouyun.advertisingsystem.model.order.OrderStatusEnum;
import com.sztouyun.advertisingsystem.model.order.QOrderInfo;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.repository.monitor.PartnerAdvertisementMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.repository.order.OrderInfoRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.MonitorStatusStatistic;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerAdvertisementMonitorStatisticRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerOrderStoreMonitorItem;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerOrderStoreMonitorRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PartnerAdvertisementMonitorStatisticService extends BaseService {
    @Autowired
    private PartnerAdvertisementMonitorStatisticRepository partnerAdvertisementMonitorStatisticRepository;
    @Autowired
    private PartnerAdvertisementMonitorStatisticMapper partnerAdvertisementMonitorStatisticMapper;
    @Autowired
    private OrderInfoRepository orderInfoRepository;

    private final QPartnerAdvertisementMonitorStatistic qPartnerAdvertisementMonitorStatistic = QPartnerAdvertisementMonitorStatistic.partnerAdvertisementMonitorStatistic;
    private final QOrderInfo qOrderInfo = QOrderInfo.orderInfo;


    public Page<PartnerAdvertisementMonitorStatistic> getPartnerAdvertisementMonitors(PartnerAdvertisementMonitorStatisticRequest request) {
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        return partnerAdvertisementMonitorStatisticRepository.findAllAuthorized(q -> q.selectFrom(qPartnerAdvertisementMonitorStatistic)
                        .innerJoin(qPartnerAdvertisementMonitorStatistic.partner)
                        .innerJoin(qPartnerAdvertisementMonitorStatistic.orderInfo)
                        .innerJoin(qPartnerAdvertisementMonitorStatistic.orderInfo.orderMaterials)
                        .where(filter(request))
                        .orderBy(qPartnerAdvertisementMonitorStatistic.orderInfo.effectiveStartTime.desc()),
                pageable
        );
    }

    public List<MonitorStatusStatistic> getPartnerAdvertisementMonitorStatistic(PartnerAdvertisementMonitorStatisticRequest request) {
        request.setStatus(MonitorStatusEnum.All.getValue());
        return partnerAdvertisementMonitorStatisticRepository.findAllAuthorized(q -> q.select(
                Projections.bean(MonitorStatusStatistic.class,
                        qPartnerAdvertisementMonitorStatistic.orderInfo.orderStatus.as("status"),
                        qPartnerAdvertisementMonitorStatistic.orderInfo.orderStatus.count().as("count")
                )
                ).from(qPartnerAdvertisementMonitorStatistic)
                .innerJoin(qPartnerAdvertisementMonitorStatistic.orderInfo)
                .innerJoin(qPartnerAdvertisementMonitorStatistic.partner)
                .where(filter(request).and(qPartnerAdvertisementMonitorStatistic.orderInfo.orderStatus.in(OrderStatusEnum.Delivering.getValue(), OrderStatusEnum.TakeOff.getValue(), OrderStatusEnum.Finished.getValue())))
                .groupBy(qPartnerAdvertisementMonitorStatistic.orderInfo.orderStatus)
        );
    }

    private BooleanBuilder filter(PartnerAdvertisementMonitorStatisticRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();
        if(!StringUtils.isEmpty(request.getOrderName())) {
            predicate.and(qPartnerAdvertisementMonitorStatistic.orderInfo.name.contains(request.getOrderName()));
        }
        if(!StringUtils.isEmpty(request.getOrderCode())) {
            predicate.and(qPartnerAdvertisementMonitorStatistic.orderInfo.code.contains(request.getOrderCode()));
        }
        if (request.getStartTime() != null) {
            predicate.and(qPartnerAdvertisementMonitorStatistic.orderInfo.effectiveStartTime.goe(request.getStartTime()));
        }
        if (request.getEndTime() != null) {
            predicate.and(qPartnerAdvertisementMonitorStatistic.orderInfo.effectiveStartTime.loe(request.getEndTime()));
        }
        if (!StringUtils.isEmpty(request.getPartnerId())) {
            predicate.and(qPartnerAdvertisementMonitorStatistic.partnerId.eq(request.getPartnerId()));
        }
        if (request.getStatus() != null) {
            MonitorStatusEnum monitorStatusEnum = EnumUtils.toEnum(request.getStatus(), MonitorStatusEnum.class);
            switch (monitorStatusEnum) {
                case Finished:
                    predicate.and(qPartnerAdvertisementMonitorStatistic.orderInfo.orderStatus.in(OrderStatusEnum.TakeOff.getValue(), OrderStatusEnum.Finished.getValue()));
                    break;
                case OnWatching:
                    predicate.and(qPartnerAdvertisementMonitorStatistic.orderInfo.orderStatus.in(OrderStatusEnum.Delivering.getValue()));
                    break;
            }
        }
        return predicate;

    }

    public PartnerAdvertisementMonitorStatistic findPartnerAdvertisementMonitorStatisticById(String id){
        PartnerAdvertisementMonitorStatistic partnerAdvertisementMonitorStatistic=partnerAdvertisementMonitorStatisticRepository.findOne(id);
        if(partnerAdvertisementMonitorStatistic==null)
            throw new BusinessException("监控ID无效");
        return partnerAdvertisementMonitorStatistic;
    }


    public Page<PartnerOrderStoreMonitorItem> getPartnerOrderStoreMonitorItems(PartnerOrderStoreMonitorRequest request) {
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        return pageResult(partnerAdvertisementMonitorStatisticMapper.getPartnerOrderStoreMonitorItems(request), pageable, partnerAdvertisementMonitorStatisticMapper.getPartnerOrderStoreMonitorItemCount(request));
    }

    public List<Area> getValidAreaInPartnerStoreMonitor(String orderId) {
        if(!orderInfoRepository.existsAuthorized(qOrderInfo.id.eq(orderId)))
            throw new BusinessException("订单不存在或没有权限");
        return partnerAdvertisementMonitorStatisticMapper.getValidAreaInPartnerStoreMonitor(orderId);
    }

}
