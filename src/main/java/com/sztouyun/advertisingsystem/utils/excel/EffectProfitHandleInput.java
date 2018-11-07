package com.sztouyun.advertisingsystem.utils.excel;

import com.sztouyun.advertisingsystem.utils.dataHandle.DataInput;
import com.sztouyun.advertisingsystem.utils.dataHandle.DataOperationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedInputStream;

/**
 * Created by fengwen on 22/08/2018.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EffectProfitHandleInput implements DataInput {
    private BufferedInputStream in;
    private ExcelConvertConfig convertConfig;

    @Override
    public DataOperationTypeEnum getOperationType() {
        return DataOperationTypeEnum.ExcelToMongo;
    }
}
