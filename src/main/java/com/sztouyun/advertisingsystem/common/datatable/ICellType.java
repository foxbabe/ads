package com.sztouyun.advertisingsystem.common.datatable;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public interface ICellType extends EnumMessage<Integer> {
    Class<?> getDataClass();
}
