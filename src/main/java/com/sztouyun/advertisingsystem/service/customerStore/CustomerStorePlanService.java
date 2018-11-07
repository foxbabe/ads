package com.sztouyun.advertisingsystem.service.customerStore;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.CustomerStorePlanMapper;
import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;
import com.sztouyun.advertisingsystem.model.customerStore.CustomerStorePlan;
import com.sztouyun.advertisingsystem.model.customerStore.CustomerStorePlanDetail;
import com.sztouyun.advertisingsystem.model.customerStore.QCustomerStorePlan;
import com.sztouyun.advertisingsystem.model.customerStore.QCustomerStorePlanDetail;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.repository.customerStore.CustomerStorePlanDetailRepository;
import com.sztouyun.advertisingsystem.repository.customerStore.CustomerStorePlanRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.common.CodeGenerationService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import com.sztouyun.advertisingsystem.utils.excel.ExcelConvertConfig;
import com.sztouyun.advertisingsystem.utils.excel.ExcelConversion;
import com.sztouyun.advertisingsystem.utils.excel.LoadInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.*;
import com.sztouyun.advertisingsystem.viewmodel.store.CustomerStoreInfoAreaSelectedRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.OneKeyInsertCustomerStoreRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoStatisticViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by szty on 2018/5/15.
 */
@Service
public class CustomerStorePlanService extends BaseService {
    @Autowired
    private CustomerStorePlanMapper customerStorePlanMapper;
    @Autowired
    private CustomerStorePlanDetailRepository customerStorePlanDetailRepository;
    @Autowired
    private CustomerStorePlanRepository customerStorePlanRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private StoreInfoRepository storeInfoRepository;

    private final QCustomerStorePlanDetail qCustomerStorePlanDetail=QCustomerStorePlanDetail.customerStorePlanDetail;

    public List<String> getCustomerCheckedStoreAreaIds(CustomerStoreInfoAreaSelectedRequest request) {
        return customerStorePlanMapper.getCustomerCheckedStoreAreaIds(request);
    }

    private final QCustomerStorePlan qCustomerStorePlan = QCustomerStorePlan.customerStorePlan;
    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    @Transactional
    public void deleteCustomerStorePlan(String id) {
        if (!customerStorePlanRepository.existsAuthorized(qCustomerStorePlan.id.eq(id)))
            throw new BusinessException("该选点记录不存在");
        customerStorePlanRepository.delete(id);
        customerStorePlanDetailRepository.deleteCustomerStorePlanDetailByCustomerStorePlanId(id);
    }

    public Page<CustomerStorePlan> getCustomerStorePlanList(CustomerStorePlanPageInfoViewModel viewModel) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (!StringUtils.isEmpty(viewModel.getCustomerId())) {
            predicate.and(qCustomerStorePlan.customerId.eq(viewModel.getCustomerId()));
        }
        if (!StringUtils.isEmpty(viewModel.getCustomerName())) {
            predicate.and(qCustomerStorePlan.customer.name.contains(viewModel.getCustomerName()));
        }
        if (!StringUtils.isEmpty(viewModel.getCode())) {
            predicate.and(qCustomerStorePlan.code.contains(viewModel.getCode()));
        }
        if (!StringUtils.isEmpty(viewModel.getOwnerName())) {
            predicate.and(qCustomerStorePlan.customer.owner.nickname.contains(viewModel.getOwnerName()));
        }
        return customerStorePlanRepository.findAllAuthorized(predicate, new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize()), new JoinDescriptor().innerJoin(qCustomerStorePlan.customer));
    }

    public Integer getSelectedStoreCount(String customerStorePlanId){
        if(StringUtils.isEmpty(customerStorePlanId))
            return 0;
        return (int)(customerStorePlanDetailRepository.count(qCustomerStorePlanDetail.customerStorePlanId.eq(customerStorePlanId).and(qCustomerStorePlanDetail.storeInfo.deleted.isFalse())));
    }

    @Autowired
    private CodeGenerationService codeGenerationService;


    @Transactional
    public void save(SaveCustomerStorePlanInfo info) {
        String code = "";
        if (!StringUtils.isEmpty(info.getOldCustomerStorePlanId())) {
            //编辑
            CustomerStorePlan customerStorePlan = customerStorePlanRepository.findOne(qCustomerStorePlan.id.eq(info.getOldCustomerStorePlanId()));
            if(customerStorePlan == null) {
                throw new BusinessException("选点记录不存在");
            }
            code = customerStorePlan.getCode();
            customerStorePlanMapper.deleteByCustomerStorePlanId(info.getOldCustomerStorePlanId());
            customerStorePlanMapper.updateCustomerStorePlanId(info);
            info.setTempCustomerStorePlanId(info.getOldCustomerStorePlanId());
        }
        create(info.getCustomerId(), info.getTempCustomerStorePlanId(), code);
    }

    public void create(String customerId, String customerStorePlanId, String code) {
        customerStorePlanMapper.deleteInValidCustomerStorePlanDetail(customerStorePlanId);
        Long storeCount = customerStorePlanDetailRepository.count(qCustomerStorePlanDetail.customerStorePlanId.eq(customerStorePlanId));
        if(storeCount.equals(0)) {
            throw new BusinessException("请选择投放门店");
        }
        if(!customerRepository.exists(customerId)) {
            throw new BusinessException("客户信息不存在 ");
        }
        CustomerStorePlan customerStorePlan=new CustomerStorePlan();
        customerStorePlan.setId(customerStorePlanId);
        if(StringUtils.isEmpty(code)) {
            customerStorePlan.setCode(codeGenerationService.generateCode(CodeTypeEnum.CSP));
        } else {
            customerStorePlan.setCode(code);
        }
        customerStorePlan.setCustomerId(customerId);
        Long cityCount = customerStorePlanMapper.getCityCount(customerStorePlanId);
        customerStorePlan.setCityCount(cityCount.intValue());
        customerStorePlan.setStoreCount(storeCount.intValue());
        customerStorePlanRepository.save(customerStorePlan);
    }

	public void oneKeySelectStore(OneKeyInsertCustomerStoreRequest request){
        customerStorePlanMapper.batchChooseCustomerStoreInfo(request);
    }

    @Transactional
    public void emptyCustomerStorePlan(String customerStorePlanId) {
        customerStorePlanMapper.emptyCustomerStorePlan(customerStorePlanId);
    }

    @Transactional
    public void cancel(String tempCustomerStorePlanId) {
        customerStorePlanMapper.cancelTempCustomerStoreInfo(tempCustomerStorePlanId);
    }

    @Transactional
    public void toggledSelectedCustomerStore(String customerStorePlanId, String storeId) {
        if(!storeInfoRepository.exists(qStoreInfo.id.eq(storeId))) {
            throw new BusinessException("门店信息不存在, 门店ID为: " + storeId);
        }
        DeleteCustomerStorePlanInfo info = new DeleteCustomerStorePlanInfo();
        info.setCustomerStorePlanId(customerStorePlanId);
        info.setStoreId(storeId);
        boolean existsDetail = customerStorePlanDetailRepository.existsAuthorized(qCustomerStorePlanDetail.customerStorePlanId.eq(customerStorePlanId).and(qCustomerStorePlanDetail.storeId.eq(storeId)));
        if (existsDetail) {
            customerStorePlanMapper.deleteCustomerStorePlanDetail(info);
        } else {
            CustomerStorePlanDetail customerStorePlanDetail = ApiBeanUtils.copyProperties(info, CustomerStorePlanDetail.class);
            customerStorePlanDetailRepository.save(customerStorePlanDetail);
        }
    }

    public CustomerStorePlan findCustomerStorePlanById(String id){
	    return customerStorePlanRepository.findOneAuthorized(qCustomerStorePlan.id.eq(id));
    }


    public void copyTempCustomerStorePlan(String tempCustomerStorePlanId, String oldCustomerStorePlan) {
        customerStorePlanMapper.copyTempCustomerStorePlanDetail(new CopyCustomerStorePlanInfo(tempCustomerStorePlanId, oldCustomerStorePlan));
    }

    public Page<StoreInfoStatisticViewModel> getStoreInfoToPage(String id) {
        ExportStoreInfoQueryRequest request = new ExportStoreInfoQueryRequest();
        request.setId(id);
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        PageRequest pageable = new PageRequest(request.getPageIndex(), request.getPageSize());
        return pageResult(customerStorePlanMapper.getStoreInfoByCustomerStorePlanId(request), pageable, customerStorePlanMapper.getStoreInfoCountByCustomerStorePlanId(id));
    }

    public ExcelImportViewModel importCustomerStoreFromExcel(MultipartFile file,String tempCustomerStorePlanId){
        String newTempCustomerStorePlanId= UUIDUtils.generateOrderedUUID();
        ExcelImportViewModel excelImportViewModel=new ExcelImportViewModel(newTempCustomerStorePlanId);
        ExcelConvertConfig excelConvertConfig =new ExcelConvertConfig(
                newTempCustomerStorePlanId,
                tempCustomerStorePlanId,
                new Integer[]{1,2,4,5,6,7},
                new String[]{"编号","门店ID","门店名称","省份","城市","地区","具体地址","设备ID"}
                );
        Integer rowCount= ExcelConversion.transfer(file, excelConvertConfig);
        if(rowCount==0)
            throw  new BusinessException("至少导入一家门店");
        excelImportViewModel.setTotal(rowCount);
        loadsCustomerStore(excelConvertConfig);
        customerStorePlanMapper.batchInsertCustomerStoreDetail(newTempCustomerStorePlanId);
        Long insertCount=customerStorePlanDetailRepository.count(qCustomerStorePlanDetail.customerStorePlanId.eq(newTempCustomerStorePlanId));
        excelImportViewModel.setUnmatched(rowCount-insertCount.intValue());
        new Thread(()->{
            FileUtils.clearFile(excelConvertConfig.getSavePath());
            customerStorePlanMapper.deleteByCustomerStorePlanId(tempCustomerStorePlanId);
        }).start();
        return excelImportViewModel;
    }

    private void loadsCustomerStore(ExcelConvertConfig config){
        customerStorePlanMapper.loadCustomerStore(new LoadInfo(config.getSavePath(),config.getOid(),config.getBid()));
    }

    public void cleanTempData(String tempId){
        customerStorePlanMapper.cleanTempData(tempId);
    }

    public List<InvalidCustomerStorePlanDetail> getInvalidCustomerStoreImportInfo(InvalidCustomerStorePlanDetailRequest request){
        return customerStorePlanMapper.getInvalidCustomerStoreImportInfo(request);
    }

    public Long getInvalidCustomerStoreImportCount(InvalidCustomerStorePlanDetailRequest request){
        return customerStorePlanMapper.getInvalidCustomerStoreImportCount(request);
    }

}
