package com.sztouyun.advertisingsystem.viewmodel.system;

import com.sztouyun.advertisingsystem.common.ITreeList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@ApiModel
public class TerminalAndAdvertisementPositionInfo implements ITreeList<TerminalAndAdvertisementPositionInfo> {

    @ApiModelProperty(value = "终端类型或广告位id",required = true)
    private String id;
    @ApiModelProperty(value = "广告位集合",hidden = true)
    private List<TerminalAndAdvertisementPositionInfo> children = new ArrayList<>();
    @ApiModelProperty(value = "名称",required = true)
    private String name;
    @ApiModelProperty(value = "是否勾选",required = true)
    private Boolean checked;
    private Integer type;
    private Integer value;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TerminalAndAdvertisementPositionInfo> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<TerminalAndAdvertisementPositionInfo> children) {
        this.children = children;
    }
}
