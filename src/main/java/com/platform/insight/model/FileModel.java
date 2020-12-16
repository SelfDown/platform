package com.platform.insight.model;

public class FileModel {

    //id
    private String id;
    // 编码
    private String code;
    //文件名
    private String file_name;
    //文件序号
    private String file_index;
    //文件实际路径
    private String file_source_path;
    //文件访问路径
    private String file_domain_path;

    //信息
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_index() {
        return file_index;
    }

    public void setFile_index(String file_index) {
        this.file_index = file_index;
    }

    public String getFile_source_path() {
        return file_source_path;
    }

    public void setFile_source_path(String file_source_path) {
        this.file_source_path = file_source_path;
    }

    public String getFile_domain_path() {
        return file_domain_path;
    }

    public void setFile_domain_path(String file_domain_path) {
        this.file_domain_path = file_domain_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
