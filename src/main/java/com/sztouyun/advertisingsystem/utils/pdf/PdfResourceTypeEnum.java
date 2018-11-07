package com.sztouyun.advertisingsystem.utils.pdf;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * Created by szty on 2018/6/26.
 */
public enum PdfResourceTypeEnum implements EnumMessage<Integer> {
    Font(1,"contract/simsun.ttf"),
    Logo(2,"contract/logo.png");
    private Integer value;
    private String displayName;
    private String resourcePath;
    private String resourcePathDir;
    PdfResourceTypeEnum(Integer value, String displayName) {
        this.value = value;
        this.displayName = displayName;

    }

    private void initPath(){
        File file=new File(FileUtils.getJarPath()+this.displayName);
        this.resourcePath=file.getAbsolutePath();
        this.resourcePathDir=file.getParent();
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getResourcePath(){
        if(StringUtils.isEmpty(resourcePath)){
            initPath();
        }
        return resourcePath;
    }

    public String getResourcePathDir(){
        if(StringUtils.isEmpty(resourcePathDir)){
            initPath();
        }
        return resourcePathDir;
    }
}
