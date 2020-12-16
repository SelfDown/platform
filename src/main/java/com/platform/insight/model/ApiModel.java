package com.platform.insight.model;

public class ApiModel {
    private String ID;
    private String type;
    private String backup;
    private String template;
    private String service;


    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
