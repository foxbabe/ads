package com.sztouyun.advertisingsystem.common.file;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class AzureStorageFileService extends BaseService implements IFileService {
    @Value("${azure.storage.connection-string}")
    private String connectionString;
    @Value("${azure.storage.container}")
    private String containerName;
    @Value("${blob.url}")
    private String blobUrl;
    @Value("${blob.url.default}")
    private String defaultBlobUrl;

    @Autowired
    private CloudStorageAccount storageAccount;

    @Override
    public String upload(InputStream inputStream,Long size,String contentType, String fileName,Boolean userOriginalName) {
        try {
            CloudBlockBlob blob = getBlob(getUniqueFileName(fileName,userOriginalName));
            blob.getProperties().setContentType(contentType);
            blob.upload(inputStream,size);
            return blob.getUri().toURL().toString().replace(defaultBlobUrl,blobUrl);
        }catch (Exception ex){
            logger.error("上传文件失败",ex);
        }
        throw new BusinessException("上传文件失败");
    }

    @Override
    public void delete(String id) {
        try {
            CloudBlockBlob blob= getBlob(id);
            blob.deleteIfExists();
            return;
        }catch (Exception ex){
            logger.error("删除文件失败",ex);
        }
        throw new BusinessException("删除文件失败");

    }

    private String getUniqueFileName(String fileName,Boolean userOriginalName){
        if(Boolean.TRUE.equals(userOriginalName))
            return UUIDUtils.generateOrderedUUID()+"/"+fileName;

        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String uniqueFileName = UUIDUtils.generateOrderedUUID()+suffix;
        Date now = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(now);
        return calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH) + 1)+"/"+calendar.get(Calendar.DATE)+"/"+ uniqueFileName;
    }

    private CloudBlockBlob getBlob(String key) throws URISyntaxException, StorageException {
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        ServiceProperties serviceProperties = getCorsProperties();
        blobClient.uploadServiceProperties(serviceProperties);
        CloudBlobContainer container =blobClient.getContainerReference(containerName);
        container.createIfNotExists();
        return container.getBlockBlobReference(key);
    }

    private static ServiceProperties getCorsProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        CorsProperties corsProperties = serviceProperties.getCors();

        CorsRule cr = new CorsRule();

        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("x-ms-*");
        List<String> exposedHeaders = new ArrayList<>();
        exposedHeaders.add("x-ms-*");

        cr.setAllowedHeaders(allowedHeaders);
        cr.setExposedHeaders(exposedHeaders);
        EnumSet<CorsHttpMethods> allowedMethod = EnumSet.of(CorsHttpMethods.GET,CorsHttpMethods.POST);
        cr.setAllowedMethods(allowedMethod);

        List<String> allowedOrigin = new ArrayList<>();
        allowedOrigin.add("*");
        cr.setAllowedOrigins(allowedOrigin);
        cr.setMaxAgeInSeconds(600);

        corsProperties.getCorsRules().add(cr);
        serviceProperties.setCors(corsProperties);
        return serviceProperties;
    }
}
