package com.sztouyun.advertisingsystem.viewmodel.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sztouyun.advertisingsystem.common.ITreeList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
@JsonIgnoreProperties({"children"})
public class DataDictViewModel implements ITreeList<DataDictViewModel> {

    @ApiModelProperty(value = "数据ID")
    private String id;
    @ApiModelProperty(value = "数据名称")
    private String name;
    @ApiModelProperty(value = "数据级别")
    private Integer level;
    @ApiModelProperty(value = "数据所属ID")
    private String parentId;

    private List<DataDictViewModel> dictList;

    @Override
    public void setChildren(List<DataDictViewModel> children) {
        setDictList(children);
    }
}
