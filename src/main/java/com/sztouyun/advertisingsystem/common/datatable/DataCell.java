package com.sztouyun.advertisingsystem.common.datatable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.utils.ObjectMapperUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class DataCell {
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Integer rowIndex;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Integer cellIndex;
    @ApiModelProperty("单元格类型")
    private Integer type;
    @ApiModelProperty("单元格数据")
    private Object data;

    public <T> T getData(){
        return (T)data;
    }

    public double findDoubleData(){
        if(data instanceof  Number)
            return Double.valueOf(data.toString());
        return 0D;
    }

    public void updateCellData(ICellType cellType){
        if(cellType ==null || !type.equals(cellType.getValue()))
            return;
        this.data = getCellData(cellType,this.data);
    }

    private Object getCellData(ICellType cellType, Object data) {
        if(data ==null)
            return null;
        if(type ==null)
            return data;
        if(cellType ==null)
            return data;
        if(cellType.getDataClass().isAssignableFrom(data.getClass()))
            return data;
        return ObjectMapperUtils.jsonToObject(ObjectMapperUtils.toJsonString(data),cellType.getDataClass());
    }
}
