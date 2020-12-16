package com.platform.insight.ssh.service_server_scan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.model.ApiModel;
import com.platform.insight.service.ResultBaseService;
import com.platform.insight.utils.EnvUtils;
import com.platform.insight.utils.thread_pool.Executor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.platform.insight.utils.IPUtils.parseIpMaskRange;
import static com.platform.insight.utils.IPUtils.parseIpRange;


@Service(ServiceServerScan.ID)
public class ServiceServerScan extends ResultBaseService implements ApiService {
    public static final String ID ="service_server_scan";
    @Override
    public Map<String, Object> result(Map<String, Object> actual) {
        Map<String,Object> test = new HashMap<>();
        String service = (String) actual.get("service");
        ApiModel apiModel = getApiModel(service);
        String template_final = apiModel.getTemplate();
        JSONObject final_template = JSON.parseObject(template_final);
        Map<String, Object> map = checkFields((Map<String, Object>) actual.get("fields"), final_template.getJSONArray("fields"),final_template);
        if(actual.get("fields")==null){
            actual.put("fields",new HashMap<>());
        }
        //检查状态是否成功
        String msg = null;
        if(!(boolean)map.get("success")){
            return map;
        }
        HashMap<String,Object> fields = (HashMap<String, Object>) actual.get("fields");
        Map<String,Object> result= new HashMap<>();

        List<String> ip_matcher = parseIp((String) fields.get("ip_matcher"));
        if(EnvUtils.isDebugger()){
            System.out.println(ip_matcher);
            System.out.println("共 "+ip_matcher.size()+" 个服务器");
        }
        Map<String, Object> default_fields = getDictFields(final_template, "default_fields");
        String regex = (String) default_fields.get("regex");
        List<String> filterIp = filterIp(ip_matcher,regex);
        List<Map<String,Object>> data = new ArrayList<>();

        String port = (String) fields.get("port");
        int timeout = Integer.parseInt((String) default_fields.get("timeout"));
        long startTime = System.currentTimeMillis();
        List<Map<String,Object>> connect_list = new ArrayList<>();
        for (String item:filterIp){

            if(port.contains("-")){
                String[] ports = port.split("-");
                int port_start = Integer.parseInt(ports[0]);
                int port_end = Integer.parseInt(ports[1]);
                for (int i = port_start;i<=port_end;i++){
                    Map<String,Object> connect= new HashMap<>();
                    connect.put("ip",item);
                    connect.put("port",i);
                    connect.put("timeout",timeout);
                    connect_list.add(connect);
                }

            }else{
                Map<String,Object> connect= new HashMap<>();
                connect.put("ip",item);
                connect.put("port", Integer.parseInt(port));
                connect.put("timeout",timeout);
                connect_list.add(connect);
            }

        }
        Map<String, Object> final_fields = getDictFields(final_template, "fields");
        String thread_number = (String) final_fields.getOrDefault("thread_number", "0");
        int tn = Integer.parseInt(thread_number);

        int wait = Integer.parseInt((String) default_fields.getOrDefault("wait","0"));
        Executor executor = new Executor(Executor.SERVER_PORT_CONNECT, connect_list, tn,wait);
        List<Map<String, Object>> execute = executor.execute();

        long endTime = System.currentTimeMillis();

        String show_connect = (String) final_fields.get("show_connect");
        if("true".equals(show_connect)){
            for(Map<String,Object> item:execute){
                boolean connect = (boolean) item.get("connect");
                if(connect){
                    data.add(item);
                }
            }
        }else{
            data.addAll(execute);
        }

        msg= String.format("扫描消耗 %s ms",(endTime - startTime));
        if(EnvUtils.isDebugger()){
            System.out.println(msg);
        }

        result.put("data",data);
        result.put("success",true);
        result.put("msg", msg);

        return result;
    }

    private List<String> filterIp(List<String> ip_list,String regex){

        Pattern pattern = Pattern.compile(regex);    // 编译正则表达式

        Set<String> ip_set = new HashSet<>();
        for(String item:ip_list){
               // 创建给定输入模式的匹配器
            Matcher matcher = pattern.matcher(item);
            boolean is_ip = matcher.matches();
            if (is_ip){
                ip_set.add(item);
            }
        }
        return new ArrayList<>(ip_set);
    }

    private List<String> parseIp(String ip_matcher){
        List<String> list = new ArrayList<>();
        if (StringUtils.isEmpty(ip_matcher)){
            return list;
        }
        String[] ip_split = ip_matcher.split(",");
        for (String item:ip_split){
            if (StringUtils.isEmpty(item)){
                continue;
            }
            item = item.trim();

            if (item.contains("-")||item.contains("~")){
                String  split_tags ="-";
                if (item.contains("~")){
                    split_tags = "~";
                }
                String start = item.substring(0,item.lastIndexOf("."));
                String[] end = item.substring(item.lastIndexOf("."),item.length()).split( split_tags);
                if(end.length<2){
                    continue;
                }
                String start_ip = start+end[0];
                String end_ip = start+"."+end[1];
                List<String> ip_range = parseIpRange(start_ip, end_ip);
                list.addAll(ip_range);
            }else{
                list.addAll(parseIpMaskRange(item,"32"));
            }

        }

        return  list;

    }



}
