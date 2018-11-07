package com.sztouyun.advertisingsystem.repository.message;

import com.sztouyun.advertisingsystem.model.message.VersionInfoReceiver;
import com.sztouyun.advertisingsystem.repository.BaseRepository;

public interface VersionInfoReceiverRepository extends BaseRepository<VersionInfoReceiver> {
    void deleteAllByVersionInfoId(String versionInfoId);

    VersionInfoReceiver findByVersionInfoIdAndReceiverId(String versionInfoId, String receiverId);
}
