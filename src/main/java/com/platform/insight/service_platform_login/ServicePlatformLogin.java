package com.platform.insight.service_platform_login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.platform.insight.model.ApiModel;
import com.platform.insight.service.ResultBaseService;
import com.platform.insight.utils.EncryptContext;
import com.platform.insight.utils.MD5Utils;
import com.platform.insight.utils.UserManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(ServicePlatformLogin.ID)
public class ServicePlatformLogin extends ResultBaseService implements  ApiService {

//    @Qualifier("sessionDAO")
//    SessionDAO sessionDAO;

    public static final String ID = "service_platform_login";
    @Override
    public Map<String, Object> result(Map<String, Object> actual) {


        Map<String,Object> result = new HashMap<>();
        String service = (String) actual.get("service");
        ApiModel apiModel = getApiModel(service);
        String template_final = apiModel.getTemplate();
        JSONObject final_template = JSON.parseObject(template_final);
        //检查类型
        if(!actual.containsKey("fields")){
            actual.put("fields",new HashMap<>());
        }

        //检查筛选条件字段 filters,如果有记录就保存起来
        String msg = null;
        Map<String, Object> fields = checkFields((Map<String, Object>) actual.get("fields"), final_template.getJSONArray("fields"),final_template);
        if(!(boolean)fields.get("success")){
            return fields;

        }
        Map<String,Object> fields_map = (Map<String, Object>) actual.get("fields");
        String username = (String) fields_map.get("username");
        String password = (String) fields_map.get("password");
        EncryptContext dc = new EncryptContext(MD5Utils.getMD5String(username + "admindms@888").toUpperCase(), password.toUpperCase());
        UsernamePasswordToken shiroUsernamePasswordToken = new UsernamePasswordToken(username,dc.getEncryptPassword() );
        boolean alwaysLogin = getAlwaysLogin(actual, final_template.getJSONArray("fields"));
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
        List<Session> deleteSessionList = new ArrayList<>();
        //获取以前登陆的信息
        SessionDAO sessionDAO = sessionManager.getSessionDAO();
        Collection<Session> activeSessions =sessionDAO .getActiveSessions();
        for (Session item: activeSessions){
            Object attribute = item.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if(attribute!= null){
                SimplePrincipalCollection old_attribute = (SimplePrincipalCollection) attribute;
                Map<String,Object>  obj = (Map<String, Object>) old_attribute.getPrimaryPrincipal();
                String username_history = (String) obj.get("username");
                if(username_history.equals(username)){
                    deleteSessionList.add(item);
                }
            }

        }
        //是否强制登陆
        if(alwaysLogin){
            if(session!=null){
                session.stop();
            }


        }else{
            //已经登过了
            if(deleteSessionList.size()>0){
                result.put("code", "3");
                result.put("success",false);
                result.put("msg","用户已经登陆");
                return result;
            }
        }
        try {
            subject.login(shiroUsernamePasswordToken);
            session = subject.getSession();
            SimplePrincipalCollection new_attribute = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            //删除
            for(Session item:deleteSessionList){
                sessionDAO.delete(item);
            }

        } catch (UnknownAccountException var11) {
            result.put("code", "1");
            result.put("success",false);
            result.put("msg","用户不存在");
            return result;
        }catch (AuthenticationException var13) {
            result.put("code", "2");
            result.put("success",false);
            result.put("msg","密码错误");
            return result;
        }

        result.put("data", UserManager.getUserInfo());
        result.put("success",true);
        result.put("msg","登陆成功");
        return result;
    }


    private boolean getAlwaysLogin(Map<String,Object> actual, JSONArray fields){

        boolean alwaysLogin = actual.containsKey("alwaysLogin");
        if(actual.containsKey("fields")){
            Map<String,Object> fields_actual = (Map<String, Object>) actual.get("fields");
            if("true".equals(fields_actual.get("alwaysLogin"))){
                return true;
            }else{
                return false;
            }
        }
        for(int i=0;i<fields.size();i++){
            JSONObject jsonObject = fields.getJSONObject(i);
            String field_1 = jsonObject.getString("field_1");
            if("alwaysLogin".equals(field_1)){
                if("true".equals(jsonObject.get("field_5"))){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
}
