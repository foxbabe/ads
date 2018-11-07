package com.sztouyun.advertisingsystem.common.file;


import java.io.InputStream;

public interface IFileService {
    String upload(InputStream inputStream ,Long size ,String contentType ,String fileName,Boolean userOriginalName);

    default String upload(InputStream inputStream ,Long size ,String contentType ,String fileName){
        return upload(inputStream ,size ,contentType ,fileName,false);
    }

    void delete(String fileName);
}
