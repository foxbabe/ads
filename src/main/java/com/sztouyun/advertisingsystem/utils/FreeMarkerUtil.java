package com.sztouyun.advertisingsystem.utils;

import com.google.common.collect.Maps;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.utils.pdf.PdfResourceTypeEnum;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * FREEMARKER 模板工具类
 */
public class FreeMarkerUtil {

    private static final String WINDOWS_SPLIT = "\\";

    private static final String UTF_8="UTF-8";

    private static Map<String,FileTemplateLoader> fileTemplateLoaderCache=Maps.newConcurrentMap();

    private static  Map<String,Configuration> configurationCache= Maps.newConcurrentMap();

    public static Configuration getConfiguration(String templateFilePath){
        if(null!=configurationCache.get(templateFilePath)){
            return configurationCache.get(templateFilePath);
        }
        Configuration config = new Configuration(Configuration.VERSION_2_3_25);
        config.setDefaultEncoding(UTF_8);
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        config.setNumberFormat("#");
        FileTemplateLoader fileTemplateLoader=null;
        if(null!=fileTemplateLoaderCache.get(templateFilePath)){
            fileTemplateLoader=fileTemplateLoaderCache.get(templateFilePath);
        }
        try {
            fileTemplateLoader=new FileTemplateLoader(new File(templateFilePath));
            fileTemplateLoaderCache.put(templateFilePath,fileTemplateLoader);
        } catch (IOException e) {
            throw new BusinessException("fileTemplateLoader init error!");
        }
        config.setTemplateLoader(fileTemplateLoader);
        configurationCache.put(templateFilePath,config);
        return config;

    }


    /**
     * @description 获取模板
     */
    public static String getFreeMakerTemplate(String templatePath,Object data) {
        File templateFile=new File(templatePath);
        if (org.springframework.util.StringUtils.isEmpty(templatePath)) {
            throw new BusinessException("templatePath can not be empty!");
        }
        try {
            Template template=getConfiguration(templateFile.getParent()).getTemplate(templateFile.getName());
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            writer.flush();
            return  writer.toString();
        } catch (Exception ex) {
            throw new BusinessException("FreeMarkerUtil process fail");
        }
    }





    private static String getTemplatePath(String templatePath) {
        if (org.springframework.util.StringUtils.isEmpty(templatePath)) {
            return "";
        }
        if (templatePath.contains(WINDOWS_SPLIT)) {
            return templatePath.substring(0, templatePath.lastIndexOf(WINDOWS_SPLIT));
        }
        return templatePath.substring(0, templatePath.lastIndexOf("/"));
    }

    private static String getTemplateName(String templatePath) {
        if (org.springframework.util.StringUtils.isEmpty(templatePath)) {
            return "";
        }
        if (templatePath.contains(WINDOWS_SPLIT)) {
            return templatePath.substring(templatePath.lastIndexOf(WINDOWS_SPLIT) + 1);
        }
        return templatePath.substring(templatePath.lastIndexOf("/") + 1);
    }

}