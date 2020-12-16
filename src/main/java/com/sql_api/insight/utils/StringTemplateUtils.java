package com.sql_api.insight.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTemplateUtils {

    public static final String DEF_REGEX="\\$\\{(.+?)\\}";

    public static String render(String template, Map<String, Object> data) {
        return render(template,data,DEF_REGEX);
    }

    public static String render(String template, Map<String, Object> data, String regex) {
        if(StringUtils.isEmpty(template)){
            return "";
        }
        if(StringUtils.isEmpty(regex)){
            return template;
        }
        if(data == null || data.size() == 0){
            return template;
        }
        try {
            StringBuffer sb = new StringBuffer();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(template);
            while (matcher.find()) {
                String name = matcher.group(1);// 键名
                String value = data.get(name).toString();// 键值
                if (value == null) {value = "";}
                matcher.appendReplacement(sb, value);
            }
            matcher.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;

    }

    public static void main(String args[]) throws ParseException {
        String template="您提现${borrowAmount}元至尾号${tailNo}的请求失败，您可以重新提交提款申请。";
        Map<String, Object> data = new HashMap<>();
        data.put("borrowAmount", "1000.00");
        data.put("tailNo", "1234");
        System.out.println(render(template,data));
    }
}