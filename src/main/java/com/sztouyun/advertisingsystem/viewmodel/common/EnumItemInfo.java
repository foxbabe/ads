package com.sztouyun.advertisingsystem.viewmodel.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnumItemInfo {
    private Integer value;
    private String displayName;
    private Object data;
}
