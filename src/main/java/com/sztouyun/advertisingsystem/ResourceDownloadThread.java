package com.sztouyun.advertisingsystem;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.config.PreviewConfig;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import com.sztouyun.advertisingsystem.utils.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by wenfeng on 2017/8/28.
 */
@Component
public class ResourceDownloadThread implements  Runnable {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PreviewConfig previewConfig;
    private static volatile Boolean STOP=Boolean.FALSE;
    private Integer retryTimes=0;
    public ResourceDownloadThread(PreviewConfig previewConfig){
        this.previewConfig=previewConfig;
    }

    @Override
    public void run() {
        while(!STOP && retryTimes<3){
            initTempDirAndResource(previewConfig);
            ThreadUtil.sleep(2000);
            retryTimes++;
            logger.info("try:"+retryTimes);
        }
    }

    /**
     * 当以JAR运行时，在JAR所在目录创建临时目录，并且从七牛下载字体文件
     * @param previewConfig
     */
    private   void initTempDirAndResource(PreviewConfig previewConfig){
        if(previewConfig.getPkgtype().equals(Constant.JAR)){
            File tempFile=new File(FileUtils.getJarPath()+previewConfig.getTempdir());
            FileUtils.mkDir(tempFile);
            FileUtils.mkDir(new File(FileUtils.getJarPath()+previewConfig.getFiledir()));
            if(tempFile.exists()){
                downloadResource(previewConfig.getRemotefont(),previewConfig.getFullFontPath());
                downloadResource(previewConfig.getRemotelogo(),previewConfig.getFullLogoPath());
                downloadResource(previewConfig.getRemotebasefont(),previewConfig.getFullBaseFontPath());
            }
            if(new File(previewConfig.getFullBaseFontPath()).exists() &&
                    new File(previewConfig.getFullLogoPath()).exists()
                    && new File(previewConfig.getFullBaseFontPath()).exists()){
                STOP=Boolean.TRUE;
            }
        }
    }

    public void downloadResource(String remoteResourcePath,String localPath){
        downloadResource(remoteResourcePath,localPath,Boolean.FALSE);
    }

    public void downloadResource(String remoteResourcePath, String localPath, Boolean override){
        try {
            boolean needDownload=false;
            if(!new File(localPath).exists()){
                needDownload=true;
            }
            if(Boolean.TRUE.equals(override)){
                needDownload=true;
                FileUtils.clearFile(localPath);

            }
            if(needDownload){
                FileUtils.downloadFile(remoteResourcePath,localPath);
                ThreadUtil.sleep(100);
            }
        }catch (Exception e) {
            logger.error("下载资源文件失败:"+localPath,e);
            throw new BusinessException("下载资源文件失败");
        }
    }
}
