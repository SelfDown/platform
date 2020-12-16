package com.sql_api.insight.service_file_upload_save;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sql_api.insight.model.ApiModel;
import com.sql_api.insight.model.FileModel;
import com.sql_api.insight.service.ResultBaseService;
import com.sql_api.insight.utils.DateTimeUtil;
import com.sql_api.insight.utils.FileInfoUtils;
import com.sql_api.insight.utils.StringJoinUtils;
import com.sql_api.insight.utils.UserManager;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;



@Service(ServiceFileUploadSave.ID)
public class ServiceFileUploadSave extends ResultBaseService implements ApiService {
    public static final String ID ="service_file_upload_save";
    @Override
    public Map<String, Object> result(Map<String, Object> actual) {
        Map<String,Object> test = new HashMap<>();
        String service = (String) actual.get("service");
        ApiModel apiModel = getApiModel(service);
        String template_final = apiModel.getTemplate();
        JSONObject final_template = JSON.parseObject(template_final);

        List<MultipartFile> fileList = getFileList();
        List<FileModel> successList = new ArrayList<>();
        List<FileModel> errorList = new ArrayList<>();

        String accept = final_template.getString("accept");

        int bytes_size = -1;
        int max_size = -1;
        try{
             max_size = final_template.getInteger("max_size");
            bytes_size = max_size*1024*1024;
        }catch (Exception ex){

        }

        String[] indexArr = accept.split(",");
        List<String> extension_list = Arrays.asList(indexArr);
        for(int i=0;i<fileList.size();i++){


            MultipartFile file = fileList.get(i);
            String originalFilename = file.getOriginalFilename();
            String file_extension = null;
            FileModel model = new FileModel();
            model.setFile_name(originalFilename);

            //检查文件后缀
            if(originalFilename.indexOf(".")!=-1){
                file_extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                if(!extension_list.contains(file_extension)){
                    model.setMsg("上传文件 "+originalFilename+" 有误 。接口不支持 "+file_extension);
                    errorList.add(model);
                    continue;
                }
            }else{
                model.setMsg(originalFilename+" 文件必须有后缀名");
                errorList.add(model);
                continue;
            }
            //检查文件大小
            if(bytes_size!=-1){
                long size = file.getSize();
                if(bytes_size<size){
                    model.setMsg("上传文件 "+originalFilename+"有误 。文件不能大于  "+max_size+" M");
                    errorList.add(model);
                    continue;
                }
            }

            String  file_name = UUID.randomUUID()+file_extension;
            String root = final_template.getString("root");
            final String sourcePath = getSourcePath(final_template);
            String source_path = root+sourcePath+file_name;
            model.setFile_source_path(source_path);
            model.setFile_domain_path(getDomainPath(final_template)+sourcePath+file_name);
            model.setCode("code");
            String process_data_service = final_template.getString("process_data_service");
            try {
                Map<String, Object> file_map = FileInfoUtils.fileServiceInfoSave(process_data_service, model);
                if(file_map.containsKey("success") && (boolean)file_map.get("success")){
                    model.setMsg("保存成功");
                    model.setFile_source_path(null);
                    model.setId(file_map.get("primary_key")+"");
                    successList.add(model);
                }else {
                    if(file_map.containsKey("msg")){
                        model.setMsg(file_map.get("msg")+"");
                    }
                    errorList.add(model);
                    continue;
                }
                File file1 = new File(source_path);
                if(!file1.getParentFile().exists()){
                    file1.getParentFile().mkdirs();
                }
                file.transferTo(file1);

            } catch (Exception e) {

                e.printStackTrace();
            }

        }
        Map<String,Object> data = new HashMap<>();
        data.put("success",successList);
        data.put("error",errorList);
        Map<String,Object> map= new HashMap<>();
        map.put("data",data);
        map.put("success",true);
        map.put("msg","操作成功");
        return map;
    }


    private String getSourcePath(JSONObject template){

        List<String> paths = new ArrayList<>();

        JSONArray path = template.getJSONArray("path");

        for(int i =0;i<path.size();i++){
            paths.add(getRulePath(path.getJSONObject(i)));
        }


        return StringJoinUtils.join(paths,"/")+"/";

    }

    private String getDomainPath(JSONObject template){
        String domain_path = template.getString("domain_path");
        return domain_path;

    }
    private String getRulePath(JSONObject path){
        String field_3 = path.getString("field_3");
        String rulePath="";
        switch (field_3){
            case "day":
                rulePath = DateTimeUtil.getDate();
                break;
            case "count":
                int file_num = path.getInteger("field_5");
                rulePath= getCount()/file_num+"";
                break;
            case "user_id_4":
                String currentPkUser = UserManager.getCurrentPkUser();
                rulePath = UserManager.getCurrentPkUser().substring(currentPkUser.length()-4);
                break;
            case "S":
                rulePath = path.getString("field_1");
                break;
        }
        return rulePath;
    };
    @Override
    public Map<String, Object> checkAccept(JSONObject template) {
        List<MultipartFile> fileList = getFileList();
        String accept = template.getString("accept");
        //todo max_size 不存在的情况
        Integer max_size = template.getInteger("max_size");
        int bytes_size = max_size*1024*1024;
        String[] indexArr = accept.split(",");
        List<String> indexList = Arrays.asList(indexArr);
        for(int i =0;i<fileList.size();i++){
            MultipartFile file = fileList.get(i);
            long size = file.getSize();

            String originalFilename = file.getOriginalFilename();
            if(bytes_size<size){
                return getResult(false,"上传文件 "+originalFilename+"有误 。文件不能大于  "+max_size+" M");
            }
            if(originalFilename.indexOf(".")!=-1){
                String file_index = originalFilename.substring(originalFilename.lastIndexOf("."));
                if(!indexList.contains(file_index)){
                    return getResult(false,"上传文件 "+originalFilename+" 有误 。接口不支持 "+file_index);
                }
            }else{
                return getResult(false,originalFilename+" 必须有后缀名");
            }

        }


        return getResult(true,"类型正确");
    }
}
