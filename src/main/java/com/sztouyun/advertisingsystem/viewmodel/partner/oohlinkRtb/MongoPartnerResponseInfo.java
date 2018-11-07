package com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MongoPartnerResponseInfo {
    private String id;

    private String responseInfo;

    public MongoPartnerResponseInfo(String id, String responseInfo) {
        this.id = id;
        this.responseInfo = responseInfo;
    }
}
