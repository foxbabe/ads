package com.sztouyun.advertisingsystem.service.material;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.material.*;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementSizeConfig;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementSizeConfigRepository;
import com.sztouyun.advertisingsystem.repository.material.PartnerMaterialOperationLogRepository;
import com.sztouyun.advertisingsystem.repository.material.PartnerMaterialRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.common.CodeGenerationService;
import com.sztouyun.advertisingsystem.common.file.IFileService;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.AuditMaterialNotificationData;
import com.sztouyun.advertisingsystem.service.system.AuditReasonConfigService;
import com.sztouyun.advertisingsystem.utils.AdsFile;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.partnerMaterial.AuditMaterialRequest;
import com.sztouyun.advertisingsystem.viewmodel.partnerMaterial.CreateMaterialRequest;
import com.sztouyun.advertisingsystem.viewmodel.partnerMaterial.MaterialStatusStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.partnerMaterial.PartnerMaterialRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wenfeng on 2018/1/29.
 */
@Service
public class PartnerMaterialService extends BaseService {
    @Autowired
    private PartnerMaterialRepository partnerMaterialRepository;
    @Autowired
    private IFileService fileService;
    @Autowired
    private AdvertisementSizeConfigRepository advertisementSizeConfigRepository;
    @Autowired
    private CodeGenerationService codeGenerationService;
    @Autowired
    private PartnerMaterialOperationLogRepository partnerMaterialOperationLogRepository;
    @Autowired
    private AuditReasonConfigService auditReasonConfigService;

    private final static QPartnerMaterial qPartnerMaterial=QPartnerMaterial.partnerMaterial;

    @Value(value = "${advertisement.upload.video.size}")
    private Long videoLimitSize;

    public void create(CreateMaterialRequest request){
        if(partnerMaterialRepository.exists(qPartnerMaterial.partnerId.eq(request.getPartnerId()).and(qPartnerMaterial.thirdPartId.eq(request.getThirdPartId()))))
            throw new BusinessException("素材已存在");
        PartnerMaterial partnerMaterial= ApiBeanUtils.copyProperties(request,PartnerMaterial.class);
        handleMaterial(partnerMaterial);
    }

    @Transactional
    public void handleMaterial(PartnerMaterial partnerMaterial){
        AdsFile adsFile=new AdsFile(partnerMaterial.getOriginalUrl(),partnerMaterial.getMaterialType());
        AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum= EnumUtils.toEnum(partnerMaterial.getAdvertisementPositionCategory(),AdvertisementPositionCategoryEnum.class);
        validateMaterial(adsFile,advertisementPositionCategoryEnum);
        String adsURL=fileService.upload(adsFile.getInputStream(),adsFile.getLength(),adsFile.getMaterialContentTypeEnum().getContentType(),partnerMaterial.getId()+ "."+adsFile.getMaterialContentTypeEnum().getSuffix());
        partnerMaterial.setUrl(adsURL);
        if(MaterialTypeEnum.Img.getValue().equals(partnerMaterial.getMaterialType())){
            partnerMaterial.setMaterialSpecification(adsFile.getImgSpecification());
        }
        partnerMaterial.setMaterialSize(FileUtils.convertFileSize(adsFile.getLength()));
        partnerMaterial.setMaterialCode(codeGenerationService.generateCode(CodeTypeEnum.PMTR));
        partnerMaterialRepository.save(partnerMaterial);
        adsFile.close();
    }

    public Page<PartnerMaterial> getPartnerMaterials(PartnerMaterialRequest request){
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize(), new QSort(qPartnerMaterial.createdTime.desc()));
        return partnerMaterialRepository.findAllAuthorized(filter(request),pageable,new JoinDescriptor().leftJoin(qPartnerMaterial.partner));
    }

    private BooleanBuilder filter(PartnerMaterialRequest request){
        BooleanBuilder predicate = new BooleanBuilder();
        if(request.getMaterialType()!=null){
            predicate.and(qPartnerMaterial.materialType.eq(request.getMaterialType()));
        }
        if(request.getAdvertisementPositionType()!=null){
            predicate.and(qPartnerMaterial.advertisementPositionType.eq(request.getAdvertisementPositionType()));
        }
        if(request.getPartnerMaterialStatus()!=null){
            predicate.and(qPartnerMaterial.materialStatus.eq(request.getPartnerMaterialStatus()));
        }
        if(!StringUtils.isEmpty(request.getPartnerId())){
            predicate.and(qPartnerMaterial.partnerId.eq(request.getPartnerId()));
        }
        if(!StringUtils.isEmpty(request.getMaterialCode())){
            predicate.and(qPartnerMaterial.materialCode.contains(request.getMaterialCode()));
        }
        if(request.getStartTime()!=null){
            predicate.and(qPartnerMaterial.createdTime.goe(request.getStartTime()));
        }
        if(request.getEndTime()!=null){
            predicate.and(qPartnerMaterial.createdTime.loe(request.getEndTime()));
        }
        return predicate;
    }


    public void auditMaterial(AuditMaterialRequest request){
        PartnerMaterial partnerMaterial=partnerMaterialRepository.findOneAuthorized(qPartnerMaterial.id.eq(request.getId()));
        if(partnerMaterial==null)
            throw new BusinessException("素材ID无效");
        if(!partnerMaterial.getMaterialStatus().equals(PartnerMaterialStatusEnum.PendingAuditing.getValue()))
            throw new BusinessException("素材当前状态无法审核");
        AuditMaterialNotificationData auditMaterialNotificationData=new AuditMaterialNotificationData();
        PartnerMaterialOperationLog partnerMaterialOperationLog=new PartnerMaterialOperationLog(request.getId(), PartnerMaterialOperationEnum.Auditing,request.getApprove(),request.getAuditRemark());
        if(request.getApprove()){
            partnerMaterial.setMaterialStatus(PartnerMaterialStatusEnum.Approved.getValue());
        }else{
            partnerMaterial.setMaterialStatus(PartnerMaterialStatusEnum.Rejected.getValue());
            partnerMaterialOperationLog.setReasonId(request.getReasonId());
            partnerMaterialOperationLog.setSubReasonId(request.getSubReasonId());
            auditReasonConfigService.updateAuditReasonConfigCount(Arrays.asList(request.getReasonId(),request.getSubReasonId()));
        }
        auditMaterialNotificationData.setMessage(request.getAuditRemark());
        auditMaterialNotificationData.setSuccess(request.getApprove());
        auditMaterialNotificationData.setThirdPartId(partnerMaterial.getThirdPartId());
        auditMaterialNotificationData.setPartnerId(partnerMaterial.getPartnerId());
        partnerMaterial.setAuditorId(getUser().getId());
        partnerMaterial.setAuditTime(new DateTime().toDate());
        partnerMaterialRepository.save(partnerMaterial);
        partnerMaterialOperationLogRepository.save(partnerMaterialOperationLog);
        publishOpenApiNotification(auditMaterialNotificationData);
    }

    public void validateMaterial(AdsFile file,AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum){
        AdvertisementSizeConfig advertisementSizeConfig=advertisementSizeConfigRepository.findFirstByTerminalTypeAndAdvertisementPositionType(advertisementPositionCategoryEnum.getTerminalType().getValue(),advertisementPositionCategoryEnum.getAdvertisementPositionType().getValue());
        if(file.getMaterialContentTypeEnum().getMaterialTypeEnum().equals(MaterialTypeEnum.Img)){
            if(advertisementSizeConfig==null || !advertisementSizeConfig.getImgSpecification().equals(file.getImgSpecification()))
                throw new BusinessException("广告尺寸配置不匹配");
        }else if (file.getMaterialContentTypeEnum().getMaterialTypeEnum().equals(MaterialTypeEnum.Video)) {
            if (file.getLength() > videoLimitSize)
                throw new BusinessException("视频文件大小超过限制!");
        }
    }

    public List<MaterialStatusStatisticViewModel> statusStatistic(PartnerMaterialRequest request){
        return partnerMaterialRepository.findAllAuthorized(q->q.select(Projections.bean(MaterialStatusStatisticViewModel.class,qPartnerMaterial.materialStatus,qPartnerMaterial.id.count().as("materialCount"))).from(qPartnerMaterial).where(filter(request)).groupBy(qPartnerMaterial.materialStatus));
    }

    public PartnerMaterial findById(String id){
        PartnerMaterial partnerMaterial=partnerMaterialRepository.findOne(id);
        if(partnerMaterial==null)
            throw new BusinessException("素材ID无效");
        return partnerMaterial;
    }

    public PartnerMaterial getPartnerMaterial(String partnerId,String thirdPartId) {
        if(StringUtils.isEmpty(thirdPartId))
            throw new BusinessException("素材不存在");
        PartnerMaterial partnerMaterial= partnerMaterialRepository.findOne(qPartnerMaterial.partnerId.eq(partnerId).and(qPartnerMaterial.thirdPartId.eq(thirdPartId)));
        if(partnerMaterial==null)
            throw new BusinessException("素材不存在");
        return partnerMaterial;
    }
}
