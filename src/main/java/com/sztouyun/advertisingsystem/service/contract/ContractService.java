package com.sztouyun.advertisingsystem.service.contract;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.ContractMapper;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.*;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.customerStore.CustomerStorePlan;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.system.*;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementDisplayTimesConfigRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementDurationConfigRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementSizeConfigRepository;
import com.sztouyun.advertisingsystem.repository.contract.*;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.repository.customerStore.CustomerStorePlanRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.repository.system.SystemParamConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.contract.*;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.CustomerStorePlanChooseRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.ContractStoreCheckTreeViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoAreaSelectedViewModel;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ContractService extends BaseService {

    private final QCustomer qCustomer = QCustomer.customer;

    private final QContract qContract = QContract.contract;

    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    private final QContractStore qContractStore = QContractStore.contractStore;

    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;

    private final QAdvertisementSizeConfig qAdvertisementSizeConfig = QAdvertisementSizeConfig.advertisementSizeConfig;

    private static final QAdvertisementDisplayTimesConfig qAdvertisementDisplayTimesConfig = QAdvertisementDisplayTimesConfig.advertisementDisplayTimesConfig;

    private static final QAdvertisementDurationConfig qAdvertisementDurationConfig = QAdvertisementDurationConfig.advertisementDurationConfig;

    private static final QSystemParamConfig qSystemParamConfig = QSystemParamConfig.systemParamConfig;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContractStoreRepository contractStoreRepository;

    @Autowired
    private StoreInfoRepository storeInfoRepository;

    @Autowired
    private ContractExtensionRepository contractExtensionRepository;

    @Autowired
    private AdvertisementSizeConfigRepository advertisementSizeConfigRepository;

    @Autowired
    private AdvertisementDisplayTimesConfigRepository advertisementDisplayTimesConfigRepository;

    @Autowired
    private AdvertisementDurationConfigRepository advertisementDurationConfigRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private ContractAdvertisementPositionConfigRepository contractAdvertisementPositionConfigRepository;
    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private ContractOperationLogRepository contractOperationLogRepository;
    @Autowired
    private SystemParamConfigRepository systemParamConfigRepository;
    @Autowired
    private CustomerStorePlanRepository customerStorePlanRepository;

    @Transactional
    public String createContract(Contract contract) {
        if (contract == null)
            throw new BusinessException("合同数据缺失");

        Boolean hasRepeatContractCode = contractRepository.exists(qContract.contractCode.eq(contract.getContractCode()).and(qContract.id.ne(contract.getId())));
        if (hasRepeatContractCode)
            throw new BusinessException("该合同编号已经存在");

        boolean hasRepeatContractName = contractRepository.exists(qContract.contractName.eq(contract.getContractName().trim()));
        if (hasRepeatContractName)
            throw new BusinessException("合同的名称已经存在");

        if (!customerRepository.exists(contract.getCustomerId()))
            throw new BusinessException("签约的客户不存在");

        validatedContractAdvertisementPositionConfigEnable(contract.getContractExtension().getContractAdvertisementPositionConfigViewModels());

        contract.setCreatorId(getUser().getId());//设置负责人
        contract.setOwnerId(getUser().getId());//设置负责人

        contract.setTotalCost(contract.getContractExtension().getTotalCost());
        contract.setContractStatus(ContractStatusEnum.PendingCommit.getValue());

        saveContractAdvertisementPositionConfig(contract.getId(), contract.getContractExtension().getContractAdvertisementPositionConfigViewModels());
        resetContractStoreInfo(contract);
        contractRepository.save(contract);
        return contract.getId();
    }

    public Contract getContract(String id) {
        Contract contract = contractRepository.findOneAuthorized(qContract.id.eq(id), new JoinDescriptor().leftJoin(qContract.contractExtension));
        if (null == contract)
            throw new BusinessException("合同不存在或者权限不足！");
        return contract;
    }

    /**
     * 根据合同id获取所有门店记录
     *
     * @param contactId
     * @return
     */
    public List<ContractStore> getContractStores(String contactId) {
        return contractStoreRepository.findAll(f -> f.selectFrom(qContractStore).where(qContractStore.contractId.eq(contactId)
                .and(qContractStore.storeInfo.deleted.eq(false))));
    }

    @Transactional
    public void updateContract(Contract contract) {

        Integer contractStatus = contract.getContractStatus();
        if (!contractStatus.equals(ContractStatusEnum.PendingCommit.getValue()) && !contractStatus.equals(ContractStatusEnum.PendingAuditing.getValue()))
            throw new BusinessException("合同状态无效, 无法编辑");

        if (!customerRepository.exists(contract.getCustomerId()))
            throw new BusinessException("签约的客户不存在");

        boolean hasRepeatContractName = contractRepository.exists(qContract.contractName.eq(contract.getContractName().trim()).and(qContract.id.ne(contract.getId())));
        if (hasRepeatContractName)
            throw new BusinessException("合同名称已经存在");

        validatedContractAdvertisementPositionConfigEnable(contract.getContractExtension().getContractAdvertisementPositionConfigViewModels());

        var user = getUser();
        if (contractStatus.equals(ContractStatusEnum.PendingAuditing.getValue())) {
            if (user.getRoleTypeEnum().getValue().equals(RoleTypeEnum.SaleMan.getValue()))
                throw new BusinessException("业务员没有权限编辑待审核状态的合同");
            ContractOperationLog log = new ContractOperationLog(contract.getId(), ContractOperationEnum.Edit.getValue(), true, "");
            contractOperationLogRepository.save(log);
        }

        contract.setContractStatus(ContractStatusEnum.PendingCommit.getValue());//保存之后设置为待提交状态
        contract.getContractExtension().setSaveTime(new Date());
        contract.setTotalCost(contract.getContractExtension().getTotalCost());

        //更新合同投放位置配置
        contractAdvertisementPositionConfigRepository.deletePositionConfigByContractId(contract.getId());
        saveContractAdvertisementPositionConfig(contract.getId(), contract.getContractExtension().getContractAdvertisementPositionConfigViewModels());

        resetContractStoreInfo(contract);
        contractRepository.save(contract);
    }

    @Transactional
    public InvokeResult deleteContract(String id) {
        Contract contract = contractRepository.findOne(id);
        if (contract == null)
            throw new BusinessException("查询不到合同相关信息");

        if (!ContractStatusEnum.PendingCommit.getValue().equals(contract.getContractStatus()))
            throw new BusinessException("合同状态无效, 无法删除");

        contractStoreRepository.deleteByContractId(id);
        contractExtensionRepository.delete(id);
        contractRepository.delete(id);
        return InvokeResult.SuccessResult();
    }

    public Page<Contract> getContractList(ContractQueryRequest queryRequest) {
        Pageable pageable = new MyPageRequest(queryRequest.getPageIndex(), queryRequest.getPageSize());
        BooleanBuilder predicate = filterQuery(queryRequest);
        if (queryRequest.getContractStatus() > 0) {
            predicate.and(qContract.contractStatus.eq(queryRequest.getContractStatus()));
        }
        if (queryRequest.getContractStatus() == 0 && !StringUtils.isEmpty(queryRequest.getChoosedStautuses())) {
            predicate.and(qContract.contractStatus.in(com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(queryRequest.getChoosedStautuses(), Constant.SEPARATOR)));
        }
        if (queryRequest.isAuditing() != null) {
            predicate.and(qContract.auditing.eq(queryRequest.isAuditing()));
        }
        if (queryRequest.getCustomerIds() != null) {
            predicate.and(qContract.customerId.in(queryRequest.getCustomerIds()));
        }
        return contractRepository.findAllAuthorized(predicate, pageable, queryRequest.getJoinDescriptor());
    }

    /**
     * 合同状态信息统计（合同总数、待审核、待签约、待执行）
     */
    public ContractStatusViewModel getContractStatusStatistics(ContractQueryRequest queryRequest) {
        List<ContractStatusViewModel> contractStatusList = contractRepository.findAllAuthorized(queryFactory -> queryFactory
                .select(Projections.bean(ContractStatusViewModel.class, qContract.contractStatus.as("contractStatus"), qContract.count().as("statusCount")))
                .from(qContract)
                .where(filterQuery(queryRequest))
                .groupBy(qContract.contractStatus)
        );

        long total = 0;
        ContractStatusViewModel viewModelResult = new ContractStatusViewModel();
        for (ContractStatusViewModel contractStatusViewModel : contractStatusList) {
            if (ContractStatusEnum.PendingAuditing.getValue().equals(contractStatusViewModel.getContractStatus())) {
                viewModelResult.setToAudit(contractStatusViewModel.getStatusCount());
            }
            if (ContractStatusEnum.PendingSign.getValue().equals(contractStatusViewModel.getContractStatus())) {
                viewModelResult.setToSign(contractStatusViewModel.getStatusCount());
            }
            if (ContractStatusEnum.PendingExecution.getValue().equals(contractStatusViewModel.getContractStatus())) {
                viewModelResult.setToExecute(contractStatusViewModel.getStatusCount());
            }
            total += contractStatusViewModel.getStatusCount();
        }
        viewModelResult.setTotal(total);
        return viewModelResult;
    }

    /**
     * 过滤查询条件, 根据合同名称、合同编号、合同状态进行查询
     * @param queryRequest
     * @return
     */
    private BooleanBuilder filterQuery(ContractQueryRequest queryRequest) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (!StringUtils.isEmpty(queryRequest.getContractName())) {
            predicate.and(qContract.contractName.contains(queryRequest.getContractName()));
        }
        if (!StringUtils.isEmpty(queryRequest.getContractCode())) {
            predicate.and(qContract.contractCode.contains(queryRequest.getContractCode()));
        }
        return predicate;
    }

    @Transactional
    public void addContractStore(ContractStore contractStore) {
        QContractStore qContractStore = QContractStore.contractStore;
        Boolean isExists = contractStoreRepository.exists(
                qContractStore.contractId.eq(contractStore.getContractId())
                        .and(qContractStore.storeId.eq(contractStore.getStoreId()))
        );
        if (isExists)
            throw new BusinessException("该记录已经存在");

        boolean isExistsStoreInfo = storeInfoRepository.exists(qStoreInfo.id.eq(contractStore.getStoreId()).and(qStoreInfo.deleted.eq(false)));
        if (!isExistsStoreInfo)
            throw new BusinessException("门店数据不存在");

        if (!storeInfoMapper.hasAvailableAdPosition(contractStore.getStoreId()))
            throw new BusinessException("门店可用广告位不足");

        ContractStore insertStore = contractStoreRepository.save(contractStore);
        if (insertStore == null)
            throw new BusinessException("插入门店数据失败");

        //门店占用量加1
        storeInfoMapper.increaseStoreUsedCount(contractStore.getStoreId());
    }

    public Long getTotalStoresByType(ContractStore contractStore) {
        return contractStoreRepository.count(qContractStore.contractId.eq(contractStore.getContractId()).and(qContractStore.storeType.eq(contractStore.getStoreType())));
    }

    public Long getTotalStores(String contractId) {
        return contractStoreRepository.count(qContractStore.contractId.eq(contractId));
    }

    @Transactional
    public void deleteContractStore(ContractStore contractStore) {
        contractStoreRepository.deleteByContractIdAndStoreId(contractStore.getContractId(), contractStore.getStoreId());
        //门店占用量减少1
        storeInfoMapper.decreaseStoreUsedCount(contractStore.getStoreId());
    }

    @Transactional
    public void deleteAllContractStores(String contractId, int storeType) {
        if (!contractRepository.exists(contractId))
            throw new BusinessException("查询不到合同数据");

        Boolean isExists = contractStoreRepository.exists(qContractStore.storeType.eq(storeType).and(qContractStore.contractId.eq(contractId)));
        if (!isExists) {
            throw new BusinessException("该数据已经被清空");
        }
        //合同选择的所有门店占用量减少1
        storeInfoMapper.decreaseContractStoreUsedCount(contractId);
        contractMapper.deleteByContractIdAndStoreType(new ContractSelectedStoreInfo(contractId,storeType));
    }

    public Page<Contract> getContractListByCustomerId(String customerId, Pageable pageable) {
        if (!customerRepository.existsAuthorized(qCustomer.id.eq(customerId)))
            throw new BusinessException("客户不存在或权限不足！");
        Page<Contract> pages = contractRepository.findAll(qContract.customerId.eq(customerId), pageable, new JoinDescriptor().innerJoin(qContract.contractExtension));
        return pages;
    }

    public Boolean hasUnDeliveryAdvertisement(String contractId) {
        return advertisementRepository.exists(
                qAdvertisement.contractId.eq(contractId)
                        .and(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.PendingCommit.getValue())
                                .or(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.PendingAuditing.getValue()))
                                .or(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.PendingDelivery.getValue()))
                                .or(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue()))
                        ));
    }

    public ContractAuditingStatistic contractAuditingStatistic() {
        NumberExpression<Integer> pendingAuditingCountExpression = qContract.contractStatus.when(AdvertisementStatusEnum.PendingAuditing.getValue()).then(1).otherwise(0);
        NumberExpression<Integer> pendingTerminationAuditingCountExpression = qContract.auditing.when(true).then(1).otherwise(0);
        return contractRepository.findOneAuthorized(q ->
                q.select(Projections.bean(ContractAuditingStatistic.class,
                        pendingAuditingCountExpression.sum().as("pendingAuditingCount"),
                        pendingTerminationAuditingCountExpression.sum().as("pendingTerminationAuditingCount")))
                        .from(qContract));
    }

    public List<ContractStoreCheckTreeViewModel> getContractStoreAreaIds(StoreInfoAreaSelectedViewModel queryRequest) {
        return storeInfoMapper.getContractStoreAreaId(queryRequest);
    }

    public Boolean existsTestContractStore(StoreInfoAreaSelectedViewModel queryRequest) {
        return storeInfoMapper.existsTestContractStore(queryRequest);
    }


    public Integer getContractTotalCityCount(String contractId) {
        Long count = contractStoreRepository.count(q -> q.select(qContractStore).from(qContractStore).where(qContractStore.contractId.eq(contractId).and(qContractStore.storeInfo.deleted.eq(false))).groupBy(qContractStore.storeInfo.cityId));
        return null == count ? 0 : count.intValue();
    }

    public String getCityNames(String contractId) {
        List<Area> cityList = contractMapper.getCitiesByContactId(contractId);
        if (cityList == null || cityList.isEmpty())
            return "";
        StringBuffer cities = new StringBuffer();
        cityList.forEach(area -> {
            cities.append(area.getName());
            cities.append("、");
        });
        return cities.substring(0, cities.length() - 1).toString();
    }


    private void saveContractAdvertisementPositionConfig(String contractId, List<ContractAdvertisementPositionConfigViewModel> contractAdvertisementPositionConfigViewModels) {

        if (CollectionUtils.isEmpty(contractAdvertisementPositionConfigViewModels))
            throw new BusinessException("投放配置不能为空");

        Set<String> displayTimesIds = new HashSet<>();
        Set<String> durationConfigIds = new HashSet<>();
        Set<Integer> positionTypeIds = new HashSet<>();
        Set<Integer> terminalTypeIds = new HashSet<>();

        contractAdvertisementPositionConfigViewModels.forEach(viewModel -> {
            if (!StringUtils.isEmpty(viewModel.getAdvertisementDisplayTimesConfigId())) {
                displayTimesIds.add(viewModel.getAdvertisementDisplayTimesConfigId());
            }
            durationConfigIds.add(viewModel.getAdvertisementDurationConfigId());
            positionTypeIds.add(viewModel.getAdvertisementPositionType());
            terminalTypeIds.add(viewModel.getTerminalType());
        });

        Iterable<AdvertisementDisplayTimesConfig> advertisementDisplayTimesConfigByIds = advertisementDisplayTimesConfigRepository.findAll(qAdvertisementDisplayTimesConfig.id.in(displayTimesIds));
        Map<String, AdvertisementDisplayTimesConfig> displayTimesConfigMap = Linq4j.asEnumerable(advertisementDisplayTimesConfigByIds).toMap(displayTimes -> displayTimes.getId(), displayTimes -> displayTimes);

        Iterable<AdvertisementDurationConfig> advertisementDurationConfigByIds = advertisementDurationConfigRepository.findAll(qAdvertisementDurationConfig.id.in(durationConfigIds));
        Map<String, AdvertisementDurationConfig> durationConfigMap = Linq4j.asEnumerable(advertisementDurationConfigByIds).toMap(duration -> duration.getId(), duration -> duration);

        Iterable<AdvertisementSizeConfig> advertisementSizeConfigByPositionTypeAndTerminalTypes = advertisementSizeConfigRepository.findAll(qAdvertisementSizeConfig.advertisementPositionType.in(positionTypeIds).and(qAdvertisementSizeConfig.terminalType.in(terminalTypeIds)));
        Map<String, AdvertisementSizeConfig> positionTypeConfigMap = Linq4j.asEnumerable(advertisementSizeConfigByPositionTypeAndTerminalTypes).toMap(positionType -> String.valueOf(positionType.getAdvertisementPositionType()) + positionType.getTerminalType(), positionType -> positionType);

        List<ContractAdvertisementPositionConfig> contractAdvertisementPositionConfigs = Linq4j.asEnumerable(contractAdvertisementPositionConfigViewModels).select(viewModel -> {
            ContractAdvertisementPositionConfig contractAdvertisementPositionConfig = ApiBeanUtils.copyProperties(viewModel, ContractAdvertisementPositionConfig.class);

            if (TerminalTypeEnum.CashRegister.getValue().equals(viewModel.getTerminalType())) {
                AdvertisementDisplayTimesConfig advertisementDisplayTimesConfig = displayTimesConfigMap.getOrDefault(viewModel.getAdvertisementDisplayTimesConfigId(), null);
                if (advertisementDisplayTimesConfig == null)
                    throw new BusinessException("投放次数配置不存在");
                BeanUtils.copyProperties(advertisementDisplayTimesConfig, contractAdvertisementPositionConfig);
            }
            AdvertisementDurationConfig advertisementDurationConfig = durationConfigMap.getOrDefault(viewModel.getAdvertisementDurationConfigId(), null);
            if (advertisementDurationConfig == null)
                throw new BusinessException("播放时长配置不存在");
            BeanUtils.copyProperties(advertisementDurationConfig, contractAdvertisementPositionConfig);

            AdvertisementSizeConfig advertisementSizeConfig = positionTypeConfigMap.getOrDefault(String.valueOf(viewModel.getAdvertisementPositionType()) + viewModel.getTerminalType(), null);
            if (advertisementSizeConfig == null)
                throw new BusinessException("广告投放位置配置不存在");
            BeanUtils.copyProperties(advertisementSizeConfig, contractAdvertisementPositionConfig);
            contractAdvertisementPositionConfig.setAdvertisementSizeConfigId(advertisementSizeConfig.getId());

            contractAdvertisementPositionConfig.setId(UUIDUtils.generateOrderedUUID());
            contractAdvertisementPositionConfig.setContractId(contractId);
            return contractAdvertisementPositionConfig;
        }).toList();
        contractAdvertisementPositionConfigRepository.save(contractAdvertisementPositionConfigs);
    }

    public void validatedContractAdvertisementPositionConfigEnable(List<ContractAdvertisementPositionConfigViewModel> contractAdvertisementPositionConfigViewModels) {
        List<String> unEnablePositionIds = systemParamConfigRepository.findAll(q -> q.select(qSystemParamConfig.id).from(qSystemParamConfig).where(qSystemParamConfig.type.eq(SystemParamTypeEnum.AdvertisementPositionType.getValue()).and(qSystemParamConfig.enabled.isFalse())));
        Map<String, ContractAdvertisementPositionConfigViewModel> positionConfigViewModelMap = Linq4j.asEnumerable(contractAdvertisementPositionConfigViewModels).toMap(viewModel -> viewModel.getSystemParamAdvertisementPositionId(), viewModel -> viewModel);
        unEnablePositionIds.forEach(unEnablePositionId -> {
            if (positionConfigViewModelMap.keySet().contains(unEnablePositionId)) {
                ContractAdvertisementPositionConfigViewModel viewModel = positionConfigViewModelMap.getOrDefault(unEnablePositionId, null);
                if (viewModel != null) {
                    String terminalName = EnumUtils.toEnum(viewModel.getTerminalType(), TerminalTypeEnum.class).getDisplayName();
                    String positionTypeName = EnumUtils.toEnum(viewModel.getAdvertisementPositionType(), AdvertisementPositionTypeEnum.class).getDisplayName();
                    throw new BusinessException(String.format("当前系统中不存在%s广告位，请在%s终端下，取消勾选%s广告位", positionTypeName, terminalName, positionTypeName));
                }
            }
        });
    }

    public void resetContractStoreInfo(Contract contract) {
        if (!contract.getContractExtension().getHasCashRegister()) {
            contract.getContractExtension().setStoreACount(0);
            contract.getContractExtension().setStoreBCount(0);
            contract.getContractExtension().setStoreCCount(0);
            contract.getContractExtension().setStoreDCount(0);
            if (contractStoreRepository.exists(qContractStore.contractId.eq(contract.getId()))) {
                storeInfoMapper.decreaseContractStoreUsedCount(contract.getId());
                contractStoreRepository.deleteByContractId(contract.getId());
            }
        }
    }

    @Transactional
    public void importCustomerStorePlan(CustomerStorePlanChooseRequest request) {
        String contractId = request.getContractId();
        ContractExtension contractExtension = contractExtensionRepository.findOne(contractId);
        if(contractExtension==null)
            throw new BusinessException("合同不存在");
        CustomerStorePlan customerStorePlan = customerStorePlanRepository.findOne(request.getCustomerStorePlanId());
        if(customerStorePlan==null)
            throw new BusinessException("客户选点记录不存在");
        contractExtension.setCustomerStorePlanId(request.getCustomerStorePlanId());
        contractExtension.setCode(customerStorePlan.getCode());
        contractExtension.setStorePlanCount(customerStorePlan.getStoreCount());
        if (contractStoreRepository.exists(qContractStore.contractId.eq(contractId))) {
            storeInfoMapper.decreaseContractStoreUsedCount(contractId);
            contractMapper.deleteByContractId(contractId);
        }
        contractMapper.addContractStoreByCustomerStorePlanId(request);
        Enumerable<ContractStore> contractStores = Linq4j.asEnumerable(getContractStores(contractId));
        contractExtension.setStoreACount(contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.A.getValue())));
        contractExtension.setStoreBCount(contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.B.getValue())));
        contractExtension.setStoreCCount(contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.C.getValue())));
        contractExtension.setStoreDCount(contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.D.getValue())));
        contractExtensionRepository.save(contractExtension);
        storeInfoMapper.recalculateContractStoreUsedCount(contractId);
    }

    public Map<String, Integer> getAllCustomerStoreContractUseNum() {
        Map<String, Integer> map = new HashMap<>();
        List<CustomerStoreContractUseNum> list = contractMapper.getAllCustomerStoreContractUseNum();
        list.forEach(obj -> {
            map.put(obj.getCustomerStorePlanId(), obj.getUseNum());
        });
        return map;
    }

    /**
     * 所有执行过的合同的总收益金额
     */
    public double getExecutedContractTotalProfit() {
        return contractMapper.getExecutedContractTotalProfit();
    }
}
