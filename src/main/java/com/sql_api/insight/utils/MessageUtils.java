package com.sql_api.insight.utils;

import com.sql_api.insight.message.MessageReceiver;
import com.sql_api.insight.service_platform_create.ServicePlatformCreate;


import java.util.Map;

public class MessageUtils {
    public static void send(String service_id, Map<String,Object> data){
        MessageReceiver message = SpringUtils.getBean(MessageReceiver.class);
        message.send(service_id,data);
    }
}
