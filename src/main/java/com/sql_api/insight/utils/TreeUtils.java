package com.sql_api.insight.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUtils {

    /**
     * listToTree
     * <p>方法说明<p>
     * 将JSONArray数组转为树状结构
     * @param arr 需要转化的数据
     * @param id 数据唯一的标识键值
     * @param pid 父id唯一标识键值
     * @param child 子节点键值
     * @return JSONArray
     */
    public static List<Map<String,Object>> listToTree(List<Map<String,Object>> arr, String id, String pid, String child){
        List<Map<String,Object>> r = new ArrayList<>();
        Map<String,Object> hash = new HashMap<>();
        //将数组转为Object的形式，key为数组中的id
        for(int i=0;i<arr.size();i++){

            Map<String, Object> map = arr.get(i);
            hash.put(map.get(id)+"", map);
        }
        //遍历结果集
        for(int j=0;j<arr.size();j++){
            //单条记录
            Map<String,Object> aVal =  arr.get(j);
            //在hash中取出key为单条记录中pid的值
            Map<String,Object> hashVP = (Map<String, Object>) hash.get(aVal.get(pid).toString());
            //如果记录的pid存在，则说明它有父节点，将她添加到孩子节点的集合中
            if(hashVP!=null){
                //检查是否有child属性
                if(hashVP.get(child)!=null){
                    List<Map<String,Object>> ch = (List<Map<String, Object>>) hashVP.get(child);
                    ch.add(aVal);
                    hashVP.put(child, ch);
                }else{
                    List<Map<String,Object>> ch = new ArrayList<>();
                    ch.add(aVal);
                    hashVP.put(child, ch);
                }
            }else{
                r.add(aVal);

            }
        }


        return r;
    }

}
