package com.sql_api.insight.service;





import com.sql_api.insight.model.ApiModel;
import com.sql_api.insight.utils.ApiCacheUtils;
import com.sql_api.insight.utils.SqliteUtils;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;



@Service(ApiService.ID)
public class ApiServiceImpl implements ApiService{


    public static final String ID = "ApiService";
    public boolean saveApi(ApiModel api) {
        Connection conn = SqliteUtils.getConn();
        try {
            Statement statement = conn.createStatement();
            String format = String.format("insert into collection_list(id,type,service,template,backup) values('%s','%s','%s','%s','%s')",System.currentTimeMillis()+"",api.getType(),api.getService(),api.getTemplate(),api.getBackup() );
            boolean resultSet = statement.execute(format);
            statement.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ApiModel queryByService(String service) {
        Object apiTemplateMap = ApiCacheUtils.getApiTemplateMap(service);
        if(apiTemplateMap != null){
            return (ApiModel) apiTemplateMap;
        }
        Connection conn = SqliteUtils.getConn();

        try {
            Statement statement = conn.createStatement();
            PreparedStatement preparedStatement = conn.prepareStatement("select * from collection_list where service = ? order by ID  limit 1 ");
            preparedStatement.setString(1,service);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()== false){
                return null;
            }else {
                ApiModel apiModel = new ApiModel();
                apiModel.setService(resultSet.getString("service"));
                apiModel.setType(resultSet.getString("type"));
                apiModel.setBackup(resultSet.getString("backup"));
                apiModel.setTemplate(resultSet.getString("template"));
                resultSet.close();
                statement.close();
                ApiCacheUtils.setApiTemplateMap(service,apiModel);
                return apiModel;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ApiModel> queryByType(String type) {
        Connection conn = SqliteUtils.getConn();
        List<ApiModel> list  = new ArrayList<ApiModel>();
        try {
            Statement statement = conn.createStatement();
            String format = String.format("select * from collection_list where type = '%s' order by ID desc ", type);
            ResultSet resultSet = statement.executeQuery(format);
            if(resultSet.next()== false){
                return list;
            }else {
                do{
                    ApiModel apiModel = new ApiModel();
                    apiModel.setService(resultSet.getString("service"));
                    apiModel.setType(resultSet.getString("type"));
                    apiModel.setBackup(resultSet.getString("backup"));
                    apiModel.setTemplate(resultSet.getString("template"));
                    list.add(apiModel);
                }while(resultSet.next());

                resultSet.close();
                statement.close();
                return list;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteApi(String service) {
        Connection conn = SqliteUtils.getConn();
        try {
            Statement statement = conn.createStatement();
            String format = String.format("delete from collection_list where service = '%s'",service);
            boolean resultSet = statement.execute(format);
            statement.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateApi(ApiModel api) {
        Connection conn = SqliteUtils.getConn();
        try {
            Statement statement = conn.createStatement();
            String format = String.format("update collection_list set backup='%s',template= '%s' where service = '%s' ",api.getBackup() ,api.getTemplate(),api.getService());
            boolean resultSet = statement.execute(format);
            statement.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }


}
