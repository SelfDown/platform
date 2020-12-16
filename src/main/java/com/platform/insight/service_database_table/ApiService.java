package com.platform.insight.service_database_table;

import com.alibaba.fastjson.JSONObject;

public interface ApiService {

    boolean checkTableExists(JSONObject final_template);
}
