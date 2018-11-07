package com.sztouyun.advertisingsystem.service.system;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.VersionInfoReceiverMapper;
import com.sztouyun.advertisingsystem.model.system.VersionInfo;
import com.sztouyun.advertisingsystem.repository.message.VersionInfoReceiverRepository;
import com.sztouyun.advertisingsystem.repository.system.VersionInfoRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class VersionInfoService extends BaseService {
    @Autowired
    private VersionInfoRepository versionInfoRepository;
    @Autowired
    private VersionInfoReceiverRepository versionInfoReceiverRepository;

    @Autowired
    private VersionInfoReceiverMapper versionInfoReceiverMapper;

    @Transactional
    public void create(VersionInfo versionInfo) {
        if (versionInfoRepository.existsByVersionNumber(versionInfo.getVersionNumber()))
            throw new BusinessException("版本号不能重复");

        versionInfoRepository.save(versionInfo);
        if (versionInfo.getIsTip()) {
            versionInfoReceiverMapper.insertBatchBy(versionInfo.getId());
        }
    }

    @Transactional
    public void update(VersionInfo versionInfo) {
        checkExist(versionInfo.getId());
        versionInfoReceiverRepository.deleteAllByVersionInfoId(versionInfo.getId());
        create(versionInfo);
    }

    @Transactional
    public void delete(String versionInfoId) {
        checkExist(versionInfoId);
        versionInfoRepository.delete(versionInfoId);
        versionInfoReceiverRepository.deleteAllByVersionInfoId(versionInfoId);
    }

    private void checkExist(String versionInfoId) {
        if (Objects.isNull(versionInfoRepository.findOne(versionInfoId)))
            throw new BusinessException("版本记录不存在");
    }

    public VersionInfo findById(String versionInfoId) {
        VersionInfo versionInfo = versionInfoRepository.findOne(versionInfoId);
        if (Objects.isNull(versionInfo))
            throw new BusinessException("版本记录不存在");
        return versionInfo;
    }

    public void readVersionInfo(String versionInfoId) {
        checkExist(versionInfoId);
        val versionInfoReceiver = versionInfoReceiverRepository.findByVersionInfoIdAndReceiverId(versionInfoId, getUser().getId());
        if (Objects.isNull(versionInfoReceiver))
            throw new BusinessException("该版本未设置提示或用户在该版本发版时被禁用或该用户是广告客户");
        versionInfoReceiver.setHasRead(true);
        versionInfoReceiverRepository.save(versionInfoReceiver);
    }

    public List<VersionInfo> findAll() {
        return versionInfoRepository.findAll(new Sort(Sort.Direction.DESC, "createdTime"));
    }

    public VersionInfo getNewestVersionInfo() {
        val page = versionInfoRepository.findAll(new MyPageRequest(0, 1));
        val versionInfos = page.getContent();
        if (versionInfos.isEmpty())
            throw new BusinessException("当前没有版本记录");
        val versionInfo = versionInfos.get(0);
        if (!versionInfo.getIsTip())
            throw new BusinessException("最新的版本迭代信息配置为不提示");
        val versionInfoReceiver = versionInfoReceiverRepository.findByVersionInfoIdAndReceiverId(versionInfo.getId(), getUser().getId());
        if (Objects.isNull(versionInfoReceiver))
            throw new BusinessException("当前登录用户没有收到提示");
        if (versionInfoReceiver.getHasRead())
            throw new BusinessException("最新的版本迭代信息已读");
        return versionInfo;
    }
}
