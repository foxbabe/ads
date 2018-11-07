package com.sztouyun.advertisingsystem.viewmodel.customerStore;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import lombok.Data;

@Data
public class ExportStoreInfoQueryRequest extends BasePageInfo {
    private String id;// 客户选点记录ID
}
