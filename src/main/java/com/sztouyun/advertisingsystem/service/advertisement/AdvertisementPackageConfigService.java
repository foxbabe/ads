package com.sztouyun.advertisingsystem.service.advertisement;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.PackagePeroidEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPackageConfig;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementPackageConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * Created by wenfeng on 2017/8/4.
 */
@Service
public class AdvertisementPackageConfigService extends BaseService {

    @Autowired
    private AdvertisementPackageConfigRepository advertisementPackageConfigRepository;

    @Transactional
    public void createAdvertisementPackageConfig(AdvertisementPackageConfig advertisementPackageConfig){
        if(advertisementPackageConfigRepository.existsByPackageNameAndActived(advertisementPackageConfig.getPackageName(),true))
            throw new BusinessException("“套餐名称不能重复，无法保存");
        if(!EnumUtils.isValidValue(advertisementPackageConfig.getPeriod(),PackagePeroidEnum.class))
            throw new BusinessException("套餐周期不存在");
        if(advertisementPackageConfig.getTotalAmount()==0)
            throw new BusinessException("必须配置有效的屏位数量");
        var loginUser=getUser();
        advertisementPackageConfig.setCreatorId(loginUser.getId());
        advertisementPackageConfig.setUpdaterId(loginUser.getId());
        create(advertisementPackageConfig);
    }

    @Transactional
    public void deleteAdvertisementPackageConfig(String id){
        boolean existsFlag=advertisementPackageConfigRepository.exists(id);
        if(!existsFlag)
            throw new BusinessException("套餐配置名称不存在！");
        //存在广告业务关联，则不允许删除
        advertisementPackageConfigRepository.delete(id);
    }

    @Transactional
    public void updateAdvertisementPackageConfig(AdvertisementPackageConfig advertisementPackageConfig){
        AdvertisementPackageConfig oldAdvertisementPackageConfig=advertisementPackageConfigRepository.findOne(advertisementPackageConfig.getId());
        if(null==oldAdvertisementPackageConfig)
            throw new BusinessException("套餐配置不存在！");
        if(!oldAdvertisementPackageConfig.getActived())
            throw new BusinessException("套餐配置已经过期，不允许修改！");
        if(advertisementPackageConfigRepository.existsByIdNotAndPackageNameAndActived(advertisementPackageConfig.getId(),advertisementPackageConfig.getPackageName(),true) )
            throw new BusinessException("“套餐名称不能重复，无法保存");
        if(!EnumUtils.isValidValue(advertisementPackageConfig.getPeriod(),PackagePeroidEnum.class))
            throw new BusinessException("请选择有效的套餐周期");
        if(advertisementPackageConfig.getTotalAmount()==0)
            throw new BusinessException("必须配置有效的屏位数量");
        var loginUser=getUser();
        advertisementPackageConfig.setUpdatedTime(new Date());
        advertisementPackageConfig.setUpdaterId(loginUser.getId());
        advertisementPackageConfigRepository.save(advertisementPackageConfig);
    }

    public AdvertisementPackageConfig getAdvertisementPackageConfig(String id){
        AdvertisementPackageConfig advertisementPackageConfig=advertisementPackageConfigRepository.findOne(id);
        if(null==advertisementPackageConfig)
            throw new BusinessException("套餐配置不存在！");
        return advertisementPackageConfig;
    }

    public Page<AdvertisementPackageConfig> queryAdvertisementPackageConfigList(Boolean isActive,String  packageName, Pageable pageable){
        Page<AdvertisementPackageConfig> pages=advertisementPackageConfigRepository.findAllByPackageNameContainingAndActived(packageName,isActive, pageable );
        return pages;
    }

    private void create(AdvertisementPackageConfig advertisementPackageConfig){
        AdvertisementPackageConfig newConfig=new AdvertisementPackageConfig();
        newConfig.setPackageName(advertisementPackageConfig.getPackageName());
        newConfig.setAmontOfTypeA(advertisementPackageConfig.getAmontOfTypeA());
        newConfig.setAmontOfTypeB(advertisementPackageConfig.getAmontOfTypeB());
        newConfig.setAmontOfTypeC(advertisementPackageConfig.getAmontOfTypeC());
        newConfig.setAmontOfTypeD(advertisementPackageConfig.getAmontOfTypeD());
        newConfig.setTotalCost(advertisementPackageConfig.getTotalCost());
        newConfig.setPeriod(advertisementPackageConfig.getPeriod());
        newConfig.setCreatorId(advertisementPackageConfig.getCreatorId());
        newConfig.setUpdaterId(advertisementPackageConfig.getUpdaterId());
        newConfig.setUpdatedTime(DateUtils.getMaxDate());
        advertisementPackageConfigRepository.save(newConfig);
    }

}
