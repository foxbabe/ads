package com.sztouyun.advertisingsystem.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VersionInfoReceiverMapper {
    void insertBatchBy(String versionInfoId);
}
