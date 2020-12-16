package com.platform.insight.utils;


import com.platform.insight.service_platform_query.ServicePlatformQuery;
import com.platform.insight.model.FileModel;
import com.platform.insight.service.Result;
import com.platform.insight.service_platform_create.ServicePlatformCreate;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileInfoUtils {
    public static Map<String,Object> fileServiceInfoSave(String service, FileModel file){
        Result result_service = SpringUtils.getBean(ServicePlatformCreate.ID);
        Map<String,Object> service_operation_add = new HashMap<>();
        service_operation_add.put("service",service);
        Map<String,Object> fields = new HashMap<>();
        fields.put("code",file.getCode());
        fields.put("file_name",file.getFile_name());
        fields.put("file_index",file.getFile_index());
        fields.put("file_domain_path",file.getFile_domain_path());
        fields.put("file_source_path",file.getFile_source_path());
        service_operation_add.put("fields",fields);
        try{
            Map<String, Object> result = result_service.result(service_operation_add);
            return result;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static HttpServletResponse download(Map<String,Object> sourcePath, HttpServletResponse response) throws FileNotFoundException {
        try {
            String path = (String) sourcePath.get("file_source_path");
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = (String) sourcePath.get("file_name");
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    public static Map<String,Object> getFileSourcePath(String id){
        Result result_service = SpringUtils.getBean(ServicePlatformQuery.ID);
        Map<String,Object> service_operation_add = new HashMap<>();
        service_operation_add.put("service","device_file_list_query_by_id");
        Map<String,Object> fields = new HashMap<>();
        fields.put("id",id);
        service_operation_add.put("search_fields",fields);
        try{
            Map<String, Object> result = result_service.result(service_operation_add);
            if (result.containsKey("success") && (boolean)result.get("success")){

                long count = (long) result.get("count");
                if(count<=0){
                    return  null;
                }
                List<Map<String,Object>> data = (List<Map<String, Object>>) result.get("data");
                String path = (String) data.get(0).get("file_source_path");
                File file = new File(path);
                if(file.exists()){
                   return data.get(0);
                }else{
                    return null;
                }
            }else {
                return null;
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
