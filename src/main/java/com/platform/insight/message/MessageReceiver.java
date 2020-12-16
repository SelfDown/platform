package com.platform.insight.message;

import com.platform.insight.data_source.DatabaseConnections;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


@Component
public class MessageReceiver extends Thread{
    public static final String OPERATION_ADD = "ADD";
    private static Queue<Map<String,Object>> queue = new ConcurrentLinkedQueue();
    MessageReceiver(){
        this.start();


    }


    public void send(String service_id,Map<String,Object> data){
        Map<String,Object> map = new HashMap<>();
        map.put("operation",service_id);
        map.put("data",data);
        queue.add(map);
    }

    @Override
    public void run() {
        while (true){
            while (queue.isEmpty()){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(!queue.isEmpty()){
                Map<String, Object> data = queue.poll();
                String operation = (String) data.get("operation");
                Map<String,Object> map = (Map<String, Object>) data.get("data");
                switch (operation){
                    case MessageReceiver.OPERATION_ADD:
                        JdbcTemplate writeTemplate = DatabaseConnections.getWriteTemplate();
                        String sql = (String) map.get("sql");
                        Object[] sql_data = (Object[]) map.get("data");
                        writeTemplate.update(sql,sql_data);
                        break;
                }

            }

        }
    }
}
