package com.platform.insight.factory.database;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface DatabaseOperation {
    /**
     * 根据类型在在final_template 添加url ,即数据访问地址
     * @param final_template
     */
    void changeUrl(JSONObject final_template);

    /**
     * 检查表是否存在
     * @param final_template
     * @return
     */
    boolean checkTableExists(JSONObject final_template);

    /**
     * 获取数据库表字段的详细描述,存入 fields_from_database
     * @param final_template
     *
     */
    void getFieldsDescFromDataBase(JSONObject final_template);

    /**
     * 获取 fields_from_database，与配置文件fields进行比较
     * 新增字段存入newFields
     * 修改字段存入updateFields
     * @param final_template
     */
    void getDifferentField(JSONObject final_template);


    /**
     *
     * 在newFields 获取新增字段
     * 在updateFields 获取修改字段
     * 获取更新表sql
     * @param final_template
     * @return
     */
    List<String> getAlterTableSQL(JSONObject final_template);

    /**
     * sql 构造器，将模板数据拆分成对象，方便后续拼接
     * 将一个查询拆分成子查询，显示字段是一个查询，where 是主查询
     * @return
     * count_id 统计数据字段
     * table 主表
     * where_join_sql_list 主表查询关联sql
     * where_sql_list 主表条件字段
     * show_fields_list 外层显示字段
     * sub_show_fields_list 里层显示字段
     * select_join_sql_list外层关联sql
     * orderBy排序
     * 分页号 page
     * 页面大小 size
     * data 数据
     */
    Map<String,Object> getSQLObject(JSONObject final_template);

    /**
     * 根据sql 对象获取sql 语句
     * @param sqlObj
     * @return
     * selectSQL 查询sql
     * countSQL 统计sql
     * data     数据
     */
    Map<String,Object> buildSQL(Map<String,Object> sqlObj);

    /**
     * 根据sql 对象获取统计sql
     * @param sqlObj
     * @return
     */
    String getCountSQL(Map<String,Object> sqlObj);

    /**
     * 根据sql 对象获取查询sql
     * @param sqlObj
     * @return
     */
   String getSelectSQL(Map<String,Object> sqlObj);

   Map<String,Object> getSQLResult(Map<String,Object> buildSQl);

}
