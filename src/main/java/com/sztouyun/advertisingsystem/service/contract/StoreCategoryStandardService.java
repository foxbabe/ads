package com.sztouyun.advertisingsystem.service.contract;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.QStoreCategoryStandard;
import com.sztouyun.advertisingsystem.model.contract.StoreCategoryStandard;
import com.sztouyun.advertisingsystem.repository.contract.StoreCategoryStandardRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class StoreCategoryStandardService extends BaseService {
    @Autowired
    private StoreCategoryStandardRepository storeCategoryStandardRepository;

    private final QStoreCategoryStandard qStoreCategoryStandard= QStoreCategoryStandard.storeCategoryStandard;

    @Transactional
    public void createStoreCategoryStandard(StoreCategoryStandard storeCategoryStandard) {
        if(null == storeCategoryStandard)
            throw new BusinessException("门店类别标准创建失败！");
        validateStoreCategoryStandard(storeCategoryStandard);
        storeCategoryStandardRepository.save(storeCategoryStandard);
    }

    @Transactional
    public void updateStoreCategoryStandard(StoreCategoryStandard storeCategoryStandard) {
        boolean isExist = storeCategoryStandardRepository.exists(storeCategoryStandard.getId());
        if(!isExist)
            throw new BusinessException("该门店类别标准不存在");
        validateStoreCategoryStandard(storeCategoryStandard);
        storeCategoryStandardRepository.save(storeCategoryStandard);
    }

    @Transactional
    public void deleteStoreCategoryStandard(String id) {
        boolean isExist=storeCategoryStandardRepository.exists(id);
        if(!isExist)
            throw new BusinessException("门店类别标准不存在");
        storeCategoryStandardRepository.delete(id);
    }

    public Page<StoreCategoryStandard> queryStoreCategoryStandardList(Pageable pageable) {
        Page<StoreCategoryStandard> pages= storeCategoryStandardRepository.findAll(pageable);
        return  pages;
    }

    public StoreCategoryStandard getStoreCategoryStandard(String id) {
        StoreCategoryStandard storeCategoryStandard = storeCategoryStandardRepository.findOne(id);
        if(null == storeCategoryStandard)
            throw new BusinessException("查询不到相关信息");
        return storeCategoryStandard;
    }

    public void validateStoreCategoryStandard(StoreCategoryStandard storeCategoryStandard){
        if(storeCategoryStandardRepository.exists(
                qStoreCategoryStandard.storeCategoryStandardName.eq(storeCategoryStandard.getStoreCategoryStandardName()
                ).and(qStoreCategoryStandard.id.eq(storeCategoryStandard.getId()).not())))
            throw new BusinessException("该门店类别标准名称已经存在");
    }

}
