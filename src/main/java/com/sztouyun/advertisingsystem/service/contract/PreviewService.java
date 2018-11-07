package com.sztouyun.advertisingsystem.service.contract;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;
import com.sztouyun.advertisingsystem.ResourceDownloadThread;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.config.PreviewConfig;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.system.DurationUnitEnum;
import com.sztouyun.advertisingsystem.model.common.DisplayTimeUnitEnum;
import com.sztouyun.advertisingsystem.model.contract.*;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPriceConfig;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementPriceConfigRepository;
import com.sztouyun.advertisingsystem.repository.contract.*;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.common.file.IFileService;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.utils.*;
import com.sztouyun.advertisingsystem.utils.pdf.PDFHeaderFooter;
import com.sztouyun.advertisingsystem.utils.pdf.PdfToolkit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class PreviewService extends BaseService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private PreviewRepository previewRepository;

    @Autowired
    private IFileService fileService;

    @Autowired
    private PreviewConfig previewConfig;

    @Autowired
    private ContractOperationLogRepository contractOperationLogRepository;

    @Autowired
    private AdvertisementPriceConfigRepository advertisementPriceConfigRepository;

    @Autowired
    private ContractAdvertisementPositionConfigService contractAdvertisementPositionConfigService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private ContractTemplateRepository contractTemplateRepository;
    @Autowired
    private ContractTemplateService contractTemplateService;
    @Autowired
    private ResourceDownloadThread resourceDownloadThread;


    /**
     * 根据合同ID查询preview记录，URL有效直接返回，否则重新生成
     * @param contractId
     * @return
     */
    @Transactional
    public String getOrCreatePreview(String contractId){
        Contract contract=contractRepository.findOne(contractId);
        if(null==contract)
            throw new BusinessException("该合同不存在");
        Preview preview=previewRepository.findByContractId(contractId);
        if(preview==null){
            preview=new Preview();
        }else{
            if(contract.getContractStatus()>ContractStatusEnum.PendingAuditing.getValue()){
                Date auditingDate=getOperationAuditingTime(contractId);
                if(auditingDate !=null && preview.getUpdatedTime().after(auditingDate)){
                    return preview.getFilePath();
                }
            }
        }
        ContractForm contractForm = getContractForm(contract);
        ContractTemplate contractTemplate=null;
        if(ContractStatusEnum.PendingCommit.getValue().equals(contract.getContractStatus())){
            contractTemplate=contractTemplateService.getLatestContractTemplateByType(ContractTemplateTypeEnum.FREEMARKER.getValue());
        }else{
            contractTemplate=contract.getContractTemplate();
        }
        ContractTemplateTypeEnum contractTemplateTypeEnum=EnumUtils.toEnum(contractTemplate.getTemplateType(),ContractTemplateTypeEnum.class);
        String fullFmtPath=previewConfig.getFullFmtPath(contractTemplate.getId(),contractTemplateTypeEnum.getExt()
        );
        resourceDownloadThread.downloadResource(contract.getContractTemplate().getUrl(),fullFmtPath);
        String filePath=null;
        if(contractTemplateTypeEnum.equals(ContractTemplateTypeEnum.FREEMARKER)){
            filePath= createPdfByFtl(fullFmtPath,contractId,contractForm);
        }else{
            filePath=createPdf(contractForm,contract.getContractName());
        }
        preview.setUpdatedTime(new Date());
        preview.setContractId(contractId);
        preview.setFilePath(filePath);
        previewRepository.save(preview);
        return filePath;
    }

    /**
     * 准备表单和表格数据
     * @param contract
     * @return
     */
    private ContractForm getContractForm(Contract contract) {
        String contractId=contract.getId();
        ContractExtension contractExtension=contract.getContractExtension();
        ContractForm contractForm=new ContractForm();
        BeanUtils.copyProperties(contract,contractForm);
        BeanUtils.copyProperties(contractExtension,contractForm);
        contractForm.setStoreCount(contractExtension.getStoreACount(),contractExtension.getStoreBCount(),contractExtension.getStoreCCount(),contractExtension.getStoreDCount());
        contractForm.setDateOfStart(contractExtension.getStartTime());
        contractForm.setDateOfEnd(contractExtension.getEndTime());
        final List<StoreForm> storeFormList=new ArrayList<>();
        List<StoreInfo> storeInfos=contractRepository.getStoreInfoListByContractId(contractId);
        Map<String,Integer> storeStatisticInfo=new HashMap<>();
        if(storeInfos!=null && !storeInfos.isEmpty()){
            final Map<Integer,AdvertisementPriceConfig> configMap=getAdvertisementPriceConfigMap(contractId);
            storeInfos.stream().forEach(storeInfo -> {
                StoreForm storeForm=new StoreForm();
                BeanUtils.copyProperties(storeInfo,storeForm);
                String cityName=areaService.getAreaNameFromCache(storeInfo.getCityId());
                if(storeStatisticInfo.get(cityName)==null){
                    storeStatisticInfo.put(cityName,1);
                }else{
                    storeStatisticInfo.put(cityName,storeStatisticInfo.get(cityName)+1);
                }
                Integer storeType=storeInfo.getStoreType();
                Set<Integer> storeTypeSet= EnumUtils.toValueMap(StoreTypeEnum.class).keySet();
                if(!storeTypeSet.contains(storeType)){
                    logger.info("预览失败,门店类型不匹配");
                    throw new BusinessException("预览失败");
                }
                AdvertisementPriceConfig advertisementPriceConfig=configMap.get(storeType);
                if(advertisementPriceConfig==null) {
                    logger.info("预览失败,找不到该类型的门店配置");
                    throw new BusinessException("预览失败");
                }
                storeForm.setStoreCost(advertisementPriceConfig.getPrice());
                storeForm.setRemark("");
                storeFormList.add(storeForm);
            });
            setDeliveryInfo(contractForm,storeStatisticInfo);
        }
        contractForm.setStoreList(storeFormList);
        contractForm.setContractId(contractId);
        Integer signAfterDay=contractExtension.getSignAfterDay();
        if(signAfterDay>0){
            contractForm.setSignAfterDay(signAfterDay.toString());
        }else{
            contractForm.setSignAfterDay("");
        }
        setPlayInfo(contractForm,contractId);
        contractForm.setCost(contractExtension.getMediumCost(),contractExtension.getProductCost());
        contractForm.setTotalMonths(DateUtils.formateYmd(contractExtension.getTotalDays()));
        contractForm.setContractAdvertisementPeriod(DateUtils.formateYmd(contract.getContractAdvertisementPeriod()));
        contractForm.setSupplementary(contractExtension.getSupplementaryTerms());
        if(contract.getContractTemplate()==null){
            contractForm.setContractTemplateId(Constant.DEFAULT_TEMPALTE);
        }
        return contractForm;
    }

    public void  setPlayInfo(ContractForm contractForm,String contractId){
        List<ContractAdvertisementPositionConfig> list=contractAdvertisementPositionConfigService.getContractAdvertisementPositionConfigs(contractId);
        list.forEach(contractAdvertisementPositionConfig -> {
            String playDuration=contractAdvertisementPositionConfig.getDuration()+EnumUtils.getDisplayName(contractAdvertisementPositionConfig.getDurationUnit(),DurationUnitEnum.class)+"/次";
            String playTime=contractAdvertisementPositionConfig.getDisplayTimes()+"次/"+ EnumUtils.getDisplayName(contractAdvertisementPositionConfig.getTimeUnit(),DisplayTimeUnitEnum.class);
            PlayInfo playInfo=new PlayInfo();
            playInfo.setPlaysize(contractAdvertisementPositionConfig.getAdvertisementSizeConfig().getImgSpecification());
            playInfo.setPlayDuration(playDuration);
            playInfo.setPlayTime(playTime);
            contractForm.addPlayInfo(playInfo);
        });
    }

    /**
     * 设置投放信息
     * @param contractForm
     * @param storeStatisticInfo
     */
    private void setDeliveryInfo(ContractForm contractForm, Map<String,Integer> storeStatisticInfo){
        contractForm.setAmountOfCities(storeStatisticInfo.size());
        contractForm.setCities(MapUtil.getKeys(storeStatisticInfo));
        Map<String,Object> maxStore= MapUtil.getFirstPairByValue(storeStatisticInfo,MapUtil.DESC);
        contractForm.setTopCity(String.valueOf(maxStore.get("key")));
        contractForm.setStoreOfTopCity(Integer.valueOf(maxStore.get("value").toString()));
    }

    /**
     * 分两个步骤生成文件
     * 模板-》临时文件-》最终文件
     * @param contractForm
     * @return
     */
    private String createPdf(ContractForm contractForm,String contractName){
        String contractId=contractForm.getContractId();
        String jsonStr=JSON.toJSONString(contractForm);
        System.out.println(jsonStr);
        JSONObject jsonObject=JSON.parseObject(jsonStr);
        String tempPath= FileUtils.getTmpPath(previewConfig.getPkgtype())+previewConfig.getTempFilePath(contractId);
        String destPath= FileUtils.getTmpPath(previewConfig.getPkgtype())+previewConfig.getDestFilePath(contractId);
        String filePath=null;
        try {
            ContractTemplate contractTemplate=contractTemplateRepository.findOne(contractForm.getContractTemplateId());
            PdfUtils.createPdfFromTemplate(previewConfig,jsonObject,contractTemplate);
            if(contractTemplate.getTemplateType().equals(ContractTemplateTypeEnum.COMMON.getValue()) || contractTemplate.getTemplateType().equals(ContractTemplateTypeEnum.REQUIRE_FEES.getValue())){
                PdfUtils.mergeTableToPdf(previewConfig,jsonObject);
                File file =new File(destPath);
                filePath= fileService.upload(new FileInputStream(file),file.length(),"application/pdf",contractName+".pdf");
                FileUtils.clearFile(destPath);
            }else{
                File file =new File(tempPath);
                filePath= fileService.upload(new FileInputStream(file),file.length(),"application/pdf",contractName+".pdf");
            }
            logger.info("url:"+filePath);
            FileUtils.clearFile(tempPath);
            return filePath;
        } catch (IOException e) {
            logger.error("IOExcept:createPdf 文件读写异常",e);
            throw new BusinessException("预览失败！");
        } catch (DocumentException e) {
            logger.error("DocumentException:createPdf  Document操作异常 ",e);
            throw new BusinessException("预览失败！");
        }
    }

    /** 1.若审核通过后，根据审核时间所在区间查到
     * 2.未审核通过使用激活的配置
     * @param contractId
     * @return
     */
    private Map<Integer,AdvertisementPriceConfig> getAdvertisementPriceConfigMap(String contractId){
        Date date=getOperationAuditingTime(contractId);
        List<AdvertisementPriceConfig> list=null;
        if(date!=null){
            list=advertisementPriceConfigRepository.findAllByCreatedTimeBeforeAndUpdatedTimeAfter(date,date);
        }else{
            list=advertisementPriceConfigRepository.findAllByActived(true);
        }
        if(list==null || list.isEmpty()){
            logger.info("预览失败,找不到对应的价格配置");
            throw new BusinessException("预览失败,找不到对应的价格配置！");
        }
        Map<Integer,AdvertisementPriceConfig> configMap=new HashMap<>();
        list.stream().forEach(advertisementPriceConfig -> {
            configMap.put(advertisementPriceConfig.getStoreType(),advertisementPriceConfig);
        });
        return configMap;
    }

    private Date getOperationAuditingTime(String contractId){
       Date date=contractOperationLogRepository.findByLastestPassAuditingTime(contractId);
       return date;
    }

    public  String createPdfByFtl(String templatePath, String id, Object data){
        //pdf保存路径
        try {
            String destPath= FileUtils.getTmpPath(previewConfig.getPkgtype())+previewConfig.getDestFilePath(id);
            File file=new File(destPath);
            if(file.exists()){
                FileUtils.clearFile(destPath);
            }
            PDFHeaderFooter headerFooter=new PDFHeaderFooter();
            PdfToolkit kit=new PdfToolkit();
            kit.setHeaderFooterBuilder(headerFooter);
            //设置输出路径
            kit.setSaveFilePath(destPath);

            kit.exportToFile(templatePath,data);
            File pdf =new File(destPath);
            String remotePdfPath= fileService.upload(new FileInputStream(pdf),file.length(),"application/pdf",id+".pdf");
            FileUtils.clearFile(destPath);
            return  remotePdfPath;
        } catch (Exception e) {
            throw  new BusinessException("PDF生成失败{}");
        }

    }
}
