package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.PartnerProfitConfigInfo;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.PartnerProfitConfigInfoRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by szty on 2018/9/12.
 */
@Mapper
public interface PartnerMapper {
    List<PartnerProfitConfigInfo> getPartnerProfitConfigInfo(PartnerProfitConfigInfoRequest request);

    Long getPartnerProfitConfigInfoCount(PartnerProfitConfigInfoRequest request);

    List<PartnerProfitConfigInfo> getPartnerProfitConfigTime(List<String> partnerIds);
}
