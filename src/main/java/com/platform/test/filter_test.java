package com.platform.test;

import com.platform.insight.model.PlatformTemplate;

public class filter_test {
    public static void main(String[] args) {
        String json = "{\"service_type\":\"service_platform_query_template\",\"type\":\"platform_query_template\",\"service\":\"test_like\",\"login_require\":\"false\",\"action_type\":\"http\",\"preview\":\"false\",\"toTree\":\"\",\"fields\":[{\"field_1\":\"page\",\"field_1_placeholder\":\"字段名称\",\"field_2\":\"页数\",\"field_2_placeholder\":\"描述\",\"field_3\":\"I\",\"field_3_placeholder\":\"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传\",\"field_4\":\"\",\"field_4_placeholder\":\"\",\"field_5\":\"1\",\"field_5_placeholder\":\"默认值\"},{\"field_1\":\"size\",\"field_1_placeholder\":\"字段名称\",\"field_2\":\"数量\",\"field_2_placeholder\":\"描述\",\"field_3\":\"I\",\"field_3_placeholder\":\"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传\",\"field_4\":\"\",\"field_4_placeholder\":\"\",\"field_5\":\"10\",\"field_5_placeholder\":\"默认值\"},{\"field_1\":\"\",\"field_1_placeholder\":\"字段名称\",\"field_2\":\"\",\"field_2_placeholder\":\"描述\",\"field_3\":\"\",\"field_3_placeholder\":\"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传\",\"field_4\":\"\",\"field_4_placeholder\":\"\",\"field_5\":\"\",\"field_5_placeholder\":\"默认值\"}],\"default_fields\":[{\"field_1\":\"delete_flag\",\"field_1_placeholder\":\"字段名称\",\"field_2\":\"是否删除\",\"field_2_placeholder\":\"描述\",\"field_3\":\"S\",\"field_3_placeholder\":\"类型 S、I\",\"field_4\":\"\",\"field_4_placeholder\":\"\",\"field_5\":\"0\",\"field_5_placeholder\":\"默认值\"}],\"data_sql\":\"select * from shoes\",\"count_sql\":\"select count(id) as count from shoes\",\"show_fields\":[{\"field_1\":\"name\",\"field_1_placeholder\":\"字段\",\"field_2\":\"名称\",\"field_2_placeholder\":\"描述\",\"field_3\":\"\",\"field_3_placeholder\":\"\",\"field_4\":\"{}\",\"field_4_placeholder\":\"\",\"field_5\":\"\",\"field_5_placeholder\":\"备用\"}],\"count\":\"count\",\"backup\":\"\"}";
        PlatformTemplate template = PlatformTemplate.parseTemplate(json);
        System.out.println(json);
    }
}
