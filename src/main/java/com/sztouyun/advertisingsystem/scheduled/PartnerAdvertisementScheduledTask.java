package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerMongoLog;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerMongoLogTypeEnum;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.partner.config.OOHLinkApiConfig;
import com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb.OOHLinkAdvertisementExtension;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PartnerAdvertisementScheduledTask extends BaseScheduledTask{
    @Autowired
    private OOHLinkApiConfig oohLinkApiConfig;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private static final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    @Scheduled(cron = "${partner.oohlink.log.report.cron.minute}")
    public void reportOOHLinkLog() {
        AuthenticationService.setAdminLogin();
        Long lastSuccessTime = getLastSuccessTime();

        Query query = new Query();
        query.addCriteria(new Criteria("partnerId").is(oohLinkApiConfig.getPartnerId()).and("finishTime").exists(true).gte(lastSuccessTime).lte(new Date().getTime()));
        query.fields().exclude("monitorUrls").exclude("startDisplayMonitorUrls");
        List<PartnerAdvertisementDeliveryRecord> records = mongoTemplate.find(query, PartnerAdvertisementDeliveryRecord.class);
        List<String> storeIds = Linq4j.asEnumerable(records).select(a -> a.getStoreId()).distinct().toList();
        Map<String, String> storeNoMap = Linq4j.asEnumerable(storeInfoRepository.findAll(qStoreInfo.id.in(storeIds))).toMap(a -> a.getId(), b -> b.getStoreNo());

        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATETIME);
        for (var r : records) {
            try {
                OOHLinkAdvertisementExtension extend = (OOHLinkAdvertisementExtension) r.getExtend();
                StringBuffer sb = new StringBuffer();
                sb.append("playCode=").append(storeNoMap.get(r.getStoreId()))
                        .append("&planId=").append(r.getPartnerAdvertisementId().split("X")[1])
                        .append("&requestId=").append(r.getRequestId())
                        .append("&channelId=").append(extend.getChannelId())
                        .append("&fileUrl=").append(r.getOriginalUrl())
                        .append("&fileName=").append(extend.getFileName())
                        .append("&type=").append(r.getMaterialType())
                        .append("&duration=").append(oohLinkApiConfig.getDuration())
                        .append("&endTime=").append(sdf.format(new Date(r.getFinishTime())));
                if (r.getStartTime() == null) {
                    sb.append("&beginTime=").append(sdf.format(new LocalDateTime(r.getFinishTime()).minusSeconds(oohLinkApiConfig.getDuration()).toDate()));
                } else {
                    sb.append("&beginTime=").append(sdf.format(new Date(r.getStartTime())));
                }
                restTemplate.getForEntity(oohLinkApiConfig.getUploadLogUrl() + "?" + sb.toString(), String.class);
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
                logger.error("RecordId:"+r.getId()+",上报奥凌失败",e);
            }
        }
        saveOrUpdateLog();
    }

    private Long getLastSuccessTime() {
        Query queryLog = new Query();
        queryLog.addCriteria(new Criteria("partnerId").is(oohLinkApiConfig.getPartnerId()).and("logType").is(PartnerMongoLogTypeEnum.UPLOAD_LOG.getValue()));
        List<PartnerMongoLog> partnerMongoLogs = mongoTemplate.find(queryLog, PartnerMongoLog.class);
        if (CollectionUtils.isEmpty(partnerMongoLogs)) {
            return new LocalDate(2018, 7, 1).toDate().getTime();
        }
        return partnerMongoLogs.get(0).getSuccessTime();
    }

    private void saveOrUpdateLog() {
        Criteria criteria = new Criteria("partnerId").is(oohLinkApiConfig.getPartnerId()).and("logType").is(PartnerMongoLogTypeEnum.UPLOAD_LOG.getValue());
        if(mongoTemplate.exists(new Query(criteria), PartnerMongoLog.class)) {
            Update update = new Update();
            update.set("successTime", new Date().getTime());
            mongoTemplate.updateMulti(new Query(criteria), update, PartnerMongoLog.class);
        } else {
            mongoTemplate.save(new PartnerMongoLog(oohLinkApiConfig.getPartnerId(), new Date().getTime(), PartnerMongoLogTypeEnum.UPLOAD_LOG.getValue()));
        }
    }
}
