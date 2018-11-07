package com.sztouyun.advertisingsystem.api.file;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.common.file.IFileService;
import com.sztouyun.advertisingsystem.viewmodel.file.FileInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(value = "文件接口")
@RestController
@RequestMapping("/api/file")
public class FileApiController extends BaseApiController {
    @Autowired
    private IFileService fileService;

    @ApiOperation(value="上传文件",notes = "创建人：王英峰")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public InvokeResult<FileInfo> upload(@RequestParam(required = false) Boolean userOriginalName, MultipartFile file){
        if (file == null || file.isEmpty())
            return InvokeResult.Fail("文件不能为空！");

        String fileUrl;
        try {
            fileUrl = fileService.upload(file.getInputStream(),file.getSize(),file.getContentType(),file.getOriginalFilename(),userOriginalName);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("读取上传的文件错误",e);
            return InvokeResult.Fail("无效的文件！");
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setFileUrl(fileUrl);
        return InvokeResult.SuccessResult(fileInfo);
    }

    @ApiOperation(value="删除文件",notes = "创建人：王英峰")
    @RequestMapping(value = "/{fileId}/delete", method = RequestMethod.POST)
    public InvokeResult delete(@PathVariable String fileId) {
        if(StringUtils.isEmpty(fileId))
            return InvokeResult.Fail("fileId不能为空");
        fileService.delete(fileId);
        return InvokeResult.SuccessResult();
    }
}
