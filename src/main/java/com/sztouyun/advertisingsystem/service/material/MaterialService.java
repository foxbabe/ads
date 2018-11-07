package com.sztouyun.advertisingsystem.service.material;

import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractAdvertisementPositionConfig;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.material.Material;
import com.sztouyun.advertisingsystem.model.material.QMaterial;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementSizeConfig;
import com.sztouyun.advertisingsystem.repository.contract.ContractAdvertisementPositionConfigRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.repository.material.MaterialRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.common.file.IFileService;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.AdvertisementMaterialStatisticsViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@Service
public class MaterialService extends BaseService {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private IFileService fileService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContractAdvertisementPositionConfigRepository advertisementPositionConfigRepository;

    @Autowired
    private ContractAdvertisementPositionConfigRepository contractAdvertisementPositionConfigRepository;

    @Value(value = "${advertisement.upload.video.size}")
    private Long videoLimitSize;

    private static final Map<Integer, List<String>> materialExtensionMap = new HashMap<Integer, List<String>>() {
        {
            put(MaterialTypeEnum.Img.getValue(), new ArrayList<>(Arrays.asList("png", "jpg", "jpeg", "bmp")));
            put(MaterialTypeEnum.Video.getValue(), new ArrayList<>(Arrays.asList("flv", "avi", "mp4", "mkv", "wmv")));
        }
    };

    private final QMaterial qMaterial = QMaterial.material;

    private final QContract qContract = QContract.contract;

    @Transactional
    public Material uploadMaterial(String contractId, String positionId, String customerId, Integer materialType, MultipartFile file,String resolution) {

        if (!customerRepository.exists(customerId))
            throw new BusinessException("客户信息不存在");

        boolean existsContractCustomer = contractRepository.exists(qContract.customerId.eq(customerId).and(qContract.id.eq(contractId)));
        if (!existsContractCustomer)
            throw new BusinessException("不存在合同客户信息");
        if(MaterialTypeEnum.ImgVideo.getValue().equals(materialType)){
            materialType=getMaterialTypeByFileContentType(file);
        }
        validateFile(materialType, file,positionId,resolution);
        String fileUrl;
        try {
            fileUrl = fileService.upload(file.getInputStream(),file.getSize(),file.getContentType(),file.getOriginalFilename());
        } catch (IOException e) {
            throw new BusinessException("无效文件！");
        }
        Material material = new Material();
        material.setCustomerId(customerId);
        material.setData(fileUrl);
        material.setMaterialType(materialType);
        material.setFileName(file.getOriginalFilename());
        material.setMaterialSize(FileUtils.convertFileSize(file.getSize()));
        if (MaterialTypeEnum.Img.getValue().equals(materialType)) {
            material.setMaterialSpecification(getImgSpecification(positionId));
        }
        return materialRepository.save(material);
    }


    public Page<Material> getAllMaterials(Pageable pageable, String customerId, Integer materialType,String positionId) {

        if (!customerRepository.exists(customerId))
            throw new BusinessException("客户信息不存在");
        if(!materialType.equals(MaterialTypeEnum.Img.getValue()))
            return materialRepository.findAllAuthorized(qMaterial.customerId.eq(customerId).and(qMaterial.materialType.eq(materialType)), pageable);
        return materialRepository.findAllAuthorized(jpaQueryFactory -> jpaQueryFactory.select(qMaterial).from(qMaterial)
                .where(qMaterial.customerId.eq(customerId)
                        .and(qMaterial.materialType.eq(materialType))
                        .and(qMaterial.materialSpecification.eq(getImgSpecification(positionId)))), pageable);
    }

    public Material saveTextMaterial(String customerId, String data) {

        Material advertisementMaterial= materialRepository.findOne(queryFactory -> {
            return queryFactory.select(qMaterial).from(qMaterial).where(
                    qMaterial.customerId.eq(customerId).and(qMaterial.data.eq(data).and(qMaterial.materialType.eq(MaterialTypeEnum.Text.getValue()))));
        });

        if (null!=advertisementMaterial)
            return advertisementMaterial;

        advertisementMaterial = new Material();
        advertisementMaterial.setMaterialType(MaterialTypeEnum.Text.getValue());
        advertisementMaterial.setCustomerId(customerId);
        advertisementMaterial.setData(data);

         materialRepository.save(advertisementMaterial).getId();
        return advertisementMaterial;
    }

    public List<AdvertisementMaterialStatisticsViewModel> getMaterialStatistics(String customerId,String positionId) {
        List<AdvertisementMaterialStatisticsViewModel> resultList=new ArrayList<>();
        ContractAdvertisementPositionConfig positionConfig = contractAdvertisementPositionConfigRepository.findOne(positionId);
        if(positionConfig == null)
            throw new BusinessException("尺寸配置ID无效");
        List<AdvertisementMaterialStatisticsViewModel> materialStatisticsListItem = materialRepository.findAll(queryFactory -> queryFactory
                .select(Projections.bean(AdvertisementMaterialStatisticsViewModel.class, qMaterial.id.count().as("totalMaterials"), qMaterial.materialType))
                .from(qMaterial)
                .where(qMaterial.customerId.eq(customerId))
                .groupBy(qMaterial.materialType)
        );
        List<AdvertisementMaterialStatisticsViewModel> imgItems=getImageStatisticsListItem(customerId,positionId);
        Map<Integer,Long> imgCountMap=getCountMap( imgItems);
        Map<Integer,Long> allCountMap=getCountMap( materialStatisticsListItem);
        allCountMap.putAll(imgCountMap);
        Arrays.stream(MaterialTypeEnum.values()).forEach(materialTypeEnum -> {
            AdvertisementMaterialStatisticsViewModel advertisementMaterialStatisticsViewModel = new AdvertisementMaterialStatisticsViewModel();
            advertisementMaterialStatisticsViewModel.setMaterialType(materialTypeEnum.getValue());
            if (allCountMap.containsKey(materialTypeEnum.getValue())) {
                advertisementMaterialStatisticsViewModel.setTotalMaterials(allCountMap.get(materialTypeEnum.getValue()));
            }else {
                advertisementMaterialStatisticsViewModel.setTotalMaterials(0L);
            }
            if(materialTypeEnum.equals(MaterialTypeEnum.Img)){
                if(imgCountMap.containsKey(materialTypeEnum.getValue())){
                    advertisementMaterialStatisticsViewModel.setTotalMaterials(imgCountMap.get(materialTypeEnum.getValue()));
                }else{
                    advertisementMaterialStatisticsViewModel.setTotalMaterials(0L);
                }
            }
            resultList.add(advertisementMaterialStatisticsViewModel);

        });
        return resultList;
    }

    private Map<Integer, Long>  getCountMap( List<AdvertisementMaterialStatisticsViewModel> imgItems) {
        Map<Integer, Long> countMap=new HashMap<>();
        imgItems.forEach(advertisementMaterialStatisticsItem -> {
            Integer type=advertisementMaterialStatisticsItem.getMaterialType();
            countMap.put(type,advertisementMaterialStatisticsItem.getTotalMaterials());
        });
        return countMap;
    }

    public List<AdvertisementMaterialStatisticsViewModel> getImageStatisticsListItem(String customerId,String positionId){
        return materialRepository.findAllAuthorized(jpaQueryFactory -> jpaQueryFactory.select(Projections.bean(AdvertisementMaterialStatisticsViewModel.class, qMaterial.id.count().as("totalMaterials"), qMaterial.materialType)).
                from(qMaterial)
                .where(qMaterial.customerId.eq(customerId)
                        .and(qMaterial.materialSpecification.eq(getImgSpecification(positionId)))).groupBy(qMaterial.materialType));

    }
    
    /**
     * 验证文件是否有效
     *
     * @param materialType
     * @param file
     */
    private void validateFile(Integer materialType, MultipartFile file,String positionId,String resolution) {
        String fileExtension = FileUtils.getFileExtension(file.getOriginalFilename());

        boolean hasValidExtension = materialExtensionMap.get(materialType).contains(fileExtension);
        if (!hasValidExtension)
            throw new BusinessException("不支持该文件格式");
        ContractAdvertisementPositionConfig advertisementPositionConfig=advertisementPositionConfigRepository.findOne(positionId);
        AdvertisementSizeConfig advertisementSizeConfig=advertisementPositionConfig.getAdvertisementSizeConfig();
        advertisementSizeConfig.validateMaterialType(materialType);
        if (materialType.equals(MaterialTypeEnum.Img.getValue())) {
            try {
                BufferedImage sourceImg = ImageIO.read(file.getInputStream());

                int width = sourceImg.getWidth(); // 源图宽度
                int height = sourceImg.getHeight();// 源图高度
                if(advertisementSizeConfig==null || !advertisementSizeConfig.getImgSpecification().equals(resolution))
                    throw new BusinessException("广告尺寸配置已变更",Constant.SIZE_CONFIG_CHANGED);
                if ( !(advertisementSizeConfig.getHorizontalResolution().equals(width) && advertisementSizeConfig.getVerticalResolution().equals(height)))
                    throw new BusinessException(EnumUtils.toEnum(advertisementSizeConfig.getAdvertisementPositionType(), AdvertisementPositionTypeEnum.class).getDisplayName()+"图片分辨率与当前配置不匹配");
            } catch (IOException e) {
                logger.error("IOExcept:上传图片素材读写异常", e);
                throw new BusinessException("上传素材失败");
            }

        } else if (materialType.equals(MaterialTypeEnum.Video.getValue())) {
            if (file.getSize() > videoLimitSize)
                throw new BusinessException("视频文件大小超过限制!");
        }
    }

    public String getImgSpecification(String positionId) {
        ContractAdvertisementPositionConfig contractAdvertisementPositionConfig = advertisementPositionConfigRepository.findOne(positionId);
        if(contractAdvertisementPositionConfig == null)
            throw new BusinessException("图片规格不存在");
        return contractAdvertisementPositionConfig.getAdvertisementSizeConfig().getImgSpecification();
    }

    public Long getVideoLimitSize() {
        return videoLimitSize;
    }

    public void setVideoLimitSize(Long videoLimitSize) {
        this.videoLimitSize = videoLimitSize;
    }

    private Integer getMaterialTypeByFileContentType(MultipartFile file){
        String contentType=file.getContentType();
        if(Constant.CONTENT_TYPE_IMG.contains(contentType))
            return MaterialTypeEnum.Img.getValue();
        if(Constant.CONTENT_TYPE_VIDEO.contains(contentType))
            return MaterialTypeEnum.Video.getValue();
        throw new BusinessException("不支持此文件格式");
    }
}
