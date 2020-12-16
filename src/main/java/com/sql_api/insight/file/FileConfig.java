package com.sql_api.insight.file;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@Configuration
public class FileConfig {
    //todo 改成配置文件读取目录
    @Bean
    MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String path = "c://temp";
        File path_dir = new File(path);
        if(!path_dir.exists()){
            path_dir.mkdirs();
        }
        factory.setLocation(path);
        return factory.createMultipartConfig();
    }
}
