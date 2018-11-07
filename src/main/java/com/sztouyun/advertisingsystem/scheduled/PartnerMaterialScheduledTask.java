package com.sztouyun.advertisingsystem.scheduled;

import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.mapper.PartnerMaterialMapper;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisementMaterial;
import com.sztouyun.advertisingsystem.model.partner.advertisement.QPartnerAdvertisementMaterial;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.repository.partner.PartnerAdvertisementMaterialRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.common.file.IFileService;
import com.sztouyun.advertisingsystem.utils.AdsFile;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.partner.PartnerMaterialUrlInfo;
import org.apache.calcite.linq4j.Linq4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Profile({"online"})
@Component
public class PartnerMaterialScheduledTask extends BaseScheduledTask{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PartnerAdvertisementMaterialRepository partnerAdvertisementMaterialRepository;
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;
    @Autowired
    private IFileService fileService;
    @Autowired
    private PartnerMaterialMapper partnerMaterialMapper;

    private final static QPartnerAdvertisementMaterial qPartnerAdvertisementMaterial = QPartnerAdvertisementMaterial.partnerAdvertisementMaterial;

    @Scheduled(cron = "${partner.material.update.task.time}")
    public void updatePartnerMaterialUrl() {
        AuthenticationService.setAdminLogin();
        ScheduledJob scheduledJob = new ScheduledJob(PartnerMaterialScheduledTask.class.getName());
        int pageSize = 100, pageIndex = 0;
        String remark ="";
        try {
            while(true) {
                Pageable pageable = new MyPageRequest(pageIndex, pageSize);
                List<PartnerAdvertisementMaterial> partnerAdvertisementMaterials = partnerAdvertisementMaterialRepository.findAll(q -> q.selectFrom(qPartnerAdvertisementMaterial)
                        .where(qPartnerAdvertisementMaterial.url.isEmpty().and(qPartnerAdvertisementMaterial.originalUrl.isNotEmpty()))
                        .offset(pageable.getOffset()).limit(pageable.getPageSize())
                );

                if (CollectionUtils.isEmpty(partnerAdvertisementMaterials)) {
                    break;
                }

                List<String> originUrlsDistinct = Linq4j.asEnumerable(partnerAdvertisementMaterials).select(a -> a.getOriginalUrl()).distinct().toList();

                List<PartnerMaterialUrlInfo> existsPartnerMaterialUrlInfo = partnerAdvertisementMaterialRepository.findAll(q -> q.select(Projections.bean(PartnerMaterialUrlInfo.class, qPartnerAdvertisementMaterial.originalUrl, qPartnerAdvertisementMaterial.url))
                        .from(qPartnerAdvertisementMaterial).where(qPartnerAdvertisementMaterial.originalUrl.in(originUrlsDistinct).and(qPartnerAdvertisementMaterial.url.isNotEmpty()))
                        .groupBy(qPartnerAdvertisementMaterial.originalUrl));
                Map<String, String> existsUrlMapping = Linq4j.asEnumerable(existsPartnerMaterialUrlInfo).toMap(a -> a.getOriginalUrl(), b -> b.getUrl());

                partnerAdvertisementMaterials.forEach(partnerMaterial -> {
                    String url = "";
                    if (existsUrlMapping.containsKey(partnerMaterial.getOriginalUrl())) {
                        url = existsUrlMapping.get(partnerMaterial.getOriginalUrl());
                    } else {
                        AdsFile adsFile = null;
                        try {
                            adsFile = new AdsFile(partnerMaterial.getOriginalUrl(),partnerMaterial.getMaterialType());
                            url = fileService.upload(adsFile.getInputStream(),adsFile.getLength(),adsFile.getMaterialContentTypeEnum().getContentType(),partnerMaterial.getId()+ "."+adsFile.getMaterialContentTypeEnum().getSuffix());
                        } catch (Exception e) {
                            logger.error("上传素材文件失败", e);
                        }
                    }
                    partnerMaterial.setUrl(url);
                });
                partnerMaterialMapper.batchUpdatePartnerMaterialUrl(partnerAdvertisementMaterials);
                pageIndex++;
            }
        } catch (Exception e) {
            logger.error("更新第三方广告素材url失败",e);
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            remark = "更新第三方广告素材url失败";
            scheduledJob.setPageNum(pageIndex);
            scheduledJob.setSuccessed(false);
        }
        scheduledJob.setRemark(remark);
        scheduledJobRepository.save(scheduledJob);
    }
}
