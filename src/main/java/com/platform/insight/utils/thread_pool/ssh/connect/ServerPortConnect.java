package com.platform.insight.utils.thread_pool.ssh.connect;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ServerPortConnect  implements Callable<Map<String,Object>>{
    Map<String,Object> connect = null;
    private String ip;
    private int port;
    private int timeout;
    public ServerPortConnect(Map<String, Object> connect) {
                this.connect = connect;
        this.ip = (String) connect.get("ip");
        this.port= (int) connect.get("port");
        this.timeout= (int) connect.get("timeout");

    }


    public Map<String, Object> connect(String ip, int port,int timeout) {
        long startTime = System.currentTimeMillis();    //获取开始时间的时间戳
        Boolean connect = false;
        Socket socket = new Socket();
        String msg = null;
        try {
            socket.connect(new InetSocketAddress(ip, port),timeout);
            connect = true;
            msg = "连接成功";
        } catch (IOException e) {
            msg = e.getMessage();

            connect = false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
        long endTime = System.currentTimeMillis();
        Map<String,Object> map = new HashMap<>();
        map.put("ip",ip);
        map.put("port",port);
        map.put("connect",connect);
        map.put("spend",(endTime - startTime) + "ms");
        map.put("msg",msg);
        return map;
    }
    @Override
    public Map<String, Object> call() throws Exception {

        Map<String, Object> connect = connect(this.ip, this.port, this.timeout);
        return connect;
    }
}
