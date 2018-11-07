package com.sztouyun.advertisingsystem.service.advertisement;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.AdvertisementMapper;
import com.sztouyun.advertisingsystem.mapper.ContractMapper;
import com.sztouyun.advertisingsystem.model.adProfitShare.AdvertisementProfit;
import com.sztouyun.advertisingsystem.model.advertisement.*;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.*;
import com.sztouyun.advertisingsystem.model.material.Material;
import com.sztouyun.advertisingsystem.model.material.QMaterial;
import com.sztouyun.advertisingsystem.model.mongodb.MapReduceResult;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitConfig;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitModeEnum;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementStoreDailyProfit;
import com.sztouyun.advertisingsystem.model.system.*;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementMaterialRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractAdvertisementPositionConfigRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractExtensionRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractStoreRepository;
import com.sztouyun.advertisingsystem.repository.material.MaterialRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.material.MaterialService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.service.system.SystemParamConfigService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.*;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.MaterialItemViewModel;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractPlatformInfo;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractStoreInfo;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementStoreInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.MaterialLinkMonitorRequest;
import com.sztouyun.advertisingsystem.viewmodel.statistic.ContractAdvertisementDeliveryStatistic;
import com.sztouyun.advertisingsystem.viewmodel.store.ContractStoreQueryRequest;
import com.sztouyun.advertisingsystem.viewmodel.system.TerminalAndAdvertisementPositionInfo;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@Service
@CacheConfig(cacheNames = "advertisements")
public class AdvertisementService extends BaseService {
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ContractStoreRepository contractStoreRepository;
    @Autowired
    private ContractExtensionRepository contractExtensionRepository;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private AdvertisementMaterialRepository advertisementMaterialRepository;
    @Autowired
    private StoreService storeService;
    @Autowired
    private ContractAdvertisementPositionConfigRepository contractAdvertisementPositionConfigRepository;
    @Autowired
    private AdvertisementMapper advertisementMapper;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private AdvertisementOperationLogService advertisementOperationLogService;
    @Autowired
    private SystemParamConfigService systemParamConfigService;
    @Autowired
    private MongoTemplate mongoTemplate;

    private final QContractStore qContractStore = QContractStore.contractStore;
    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;
    private final QMaterial qMaterial = QMaterial.material;
    private final QAdvertisementMaterial qAdvertisementMaterial = QAdvertisementMaterial.advertisementMaterial;
    private final QContractAdvertisementPositionConfig qContractAdvertisementPositionConfig = QContractAdvertisementPositionConfig.contractAdvertisementPositionConfig;

    @Transactional
    public String createAdvertisement(Advertisement advertisement,List<MaterialItemViewModel> materials) {
        if (advertisementRepository.existsByAdvertisementName(advertisement.getAdvertisementName()))
            throw new BusinessException("广告名称不能重复，无法保存.");
        Contract contract = validateContractAndTime(advertisement);
        ContractStatusEnum contractStatusEnum=EnumUtils.toEnum(contract.getContractStatus(),ContractStatusEnum.class);
        List<ContractStatusEnum> availableStatus = Arrays.asList(ContractStatusEnum.PendingExecution,ContractStatusEnum.Executing);
        if (!availableStatus.contains(contractStatusEnum))
            throw new BusinessException("合同不是待执行状态或执行中状态");
        if (advertisement.isEnableProfitShare()
                && Objects.equals(AdvertisementProfitModeEnum.FixedAmount.getValue(), advertisement.getMode())
                && (advertisement.getProfitShareStandardAmount() == null || advertisement.getProfitShareStandardAmountUnit() == null))
            throw new BusinessException("请选择正确的分成配置");
        if (advertisement.getAdvertisementType().equals(MaterialTypeEnum.Text.getValue()) && materials.isEmpty()) {
            setNewTextMaterial(advertisement, contract);
        }else{
            setNonNewTextMaterial(advertisement, materials);
        }
        advertisement.setCustomerId(contract.getCustomerId());
        advertisement.setAdvertisementPeriod(DateUtils.getIntervalDays(advertisement.getStartTime(), advertisement.getEndTime()));
        AdvertisementProfit advertisementProfit = new AdvertisementProfit();
        advertisement.setAdvertisementProfit(advertisementProfit);
        advertisementProfit.setAdvertisement(advertisement);
        advertisementRepository.save(advertisement);
        return advertisement.getId();
    }

    /**
     * 校验广告的素材二维码位置选择是否正确
     */
    private void checkQRCodePosition(AdvertisementMaterial advertisementMaterial) {
        if (advertisementMaterial.getQRCodePosition() != null && advertisementMaterial.getQRCodePositionEnum() == null)
            throw new BusinessException("请选择正确的二维码位置");
    }

    private void setNonNewTextMaterial(Advertisement advertisement, List<MaterialItemViewModel> materials) {
        if(null==materials || materials.size()<1)
            throw new BusinessException("缺少素材");
        List<String> positionIds = Linq4j.asEnumerable(materials).select(m->m.getPositionId()).toList();
        List<ContractAdvertisementPositionConfig> contractAdvertisementPositionConfigs= contractAdvertisementPositionConfigRepository.findAll(qContractAdvertisementPositionConfig.id.in(positionIds),new JoinDescriptor().innerJoin(qContractAdvertisementPositionConfig.advertisementSizeConfig));
        if(contractAdvertisementPositionConfigs==null || contractAdvertisementPositionConfigs.size()<materials.size())
            throw new BusinessException("缺少广告位配置");
        if( contractAdvertisementPositionConfigs.size()>materials.size())
            throw new BusinessException("缺少素材");
        Map<String,AdvertisementSizeConfig> sizeConfigMap=Linq4j.asEnumerable(contractAdvertisementPositionConfigs).toMap(a->a.getId(),a->a.getAdvertisementSizeConfig());
        removeAllMaterials(advertisement.getId());
        List<TerminalAndAdvertisementPositionInfo> terminalAndAdvertisementPositionInfos = systemParamConfigService.getTerminalAndAdvertisementPositionInfos();
        advertisementMaterialRepository.save(Linq4j.asEnumerable(materials).select(materialItem -> {
            validateAndGetMaterial(advertisement, materialItem);
            AdvertisementMaterial advertisementMaterial = new AdvertisementMaterial();
            BeanUtils.copyProperties(materialItem, advertisementMaterial);
            advertisementMaterial.setId(UUIDUtils.generateOrderedUUID());// 使用 BeanUtils.copyProperties 会覆盖掉 id, 所以此处重新设置 id
            advertisementMaterial.setAdvertisementId(advertisement.getId());
            advertisementMaterial.setMaterialId(materialItem.getId());

            checkQRCodePosition(advertisementMaterial);

            AdvertisementSizeConfig advertisementSizeConfig = sizeConfigMap.get(materialItem.getPositionId());
            advertisementMaterial.setHighRatio(advertisementSizeConfig.getHighRatio());
            advertisementMaterial.setWidthRatio(advertisementSizeConfig.getWidthRatio());
            advertisementMaterial.setHorizontalResolution(advertisementSizeConfig.getHorizontalResolution());
            advertisementMaterial.setVerticalResolution(advertisementSizeConfig.getVerticalResolution());

            boolean clickUrlEnable = urlIsEnable(terminalAndAdvertisementPositionInfos, advertisementSizeConfig.getTerminalType(), advertisementSizeConfig.getAdvertisementPositionType(), MaterialLinkTypeEnum.MaterialClick);
            advertisementMaterial.setClickUrlEnable(clickUrlEnable);
            if (!clickUrlEnable) {
                advertisementMaterial.setMaterialClickUrl(null);
            }
            checkUrl(materialItem.getMaterialClickUrl());

            boolean qRCodeUrlEnable = urlIsEnable(terminalAndAdvertisementPositionInfos, advertisementSizeConfig.getTerminalType(), advertisementSizeConfig.getAdvertisementPositionType(), MaterialLinkTypeEnum.MaterialQRCode);
            advertisementMaterial.setQRCodeUrlEnable(qRCodeUrlEnable);
            if (!qRCodeUrlEnable) {
                advertisementMaterial.setMaterialQRCodeUrl(null);
                advertisementMaterial.setQRCodePosition(null);
                advertisementMaterial.setIsNotePhoneNumber(null);
            }
            checkUrl(materialItem.getMaterialQRCodeUrl());

            return advertisementMaterial;
        }).toList());
    }

    /**
     * 校验url
     */
    private void checkUrl(String url) {
        if (!StringUtils.isEmpty(url) && !url.matches(Constant.REGEX_URL)) throw new BusinessException("请输入有效的Url链接");
    }

    private boolean urlIsEnable(List<TerminalAndAdvertisementPositionInfo> list, Integer terminalType, Integer advertisementPositionType, MaterialLinkTypeEnum materialLinkTypeEnum) {
        return list.stream().filter(e -> Objects.equals(e.getValue(), terminalType)).findAny().get().getChildren().stream().filter(e -> Objects.equals(e.getValue(), advertisementPositionType)).findAny().get().getChildren().stream().filter(e -> Objects.equals(e.getValue(), materialLinkTypeEnum.getValue())).findAny().get().getChecked();
    }

    private void setNewTextMaterial(Advertisement advertisement, Contract contract) {
        materialService.saveTextMaterial(contract.getCustomerId(), advertisement.getData());
    }

    @Transactional
    public void deleteAdvertisement(String id) {
        Advertisement advertisement = advertisementRepository.findOne(id);
        if (advertisement == null)
            throw new BusinessException("广告不存在！");
        if (!advertisement.getAdvertisementStatus().equals(AdvertisementStatusEnum.PendingCommit.getValue()))
            throw new BusinessException("当前状态不允许删除！");
        advertisementRepository.delete(id);
    }

    @Transactional
    public void updateAdvertisement(Advertisement advertisement,List<MaterialItemViewModel> materials) {
        if (advertisement.getAdvertisementStatus().equals(AdvertisementStatusEnum.PendingAuditing.getValue()) && getUser().getRoleTypeEnum().equals(RoleTypeEnum.SaleMan)) {
            throw new BusinessException("您没有该操作权限");
        }
        Advertisement oldAdvertisement = advertisementRepository.findOne(advertisement.getId());
        if (oldAdvertisement == null)
            throw new BusinessException("广告不存在！");
        if (advertisementRepository.existsByIdNotAndAdvertisementName(advertisement.getId(), advertisement.getAdvertisementName()))
            throw new BusinessException("广告名称不能重复，无法保存.");
        List<Integer> editableAdvertisementStatuses = Arrays.asList(AdvertisementStatusEnum.PendingCommit.getValue()
                , AdvertisementStatusEnum.PendingAuditing.getValue()
                , AdvertisementStatusEnum.PendingDelivery.getValue());
        if (!editableAdvertisementStatuses.contains(oldAdvertisement.getAdvertisementStatus()))
            throw new BusinessException("当前广告状态不允许编辑！");
        if (advertisement.isEnableProfitShare()
                && Objects.equals(AdvertisementProfitModeEnum.FixedAmount.getValue(), advertisement.getMode())
                && (advertisement.getProfitShareStandardAmount() == null || advertisement.getProfitShareStandardAmountUnit() == null))
            throw new BusinessException("请选择正确的分成配置");
        Contract contract = validateContractAndTime(advertisement);
        if (advertisement.getAdvertisementType().equals(MaterialTypeEnum.Text.getValue())) {
            setNewTextMaterial(advertisement, contract);
        }else {
            setNonNewTextMaterial(advertisement, materials);
        }
        if(!AdvertisementStatusEnum.PendingCommit.getValue().equals(oldAdvertisement.getAdvertisementStatus())) {
            advertisementOperationLogService.saveAdvertisementOperationLog(new AdvertisementOperationLog(advertisement.getId(), AdvertisementOperationEnum.Edit.getValue(),true,""));
        }
        advertisement.setUpdatedTime(new Date());
        advertisement.setAdvertisementStatus(AdvertisementStatusEnum.PendingCommit.getValue());
        advertisement.setAdvertisementPeriod(DateUtils.getIntervalDays(advertisement.getStartTime(), advertisement.getEndTime()));
        if (!advertisement.isEnableProfitShare()) {
            advertisement.setProfitShareStandardAmount(null);
            advertisement.setProfitShareStandardAmountUnit(null);
        }
        advertisement.setAdvertisementProfit(oldAdvertisement.getAdvertisementProfit());
        advertisementRepository.save(advertisement);
    }

    public Advertisement getAdvertisementAuthorized(String id) {
        Advertisement advertisement = advertisementRepository.findOneAuthorized(qAdvertisement.id.eq(id).and(AuthenticationService.getUserAuthenticationFilter(QAdvertisement.advertisement.creatorId)), new JoinDescriptor().innerJoin(qAdvertisement.contract));
        if (null == advertisement)
            throw new BusinessException("广告不存在或权限不足！");
        return advertisement;
    }

    public Advertisement getAdvertisement(String id) {
        Advertisement advertisement = advertisementRepository.findOne(qAdvertisement.id.eq(id), new JoinDescriptor().innerJoin(qAdvertisement.contract));
        if (null == advertisement)
            throw new BusinessException("广告不存在！");
        return advertisement;
    }

    public Page<Advertisement> queryAdvertisementList(AdvertisementPageInfoViewModel viewModel) {
        if (viewModel.getHighRisk()) {
            viewModel.setAdvertisementStatus(AdvertisementStatusEnum.Delivering.getValue());
        }
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize(), new QSort(qAdvertisement.updatedTime.desc()));
        BooleanBuilder predicate = new BooleanBuilder();
        if (!StringUtils.isEmpty(viewModel.getAdvertisementName())) {
            predicate = predicate.and(qAdvertisement.advertisementName.contains(viewModel.getAdvertisementName()));
        }
        if (!StringUtils.isEmpty(viewModel.getContractName())) {
            predicate = predicate.and(qAdvertisement.contract.contractName.contains(viewModel.getContractName()));
        }
        if (!StringUtils.isEmpty(viewModel.getCustomerName())) {
            predicate = predicate.and(qAdvertisement.customer.name.contains(viewModel.getCustomerName()));
        }
        if (viewModel.getAdvertisementStatus() > 0) {
            predicate = predicate.and(qAdvertisement.advertisementStatus.eq(viewModel.getAdvertisementStatus()));
        }
        if (viewModel.getHighRisk()) {
            predicate = predicate.and(qAdvertisement.expectedDueDay.lt(new Date()));
        }
        Page<Advertisement> pages = advertisementRepository.findAllAuthorized(predicate, pageable, viewModel.getJoinDescriptor());
        return pages;
    }

    public Map<String, List<String>> getAdvertisementDeliveryPlatforms(List<String> contractIds){
        Enumerable<ContractPlatformInfo> advertisementPositionConfigs = Linq4j.asEnumerable(contractAdvertisementPositionConfigRepository
                .findAll(s->s.select(Projections.bean(ContractPlatformInfo.class,
                        qContractAdvertisementPositionConfig.contractId.as("contractId"),
                        qContractAdvertisementPositionConfig.terminalType.as("terminalType")))
                        .from(qContractAdvertisementPositionConfig).where(qContractAdvertisementPositionConfig.contractId.in(contractIds)).orderBy(qContractAdvertisementPositionConfig.terminalType.asc())));
        return advertisementPositionConfigs.toMap(a -> a.getContractId(), c -> advertisementPositionConfigs.where(b -> b.getContractId().equals(c.getContractId())).select(f->EnumUtils.toEnum(f.getTerminalType(),TerminalTypeEnum.class).getDisplayName()).distinct().toList());
    }

    /**
     * 广告状态信息统计（广告总数、待审核、待投放、投放中）
     */
    public AdvertisementStatusViewModel getAdvertisementStatusStatistics(AdvertisementStatusCountViewModel dto) {
        List<AdvertisementStatusViewModel> contractStatusList = advertisementRepository.findAllAuthorized(queryFactory -> {
            JPAQuery jpaQueryBase = queryFactory
                    .select(Projections.bean(AdvertisementStatusViewModel.class, qAdvertisement.advertisementStatus.as("advertisementStatus"), qAdvertisement.count().as("statusCount")))
                    .from(qAdvertisement);

            jpaQueryBase = filterQuery(jpaQueryBase, dto);
            jpaQueryBase.groupBy(qAdvertisement.advertisementStatus);
            return jpaQueryBase;
        });
        long total = 0;
        AdvertisementStatusViewModel viewModel = new AdvertisementStatusViewModel();
        for (AdvertisementStatusViewModel statusViewModel : contractStatusList) {
            if (statusViewModel.getAdvertisementStatus().equals(AdvertisementStatusEnum.PendingAuditing.getValue())) {
                viewModel.setPendingAuditing(statusViewModel.getStatusCount());
            }
            if (statusViewModel.getAdvertisementStatus().equals(AdvertisementStatusEnum.PendingDelivery.getValue())) {
                viewModel.setPendingDelivery(statusViewModel.getStatusCount());
            }
            if (statusViewModel.getAdvertisementStatus().equals(AdvertisementStatusEnum.Delivering.getValue())) {
                viewModel.setDeliverying(statusViewModel.getStatusCount());
            }
            total += statusViewModel.getStatusCount();
        }
        viewModel.setTotal(total);
        return viewModel;
    }


    public List<TerminalAdvertisementConfigInfo> getContractAdvertisementConfig(String contractId) {
        ContractExtension contractExtension = contractExtensionRepository.findOne(contractId);
        if (contractExtension == null)
            throw new BusinessException("合同数据不存在");
        List<ContractAdvertisementPositionConfig> advertisementPositionConfigs=contractAdvertisementPositionConfigRepository.findAll(qContractAdvertisementPositionConfig.contractId.eq(contractId),new JoinDescriptor().innerJoin(qContractAdvertisementPositionConfig.advertisementSizeConfig));
        Map<Integer,TerminalAdvertisementConfigInfo> configMap=new HashMap<>();
        advertisementPositionConfigs.stream().forEach(advertisementPositionConfig->{
            TerminalAdvertisementConfigInfo configInfo;
            Integer terminalType=advertisementPositionConfig.getTerminalType();
            if(configMap.containsKey(terminalType)){
                configInfo=configMap.get(terminalType);
            }else{
                configInfo=createBaseTerminalAdvertisementConfigInfo(advertisementPositionConfig,contractExtension);
            }
            AdvertisementDeliveryConfigInfo advertisementDeliveryConfigInfo=new AdvertisementDeliveryConfigInfo();
            advertisementDeliveryConfigInfo.setPositionId(advertisementPositionConfig.getId());
            advertisementDeliveryConfigInfo.setPositionType(advertisementPositionConfig.getAdvertisementPositionType());
            advertisementDeliveryConfigInfo.setPositionTypeName(EnumUtils.getDisplayName(advertisementPositionConfig.getAdvertisementPositionType(),AdvertisementPositionTypeEnum.class));
            advertisementDeliveryConfigInfo.setResolution(advertisementPositionConfig.getAdvertisementSizeConfig().getImgSpecification());
            advertisementDeliveryConfigInfo.setDuration(advertisementPositionConfig.getFullDuration());
            advertisementDeliveryConfigInfo.setDisplayTimes(advertisementPositionConfig.getDisplayTimesInfo());
            configInfo.getDeliveryConfigList().add(advertisementDeliveryConfigInfo);
            configMap.put(terminalType,configInfo);
        });
        return Linq4j.asEnumerable(configMap.values()).toList();
    }


    public List<TerminalAdvertisementConfigInfo> getAdvertisementMaterialInfo(String advertisementId,Boolean isEdit) {
        List<AdvertisementMaterial> advertisementMaterials=advertisementMaterialRepository.findAll(qAdvertisementMaterial.advertisementId.eq(advertisementId),new JoinDescriptor().innerJoin(qAdvertisementMaterial.material).innerJoin(qAdvertisementMaterial.contractAdvertisementPositionConfig));
        Map<Integer,TerminalAdvertisementConfigInfo> configMap=new HashMap<>();
        advertisementMaterials.stream().forEach(advertisementMaterial->{
            ContractAdvertisementPositionConfig advertisementPositionConfig=advertisementMaterial.getContractAdvertisementPositionConfig();
            TerminalAdvertisementConfigInfo configInfo;
            Integer terminalType=advertisementPositionConfig.getTerminalType();
            if(configMap.containsKey(terminalType)){
                configInfo=configMap.get(terminalType);
            }else{
                configInfo=createBaseTerminalAdvertisementConfigInfo(advertisementPositionConfig,advertisementPositionConfig.getContractExtension());
            }
            AdvertisementDeliveryConfigInfo advertisementDeliveryConfigInfo=new AdvertisementDeliveryConfigInfo();
            advertisementDeliveryConfigInfo.setPositionId(advertisementPositionConfig.getId());
            advertisementDeliveryConfigInfo.setPositionType(advertisementPositionConfig.getAdvertisementPositionType());
            advertisementDeliveryConfigInfo.setPositionTypeName(EnumUtils.getDisplayName(advertisementPositionConfig.getAdvertisementPositionType(),AdvertisementPositionTypeEnum.class));
            String advertisementMaterialSpecification=advertisementMaterial.getImgSpecification();
            String configSpecification=advertisementPositionConfig.getAdvertisementSizeConfig().getImgSpecification();
            if(isEdit){
                advertisementDeliveryConfigInfo.setResolution(advertisementMaterialSpecification.equals(configSpecification)?advertisementMaterialSpecification:configSpecification);
            }else{
                advertisementDeliveryConfigInfo.setResolution(advertisementMaterialSpecification);
            }
            advertisementDeliveryConfigInfo.setMaterialInfo(advertisementMaterial.getMaterial());
            advertisementDeliveryConfigInfo.setMaterialClickUrl(advertisementMaterial.getMaterialClickUrl());
            advertisementDeliveryConfigInfo.setMaterialQRCodeUrl(advertisementMaterial.getMaterialQRCodeUrl());
            advertisementDeliveryConfigInfo.setQRCodePosition(advertisementMaterial.getQRCodePosition());
            QRCodePositionEnum qrCodePositionEnum = advertisementMaterial.getQRCodePositionEnum();
            advertisementDeliveryConfigInfo.setQRCodePositionName(qrCodePositionEnum == null ? null : qrCodePositionEnum.getDisplayName());
            advertisementDeliveryConfigInfo.setClickUrlEnable(advertisementMaterial.getClickUrlEnable());
            advertisementDeliveryConfigInfo.setQRCodeUrlEnable(advertisementMaterial.getQRCodeUrlEnable());

            advertisementDeliveryConfigInfo.setDuration(advertisementPositionConfig.getFullDuration());
            advertisementDeliveryConfigInfo.setDisplayTimes(advertisementPositionConfig.getDisplayTimesInfo());
            advertisementDeliveryConfigInfo.setIsNotePhoneNumber(advertisementMaterial.getIsNotePhoneNumber());
            configInfo.getDeliveryConfigList().add(advertisementDeliveryConfigInfo);
            configMap.put(terminalType,configInfo);
        });
        return Linq4j.asEnumerable(configMap.values()).toList();
    }

    @Cacheable(key = "#p0",condition="#p0!=null")
    public List<TerminalAdvertisementConfigInfo> getAdvertisementMaterialInfo(String advertisementId, ContractExtension contractExtension) {
        List<AdvertisementMaterial> advertisementMaterials=advertisementMaterialRepository.findAll(qAdvertisementMaterial.advertisementId.eq(advertisementId),new JoinDescriptor().innerJoin(qAdvertisementMaterial.material).innerJoin(qAdvertisementMaterial.contractAdvertisementPositionConfig));
        Map<Integer,TerminalAdvertisementConfigInfo> configMap=new HashMap<>();
        advertisementMaterials.stream().forEach(advertisementMaterial->{
            ContractAdvertisementPositionConfig advertisementPositionConfig=advertisementMaterial.getContractAdvertisementPositionConfig();
            TerminalAdvertisementConfigInfo configInfo;
            Integer terminalType=advertisementPositionConfig.getTerminalType();
            if(configMap.containsKey(terminalType)){
                configInfo=configMap.get(terminalType);
            }else{
                configInfo=createBaseTerminalAdvertisementConfigInfo(advertisementPositionConfig,contractExtension);
            }
            AdvertisementDeliveryConfigInfo advertisementDeliveryConfigInfo=new AdvertisementDeliveryConfigInfo();
            advertisementDeliveryConfigInfo.setPositionId(advertisementPositionConfig.getId());
            advertisementDeliveryConfigInfo.setPositionType(advertisementPositionConfig.getAdvertisementPositionType());
            advertisementDeliveryConfigInfo.setPositionTypeName(EnumUtils.getDisplayName(advertisementPositionConfig.getAdvertisementPositionType(),AdvertisementPositionTypeEnum.class));
            String advertisementMaterialSpecification=advertisementMaterial.getImgSpecification();
            advertisementDeliveryConfigInfo.setResolution(advertisementMaterialSpecification);
            advertisementDeliveryConfigInfo.setMaterialInfo(advertisementMaterial.getMaterial());
            advertisementDeliveryConfigInfo.setDuration(advertisementPositionConfig.getFullDuration());
            advertisementDeliveryConfigInfo.setDisplayTimes(advertisementPositionConfig.getDisplayTimesInfo());
            configInfo.getDeliveryConfigList().add(advertisementDeliveryConfigInfo);
            configMap.put(terminalType,configInfo);
        });
        return Linq4j.asEnumerable(configMap.values()).toList();
    }
    private TerminalAdvertisementConfigInfo createBaseTerminalAdvertisementConfigInfo(ContractAdvertisementPositionConfig contractAdvertisementPositionConfig, ContractExtension contractExtension){
        TerminalAdvertisementConfigInfo configInfo;
        if(contractAdvertisementPositionConfig.getTerminalType().equals(TerminalTypeEnum.CashRegister.getValue())){
            configInfo= new CashRegisterAdvertisementConfigInfo(contractAdvertisementPositionConfig.getTerminalType());
            configInfo.setSpecialConfig(contractExtension);
        }else{
            configInfo= new TerminalAdvertisementConfigInfo(contractAdvertisementPositionConfig.getTerminalType());
        }
        return configInfo;
    }

    public  Map<Integer,ContractAdvertisementPositionConfig> getAdvertisementPositionMap(List<ContractAdvertisementPositionConfig> list){
        Map<Integer,ContractAdvertisementPositionConfig> map=new HashMap<>();
        list.forEach(advertisementPositionConfig -> {
            map.put(advertisementPositionConfig.getAdvertisementPositionType(),advertisementPositionConfig);
        });
        return map;
    }

    private Contract validateContractAndTime(Advertisement advertisement) {
        if (!advertisement.getStartTime().before(advertisement.getEndTime()))
            throw new BusinessException("广告投放结束时间必须大于开始时间");

        Contract contract = contractRepository.findOne(advertisement.getContractId());
        if (contract == null)
            throw new BusinessException("合同不存在");

        ContractExtension contractExtension = contract.getContractExtension();
        boolean flag = DateUtils.goe(advertisement.getStartTime(),contractExtension.getStartTime()) && DateUtils.loe(advertisement.getStartTime(),contractExtension.getEndTime())
                && DateUtils.goe(advertisement.getEndTime(),contractExtension.getStartTime()) && DateUtils.loe(advertisement.getEndTime(),contractExtension.getEndTime());
        if (!flag)
            throw new BusinessException("广告开始时间和结束时间，必须在合同开始时间和结束时间之内");

        return contract;
    }

    private void validateAndGetMaterial(Advertisement advertisement, MaterialItemViewModel materialItem) {
        if (StringUtils.isEmpty(materialItem.getId()))
            throw new BusinessException("投放素材不能为空，请上传或选择广告素材");
        Material material= materialRepository.findOne(materialItem.getId());
        if (material == null)
            throw new BusinessException("素材不存在");
        if (!advertisement.getAdvertisementType().equals(MaterialTypeEnum.ImgVideo.getValue()) && !advertisement.getAdvertisementType().equals(material.getMaterialType()))
            throw new BusinessException("素材类型和广告类型不匹配");
        if (material.getMaterialType().equals(MaterialTypeEnum.Video.getValue())) {
            if (FileUtils.getFileByteSize(material.getMaterialSize()) > materialService.getVideoLimitSize())
                throw new BusinessException("所选视频素材超过大小限制");
        }

        ContractAdvertisementPositionConfig contractAdvertisementPositionConfig=contractAdvertisementPositionConfigRepository.findOne(materialItem.getPositionId());
        AdvertisementSizeConfig advertisementSizeConfig=contractAdvertisementPositionConfig.getAdvertisementSizeConfig();
        advertisementSizeConfig.validateMaterialType(material.getMaterialType());
        if (material.getMaterialType().equals(MaterialTypeEnum.Img.getValue())) {
            String materialImgSpecification = material.getMaterialSpecification();
            if (advertisementSizeConfig==null || !advertisementSizeConfig.getImgSpecification().equals(materialItem.getResolution()) || !advertisementSizeConfig.getImgSpecification().equals(materialImgSpecification))
                throw new BusinessException(EnumUtils.getDisplayName(contractAdvertisementPositionConfig.getTerminalType(),TerminalTypeEnum.class)+"的"+(EnumUtils.getDisplayName(contractAdvertisementPositionConfig.getAdvertisementPositionType(), AdvertisementPositionTypeEnum.class))+"图片分辨率与当前配置不匹配",Constant.SIZE_CONFIG_CHANGED);
        }
    }

    public Page<Advertisement> getAdvertisementListByCustomer(CustomerAdvertisementRequest dto) {
        Pageable pageable = new MyPageRequest(dto.getPageIndex(), dto.getPageSize(), new QSort(qAdvertisement.createdTime.desc()));
        BooleanBuilder predicate = new BooleanBuilder();
        predicate = predicate.and(qAdvertisement.customerId.eq(dto.getCustomerId()));
        Page<Advertisement> pages = advertisementRepository.findAll(predicate, pageable);
        return pages;
    }

    public Page<Material> getAdvertisementMaterialsByCustomer(Pageable pageable, String customerId) {
        return materialRepository.findAll(qMaterial.customerId.eq(customerId), pageable);
    }

    public List<AdvertisementStatusStatisticsViewModel> getAdvertisementDeliveringStatistics(AdvertisementStatusCountViewModel viewModel) {
        NumberExpression<Integer> deliveringAdvertising = qAdvertisement.advertisementStatus.when(AdvertisementStatusEnum.Delivering.getValue()).then(1).otherwise(0);
        NumberExpression<Integer> takeOffAdvertising = qAdvertisement.advertisementStatus.when(AdvertisementStatusEnum.TakeOff.getValue()).then(1).otherwise(0);
        NumberExpression<Integer> highRiskAdvertising = new CaseBuilder().when(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue()).and(qAdvertisement.expectedDueDay.lt(new Date()))).then(1).otherwise(0);

        return advertisementRepository.findAllAuthorized(queryFactor -> {
            JPAQuery jpaQuery = queryFactor.select(Projections.bean(AdvertisementStatusStatisticsViewModel.class,
                    deliveringAdvertising.sum().as("deliveringTotals"),
                    takeOffAdvertising.sum().as("takeOffTotals"),
                    highRiskAdvertising.sum().as("highRiskTotals")
            )).from(qAdvertisement);
            return filterQuery(jpaQuery, viewModel);
        });
    }

    public Page<AdvertisementAreaCountInfo> getAdvertisementReportInfo(String id, Pageable pageable) {
        Advertisement advertisement = advertisementRepository.findOne(id);
        if (advertisement == null)
            throw new BusinessException("广告ID无效");
        String contractId = advertisement.getContractId();
        if (StringUtils.isEmpty(contractId))
            throw new BusinessException("广告ID无效");
        Page<AdvertisementAreaCountInfo> page = storeService.getAdvertisementAreaCountInfoList(contractId, pageable);
        return page;
    }


    public ContractAdvertisementDeliveryStatistic getContractAdvertisementDeliveryStatistics(Contract contract) {
        ContractAdvertisementDeliveryStatistic contractAdvertisementDeliveryStatistics = new ContractAdvertisementDeliveryStatistic();

        int effectivePeriod = contract.getContractAdvertisementPeriod() - contract.getUsedContractPeriod();

        Advertisement deliveringAdvertisement = advertisementRepository.findOne(qAdvertisement.contractId.eq(contract.getId()).and(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue())));
        if (deliveringAdvertisement != null) {
            int intervalDays = DateUtils.getIntervalDays(deliveringAdvertisement.getEffectiveStartTime(), new Date());
            effectivePeriod = effectivePeriod - intervalDays;
        }

        long hasAdvertisedCount = advertisementRepository.count(
                qAdvertisement.contractId.eq(contract.getId()).and(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.TakeOff.getValue()).or(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Finished.getValue()))));

        contractAdvertisementDeliveryStatistics.setHasAdvertisedTimes(hasAdvertisedCount);
        contractAdvertisementDeliveryStatistics.setEffectivePeriod(effectivePeriod);

        return contractAdvertisementDeliveryStatistics;
    }

     public Date getCustomerLastestDeliveryTime(String customerId){
        if(StringUtils.isEmpty(customerId))
            return null;

         return advertisementRepository.findOne(q->q
                 .select(qAdvertisement.effectiveStartTime).from(qAdvertisement)
                 .where(qAdvertisement.customerId.eq(customerId)).orderBy(qAdvertisement.effectiveStartTime.desc()));
     }


    /**
     * 根据广告名称,或者合同名称, 或者客户名称进行查询
     *
     * @param jpaQuery
     * @param viewModel
     * @return
     */
    private JPAQuery filterQuery(JPAQuery jpaQuery, AdvertisementStatusCountViewModel viewModel) {
        if (!StringUtils.isEmpty(viewModel.getAdvertisementName())) {
            jpaQuery.where(qAdvertisement.advertisementName.contains(viewModel.getAdvertisementName()));
        } else if (!StringUtils.isEmpty(viewModel.getContractName())) {
            jpaQuery.innerJoin(qAdvertisement.contract).where(qAdvertisement.contract.contractName.contains(viewModel.getContractName()));
        } else if (!StringUtils.isEmpty(viewModel.getCustomerName())) {
            jpaQuery.innerJoin(qAdvertisement.customer).where(qAdvertisement.customer.name.contains(viewModel.getCustomerName()));
        }
        return jpaQuery;
    }

    /**
     * 指定合同的已投放次数
     * @return
     */
    public Long getContractDeliveryTimesByContractID(String contractId){
        return advertisementRepository.count(qAdvertisement.contractId.eq(contractId).and(qAdvertisement.advertisementStatus.in(AdvertisementStatusEnum.Delivering.getValue(),AdvertisementStatusEnum.Finished.getValue()).
                or((qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.TakeOff.getValue()).and(qAdvertisement.effectiveStartTime.isNotNull())))));
    }

    public List<AdvertisementMaterialPositionViewModel> getAdvertisementMaterialPosition(String contractId) {
        return advertisementMapper.getAdvertisementMaterialPosition(contractId);
    }


    @Transactional
    public void removeAllMaterials(String id){
        advertisementMaterialRepository.deleteByAdvertisementId(id);
    }

    public Long getTotalStoreCount(String advertisementId) {
        return advertisementMapper.getTotalStoreCount(advertisementId);
    }
	public Page<StoreAdvertisementInfo> getStoreAdvertisements(AdvertisementStoreInfoRequest request){
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        return pageResult(advertisementMapper.getStoreAdvertisements(request),pageable,advertisementMapper.getStoreAdvertisementCount(request));
    }

    public List<Advertisement> getPendingDeliveryAdvertisements(Date currentTime){
        return advertisementRepository.findAll(
                q->q.selectFrom(qAdvertisement).where(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.PendingDelivery.getValue()).and(qAdvertisement.startTime.loe(currentTime)).and(qAdvertisement.contract.advertisements.any().advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue()).not())).
                        innerJoin(qAdvertisement.contract).fetchJoin().
                        orderBy(qAdvertisement.startTime.asc(),qAdvertisement.createdTime.asc()));
	}

    /**
     * 通过广告ID获得所有已配置素材链接的广告位置和对应的广告素材
     * @param advertisementId
     * @return
     */
    public Map<AdvertisementPositionCategoryEnum, AdvertisementMaterial> getConfiguredMaterialLinkAdvertisementPositionCategoryEnums(String advertisementId) {
        Map<AdvertisementPositionCategoryEnum, AdvertisementMaterial> map = new HashMap<>();
        advertisementMaterialRepository.findAll(qAdvertisementMaterial.advertisementId.eq(advertisementId)).forEach(e -> {
            AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum = e.getContractAdvertisementPositionConfig().getAdvertisementPositionCategoryEnum();
            if (!StringUtils.isEmpty(e.getMaterialClickUrl()) || !StringUtils.isEmpty(e.getMaterialQRCodeUrl())) {
                map.put(advertisementPositionCategoryEnum, e);
            }
        });
        return map;
    }


    public Map<AdvertisementPositionCategoryEnum, AdvertisementMaterial> getConfiguredMaterialLinkAdvertisementPositionCategoryEnums(List<AdvertisementMaterial> advertisementMaterials) {
        Map<AdvertisementPositionCategoryEnum, AdvertisementMaterial> map = new HashMap<>();
        advertisementMaterials.forEach(e -> {
            AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum = e.getContractAdvertisementPositionConfig().getAdvertisementPositionCategoryEnum();
            if (!StringUtils.isEmpty(e.getMaterialClickUrl()) || !StringUtils.isEmpty(e.getMaterialQRCodeUrl())) {
                map.put(advertisementPositionCategoryEnum, e);
            }
        });
        return map;
    }

    public Page<AdvertisementMaterial> getAdvertisementMaterialContainLinkUrl(MaterialLinkMonitorRequest request){
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qAdvertisementMaterial.advertisementId.eq(request.getAdvertisementId()));
        List<Integer> linkTypes = request.getLinkTypes();
        if(linkTypes.size()>0){
            linkTypes.forEach(linkType->{
                switch (EnumUtils.toEnum(linkType,MaterialLinkTypeEnum.class)){
                    case MaterialClick:
                        predicate.and(qAdvertisementMaterial.materialClickUrl.isNotNull());
                        break;
                    case MaterialQRCode:
                        predicate.and(qAdvertisementMaterial.materialQRCodeUrl.isNotNull());
                        break;
                }
            });
        }
        if(request.getLinkType()==null){
            predicate.and(qAdvertisementMaterial.materialClickUrl.isNotNull().or(qAdvertisementMaterial.materialQRCodeUrl.isNotNull()));
        }
        if(request.getAdvertisementPositionCategory()!=null){
            AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum = EnumUtils.toEnum(request.getAdvertisementPositionCategory(), AdvertisementPositionCategoryEnum.class);
            predicate.and(qAdvertisementMaterial.contractAdvertisementPositionConfig.terminalType.eq(advertisementPositionCategoryEnum.getTerminalType().getValue()));
            predicate.and(qAdvertisementMaterial.contractAdvertisementPositionConfig.advertisementPositionType.eq(advertisementPositionCategoryEnum.getAdvertisementPositionType().getValue()));
        }
        return advertisementMaterialRepository.findAll(predicate,new MyPageRequest(request.getPageIndex(),request.getPageSize(),new QSort(qAdvertisementMaterial.contractAdvertisementPositionConfig.terminalType.asc(),qAdvertisementMaterial.contractAdvertisementPositionConfig.advertisementPositionType.asc())));
    }

    public List<AdvertisementStoreDailyProfitViewModel> getAdvertisementStoreDailyProfitList(AdvertisementStoreDailyProfitRequest request) {
        String contractId = advertisementRepository.findOne(q->q.select(qAdvertisement.contractId).from(qAdvertisement).where(qAdvertisement.id.eq(request.getId())));
        if(StringUtils.isEmpty(contractId))
            throw new BusinessException("广告ID无效");
        request.setContractId(contractId);
        if(request.hasStoreFilter()){
            request.setStoreIds(getStoreIds(request));
        }
        List<AggregationOperation> list = getAdvertisementStoreDailyProfitAggregationOperations(request,true);
        list.add(project("date","shareAmount","storeId")
                .and("store.storeNo").as("shopId")
                .and("store.storeName").as("storeName")
                .and("store.provinceId").as("provinceId")
                .and("store.cityId").as("cityId")
                .and("store.regionId").as("regionId")
                .and("store.storeAddress").as("storeAddress")
                .and("store.deviceId").as("deviceId"));

        Aggregation agg = newAggregation(list);
        return mongoTemplate.aggregate(agg, AdvertisementStoreDailyProfit.class, AdvertisementStoreDailyProfitViewModel.class).getMappedResults();
    }

    private List<AggregationOperation> getAdvertisementStoreDailyProfitAggregationOperations(AdvertisementStoreDailyProfitRequest request,boolean withPage) {
        List<AggregationOperation> list=new ArrayList<>();
        list.add(match(Criteria.where("advertisementId").is(request.getId())));
        if(request.getStartTime() !=null && request.getEndTime() == null){
            list.add(match(Criteria.where("date").gte(request.getStartTime().getTime())));
        }
        if(request.getEndTime() !=null && request.getStartTime() == null){
            list.add(match(Criteria.where("date").lte(request.getEndTime().getTime())));
        }
        if(request.getEndTime() !=null && request.getStartTime() !=null) {
            list.add(match(Criteria.where("date").gte(request.getStartTime().getTime()).lte(request.getEndTime().getTime())));
        }
        if(request.getStoreIds() !=null){
            list.add(match(Criteria.where("storeId").in(request.getStoreIds())));
        }
        if(withPage){
            list.add(sort(Sort.Direction.DESC,"id"));
            list.add(skip(request.getPageIndex()*request.getPageSize().longValue()));
            list.add(limit(request.getPageSize()));
            list.add(lookup("storeInfo", "storeId", "_id", "store"));
            list.add(unwind("store"));
        }
        return list;
    }

    private List<String> getStoreIds(AdvertisementStoreDailyProfitRequest request){
        var filter = new BooleanBuilder(qContractStore.contractId.eq(request.getContractId()));
        var qStoreInfo = qContractStore.storeInfo;
        if(!StringUtils.isEmpty(request.getProvinceId())){
            filter.and(qStoreInfo.provinceId.eq(request.getProvinceId()));
        }
        if(!StringUtils.isEmpty(request.getCityId())){
            filter.and(qStoreInfo.cityId.eq(request.getCityId()));
        }
        if(!StringUtils.isEmpty(request.getRegionId())){
            filter.and(qStoreInfo.regionId.eq(request.getRegionId()));
        }
        if(!StringUtils.isEmpty(request.getStoreName())){
            filter.and(qStoreInfo.storeName.contains(request.getStoreName()));
        }
        if(!StringUtils.isEmpty(request.getDeviceId())){
            filter.and(qStoreInfo.deviceId.contains(request.getDeviceId()));
        }
        if(!StringUtils.isEmpty(request.getShopId())){
            filter.and(qStoreInfo.storeNo.contains(request.getShopId()));
        }
        return contractStoreRepository.findAll(q->q.select(qContractStore.storeId).from(qContractStore).where(filter));
    }

    public AdvertisementStoreDailyProfitGroupInfo getAdvertisementStoreDailyProfitGroupInfo(AdvertisementStoreDailyProfitRequest request) {
        List<AggregationOperation> aggregationOperations = getAdvertisementStoreDailyProfitAggregationOperations(request,false);
        aggregationOperations.add(group().count().as("count").sum("shareAmount").as("totalAmount"));
        AdvertisementStoreDailyProfitGroupInfo info = mongoTemplate.aggregate(newAggregation(aggregationOperations), AdvertisementStoreDailyProfit.class, AdvertisementStoreDailyProfitGroupInfo.class).getUniqueMappedResult();
        return info==null?new AdvertisementStoreDailyProfitGroupInfo():info;
    }

    @Transactional
    public void saveAdvertisement(Advertisement advertisement, List<MaterialItemViewModel> materials, AdvertisementProfitConfig profitConfig) {
        if (Objects.isNull(advertisement.getId())) {
            advertisement.setId(UUIDUtils.generateOrderedUUID());
            createAdvertisement(advertisement, materials);
        } else {
            updateAdvertisement(advertisement, materials);
        }

        if (advertisement.isEnableProfitShare() && Objects.nonNull(profitConfig)) {
            profitConfig.setId(advertisement.getId());
            mongoTemplate.save(profitConfig);
        }
    }

    /**
     * 根据广告ID获得该广告的所有城市
     */
    public Map<String, String> getCitys(String contractId) {
        if (StringUtils.isEmpty(contractId)) throw new BusinessException("合同ID不能为空");
        return contractMapper.getAreas(contractId).stream().collect(Collectors.toMap(Area::getId, Area::getName));
    }

    public AdvertisementProfitConfig getAdvertisementProfitConfig(String advertisementId){
        return mongoTemplate.findById(advertisementId,AdvertisementProfitConfig.class);
    }

    public boolean existsAdvertisementProfitConfig(String advertisementId){
        return mongoTemplate.exists(new Query(Criteria.where("_id").is(advertisementId)),AdvertisementProfitConfig.class);
    }

    public Page<ContractStoreInfo> storeList(ContractStoreQueryRequest request) {
        return storeService.queryStoreList(request,new MyPageRequest(request.getPageIndex(), request.getPageSize()));
    }

    public Map<String, AdvertisementSettlementStatistic> getAdvertisementSettlementStatisticsMap(String advertisementId, List<String> storeIds) {
        if(storeIds==null||storeIds.size()==0)
            return new HashMap<>();
        Query query = new Query(Criteria.where("advertisementId").is(advertisementId));
        query.addCriteria(Criteria.where("storeId").in(storeIds));
        MapReduceResults<AdvertisementSettlementStatisticResult> mapReduceResults = mongoTemplate.mapReduce(query,
                "advertisementStoreDailyProfit",
                "classpath:script/advertisementStore/mapFunc.js",
                "classpath:script/advertisementStore/reduceFunc.js",
                new MapReduceOptions().outputTypeInline(),
                AdvertisementSettlementStatisticResult.class
        );
        Map<String, AdvertisementSettlementStatistic> map=new HashMap<>();
        for (MapReduceResult<String, AdvertisementSettlementStatistic> mapReduceResult : mapReduceResults) {
            map.put(mapReduceResult.getId(),mapReduceResult.getValue());
        }
        return map;
    }
}
