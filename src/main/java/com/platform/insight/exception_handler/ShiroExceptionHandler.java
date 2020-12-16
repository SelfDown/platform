package com.platform.insight.exception_handler;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ShiroExceptionHandler implements HandlerExceptionResolver {
    public ShiroExceptionHandler() {
    }

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex instanceof IllegalStateException) {
            return null;
        } else if (ex instanceof UnauthenticatedException) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            try {
                response.getWriter().print("未登录！");
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException var7) {
                ;
            }

            return new ModelAndView();
        } else if (!(ex instanceof UnauthorizedException) && !(ex instanceof UnauthenticatedException)) {
            return ex instanceof MaxUploadSizeExceededException ? null : null;
        } else {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");

                try {
                    response.getWriter().print("无权限！");
                    response.getWriter().flush();
                    response.getWriter().close();
                } catch (IOException var8) {
                    ;
                }

                return new ModelAndView();
            } else {
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");

                try {
                    response.getWriter().print("未登录！");
                    response.getWriter().flush();
                    response.getWriter().close();
                } catch (IOException var9) {
                    ;
                }

                return new ModelAndView();
            }
        }
    }
}
