package com.sztouyun.advertisingsystem.utils.excel;

import com.sztouyun.advertisingsystem.utils.dataHandle.DataOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by szty on 2018/8/23.
 */
@Data
@NoArgsConstructor
public class EffectProfitHandleOutput extends DataOutput {
    private Map<String,Integer> ids=new HashMap<>();

    public EffectProfitHandleOutput(Map<String, Integer> ids) {
        this.ids = ids;
    }

    public EffectProfitHandleOutput(Integer rows, Map<String, Integer> ids) {
        super(rows);
        this.ids = ids;
    }
}
