package com.sztouyun.advertisingsystem.viewmodel.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class MyPageRequest  extends PageRequest {
    public MyPageRequest(int page, int size) {
        super(page, size);
    }

    public MyPageRequest(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public MyPageRequest(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    @Override
    public Sort getSort() {
        Sort sort = super.getSort();
        if(sort ==null){
            sort =new Sort(Sort.Direction.DESC,"updatedTime");
        }
        return sort;
    }
}
