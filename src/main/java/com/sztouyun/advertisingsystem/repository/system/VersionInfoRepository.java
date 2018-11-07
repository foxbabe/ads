package com.sztouyun.advertisingsystem.repository.system;

import com.sztouyun.advertisingsystem.model.system.VersionInfo;
import com.sztouyun.advertisingsystem.repository.BaseRepository;

public interface VersionInfoRepository extends BaseRepository<VersionInfo> {
    boolean existsByVersionNumber(String versionNumber);
}
