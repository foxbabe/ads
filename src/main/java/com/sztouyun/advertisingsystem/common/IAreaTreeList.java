package com.sztouyun.advertisingsystem.common;

import java.util.List;

public interface IAreaTreeList<T extends IAreaTreeList> extends ITreeList<T> {
    String getCode();

    List<T> getChildren();
}
