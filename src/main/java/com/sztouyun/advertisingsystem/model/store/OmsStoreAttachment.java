package com.sztouyun.advertisingsystem.model.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/3/21.
 */
@Data
public class OmsStoreAttachment {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Url")
    private String url;
    /**
     * 图片类型 4:门店照，5：店内照，7：收银台照
     */
    @JsonProperty("PictureType")
    private Integer PictureType;
    @JsonProperty("CreateTime")
    private Date createdTime;
    @JsonProperty("UpdateTime")
    private Date updatedTime;
}
