package com.sql_api.insight.shiro;

import com.sql_api.insight.data_source.DatabaseConnections;
import com.sql_api.insight.service_platform_query.ServicePlatformQuery;
import com.sql_api.insight.utils.PlatformQuerySimpleUtils;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        Map<String,Object> user= (Map<String, Object>) principal.fromRealm(this.getClass().getName()).iterator().next();
        //todo 根据用户获取权限
        List<String> permissions = new ArrayList<>();
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.addStringPermissions(permissions);//将权限放入shiro中.
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken uToken=(UsernamePasswordToken) token;//获取用户输入的token
        String username = uToken.getUsername();
        Map<String,Object> query_user_by_username = new HashMap<>();
        query_user_by_username.put("username",username);
        Map<String, Object> result = PlatformQuerySimpleUtils.result("query_user_by_username", query_user_by_username);
        Map<String, Object> map = PlatformQuerySimpleUtils.resultOne(result);
        if(map == null){
            map = new HashMap<>();
        }
        return new SimpleAuthenticationInfo(map, map.get("password"),this.getClass().getName());//放入shiro.调用CredentialsMatcher检验密码
    }
}
