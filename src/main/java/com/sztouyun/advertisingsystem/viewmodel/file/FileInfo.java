package com.sztouyun.advertisingsystem.viewmodel.file;

import io.swagger.annotations.ApiModelProperty;

public class FileInfo {
    @ApiModelProperty(value = "文件名")
    private String fileName;
    @ApiModelProperty(value = "文件ID")
    private String fileId;
    @ApiModelProperty(value = "文件地址")
    private String fileUrl;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
