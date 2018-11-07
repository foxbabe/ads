package com.sztouyun.advertisingsystem.common.datatable;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.ReflectUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.var;
import org.springframework.util.CollectionUtils;

import java.util.List;

@ApiModel
@Data
public class DataTable<TCellType extends ICellType> {
    @ApiModelProperty("所有的数据行")
    List<DataRow> rows;

    public void validate() {
        if(CollectionUtils.isEmpty(rows))
            return;
        Class<TCellType> cellTypeClass =ReflectUtil.getGenericClass(this.getClass(),0);
        if(cellTypeClass != null){
            for (var rowIndex =0;rowIndex<rows.size();rowIndex++){
                var row = rows.get(rowIndex);
                if (row == null || CollectionUtils.isEmpty(row.getCells()))
                    continue;
                for (var cellIndex=0;cellIndex<row.getCells().size();cellIndex++){
                    var cell = row.getCells().get(cellIndex);
                    if(cell ==null)
                        continue;
                    TCellType cellType = EnumUtils.toEnum(cell.getType(),cellTypeClass);
                    if(cellType == null)
                        throw new BusinessException("单元格类型不正确");

                    cell.updateCellData(cellType);
                    cell.setRowIndex(rowIndex);
                    cell.setCellIndex(cellIndex);
                    onValidateCell(rowIndex,cellIndex,cell,cellType);
                }
                row.setRowIndex(rowIndex);
                onValidateRow(rowIndex,row);
            }
        }
    }

    protected void onValidateRow(int rowIndex,DataRow row){

    }

    protected void onValidateCell(int rowIndex,int cellIndex,DataCell cell,TCellType cellType){
        if (cell.getData() == null)
            throw new BusinessException("第"+rowIndex+++"行，第"+cellIndex+++"列,【"+cellType.getDisplayName()+"】值不能为空");
    }
}
