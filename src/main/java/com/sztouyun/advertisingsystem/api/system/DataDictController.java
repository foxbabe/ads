package com.sztouyun.advertisingsystem.api.system;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.system.DataDict;
import com.sztouyun.advertisingsystem.model.system.DataDictTypeEnum;
import com.sztouyun.advertisingsystem.service.system.DataDictService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.system.DataDictViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "字典数据接口")
@RestController
@RequestMapping("/api/dataDict")
public class DataDictController extends BaseApiController {

    @Autowired
    private DataDictService dataDictService;

    @ApiOperation(value = "根据类型查字典", notes = "创建人：毛向军")
    @GetMapping("/{type}/all")
    public InvokeResult<List<DataDictViewModel>> getAllDataDict(@PathVariable("type") Integer type) {
        DataDictTypeEnum dataDictTypeEnum = EnumUtils.toEnum(type, DataDictTypeEnum.class);
        if(dataDictTypeEnum==null)
            return InvokeResult.Fail("字典数据类型错误");
        List<DataDict> list = dataDictService.findAllByType(dataDictTypeEnum);
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToTreeList(list, dict -> ApiBeanUtils.copyProperties(dict, DataDictViewModel.class), Constant.TREE_ROOT_ID));
    }

}
