package com.sztouyun.advertisingsystem.utils.excel;

import com.sztouyun.advertisingsystem.viewmodel.customerStore.BaseImportInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by szty on 2018/5/23.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadInfo extends BaseImportInfo {
    private String filePath;

    public LoadInfo(String filePath,String oid, String bid) {
        super(oid, bid);
        this.filePath = filePath;
    }

    public String getFilePath() {
        return new StringBuffer("'").append(filePath).append("'").toString().replaceAll("\\\\","\\\\\\\\");
    }

    public String getOid() {
        return new StringBuffer("'").append(super.getOid()).append("'").toString();
    }

    public String getBid() {
        return new StringBuffer("'").append(super.getBid()).append("'").toString();
    }
}
