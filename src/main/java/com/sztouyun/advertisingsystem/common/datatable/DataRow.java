package com.sztouyun.advertisingsystem.common.datatable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class DataRow {
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Integer rowIndex;

    @NonNull
    @ApiModelProperty("行内的所有的单元格")
    private List<DataCell> cells;
}
