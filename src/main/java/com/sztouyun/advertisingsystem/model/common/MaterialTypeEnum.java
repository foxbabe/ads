package com.sztouyun.advertisingsystem.model.common;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum MaterialTypeEnum implements EnumMessage<Integer> {
    Img(1,"图片"),
    Text(2,"文本"),
    Video(3,"视频"),
    ImgVideo(4,"图片+视频");


    private Integer value;
    private String displayName;

    MaterialTypeEnum(Integer value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
