package com.sql_api.insight.data_source;

import com.sql_api.insight.utils.SpringUtils;
import org.springframework.jdbc.core.JdbcTemplate;



public class DatabaseConnections {

    static JdbcTemplate readTemplate = null;
    static JdbcTemplate writeTemplate = null;
    public static JdbcTemplate getReadTemplate(){
        if(readTemplate==null){
             readTemplate = SpringUtils.getBean(DruidConfiguration.READ_TEMPLATE);
        }
        return readTemplate;
    }
    public static JdbcTemplate getWriteTemplate(){
        if(writeTemplate==null){
            writeTemplate= SpringUtils.getBean(DruidConfiguration.WRITE_TEMPLATE);
        }
        return writeTemplate;
    }
}
