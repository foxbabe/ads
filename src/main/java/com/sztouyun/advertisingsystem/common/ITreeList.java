package com.sztouyun.advertisingsystem.common;

import java.util.List;

public interface ITreeList<T extends ITreeList> {
    void  setChildren(List<T> children);
}
