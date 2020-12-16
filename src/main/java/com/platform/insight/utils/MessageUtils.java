package com.platform.insight.utils;

import com.platform.insight.message.MessageReceiver;


import java.util.Map;

public class MessageUtils {
    public static void send(String service_id, Map<String,Object> data){
        MessageReceiver message = SpringUtils.getBean(MessageReceiver.class);
        message.send(service_id,data);
    }
}
