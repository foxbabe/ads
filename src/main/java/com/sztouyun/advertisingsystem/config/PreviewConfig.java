package com.sztouyun.advertisingsystem.config;

import ch.qos.logback.core.util.FileUtil;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.contract.ContractTemplateTypeEnum;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * Created by wenfeng on 2017/8/4.
 */
@Component
@ConfigurationProperties(prefix="pdf")
@PropertySource(value = "classpath:application.properties",encoding = "utf-8")
@Data
public class PreviewConfig {
    private String pkgtype;
    private String remotefont;
    private String fontpath;
    private String remotebasefont;
    private String basefontpath;
    private String fmtpath;
    private String remotelogo;
    private String logopath;
    private String tempaltepath;
    private String tempdir;
    private String filedir;
    private String suffix;
    private String defaultSize;
    private Map<String,String> header;
    private Map<String,String> width;
    private Map<String,String> keys;

    public void setKeys(Map<String, String> keys) {
        this.keys = keys;
    }

    public String[] getKey() {
        return key;
    }

    public void setKey(String[] key) {
        this.key = key;
    }

    private String[] key;
    private Map<String,String> title;
    private Integer wide;

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, String> getWidth() {
        return width;
    }

    public void setWidth(Map<String, String> width) {
        this.width = width;
    }

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public Integer getWide() {
        return wide;
    }

    public void setWide(Integer wide) {
        this.wide = wide;
    }

    public Map<String, String> getKeys() {
        return keys;
    }

    public String getFontpath() {
        if(FileUtils.isWindows() && this.pkgtype.equals(Constant.JAR)){
            return  fontpath.replaceAll("/","\\\\");
        }
        return fontpath;
    }

    public void setFontpath(String fontpath) {
        this.fontpath = fontpath;
    }

    public String getTempaltepath() {
        return tempaltepath;
    }

    public void setTempaltepath(String tempaltepath) {
        this.tempaltepath = tempaltepath;
    }

    public String getTempdir() {
        return tempdir;
    }

    public void setTempdir(String tempdir) {
        this.tempdir = tempdir;
    }

    public String getFiledir() {
        return filedir;
    }

    public void setFiledir(String filedir) {
        this.filedir = filedir;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    public String getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(String defaultSize) {
        this.defaultSize = defaultSize;
    }


    public String getTempFilePath(String code){
        StringBuffer path=new StringBuffer(this.tempdir);
        path.append(code);
        path.append(".");
        path.append(this.suffix);
        return path.toString();
    }
    public String getDestFilePath(String code){
        StringBuffer path=new StringBuffer(this.filedir);
        path.append(code);
        path.append(".");
        path.append(this.suffix);
        return path.toString();
    }
    public float[] stringToArray(String src){
        String[] temp=src.split(",");
        int length=temp.length;
        if(length==0)
            return null;
        float[] result=new float[length];
        for(int i=0;i<length;i++){
            result[i]=Float.parseFloat (temp[i]);
        }
        return result;
    }

    public String getPkgtype() {
        return pkgtype;
    }

    public void setPkgtype(String pkgtype) {
        this.pkgtype = pkgtype;
    }

    public String getRemotefont() {
        return remotefont;
    }

    public void setRemotefont(String remotefont) {
        this.remotefont = remotefont;
    }

    public String getFullFontPath(){
        return FileUtils.getJarPath()+this.getFontpath();
    }

    public String getFullFmtPath(String templateId,String ext){
        return String.format(FileUtils.getJarPath()+this.getFmtpath(),templateId,ext);
    }

    public String getFullLogoPath(){
        return FileUtils.getJarPath()+this.getLogopath();
    }

    public String getFullBaseFontPath(){
        return FileUtils.getJarPath()+this.getBasefontpath();
    }
}
