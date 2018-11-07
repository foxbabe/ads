package com.sztouyun.advertisingsystem.api.system;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.system.VersionInfo;
import com.sztouyun.advertisingsystem.service.system.VersionInfoService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.message.version.CreateVersionInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.message.version.UpdateVersionInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.message.version.VersionInfoViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api("系统版本迭代历程接口")
@RequestMapping("/api/version")
@RestController
public class VersionApiController extends BaseApiController {
    @Autowired
    private VersionInfoService versionInfoService;

    @ApiOperation(value = "新增迭代内容", notes = "创建人: 李海峰")
    @PostMapping("/create")
    public InvokeResult<String> create(@Valid @RequestBody CreateVersionInfoRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        val versionInfo = ApiBeanUtils.copyProperties(request, VersionInfo.class);
        versionInfoService.create(versionInfo);
        return InvokeResult.SuccessResult(versionInfo.getId());
    }

    @ApiOperation(value = "修改迭代内容", notes = "创建人: 李海峰")
    @PostMapping("/update")
    public InvokeResult update(@Valid @RequestBody UpdateVersionInfoRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        versionInfoService.update(ApiBeanUtils.copyProperties(request, VersionInfo.class));
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "删除迭代内容", notes = "创建人: 李海峰")
    @GetMapping("/{id}/delete")
    public InvokeResult delete(@ApiParam(value = "版本信息ID", required = true) @PathVariable String id) {
        versionInfoService.delete(id);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "根据ID获取版本详情", notes = "创建人: 李海峰")
    @GetMapping("/{id}/detail")
    public InvokeResult<VersionInfoViewModel> detail(@ApiParam(value = "版本信息ID", required = true) @PathVariable String id) {
        val versionInfo = versionInfoService.findById(id);
        val viewModel = ApiBeanUtils.copyProperties(versionInfo, VersionInfoViewModel.class);
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "查询所有版本记录", notes = "创建人: 李海峰")
    @GetMapping("/list")
    public InvokeResult<Map<Integer, List<VersionInfoViewModel>>> list() {
        val versionInfos = versionInfoService.findAll();
        val years = versionInfos.stream().map(e -> new LocalDate(e.getUpdatedTime()).getYear()).distinct().collect(Collectors.toList());
        val result = years.stream().collect(Collectors.toMap(year -> year,
                year -> versionInfos.stream().filter(
                        versionInfo -> year == new LocalDate(versionInfo.getUpdatedTime()).getYear()).map(
                        versionInfo -> ApiBeanUtils.copyProperties(versionInfo, VersionInfoViewModel.class)).collect(Collectors.toList())));
        return InvokeResult.SuccessResult(result);
    }

    @ApiOperation(value = "获取最新的版本迭代信息", notes = "创建人: 李海峰")
    @GetMapping("/newestVersionInfo")
    public InvokeResult<VersionInfoViewModel> newestVersionInfo() {
        val versionInfo = versionInfoService.getNewestVersionInfo();
        val viewModel = ApiBeanUtils.copyProperties(versionInfo, VersionInfoViewModel.class);
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "设置迭代版本信息为已读", notes = "创建人: 李海峰")
    @GetMapping("/{id}/readVersionInfo")
    public InvokeResult readVersionInfo(@ApiParam(value = "版本信息ID", required = true) @PathVariable String id) {
        versionInfoService.readVersionInfo(id);
        return InvokeResult.SuccessResult();
    }
}
