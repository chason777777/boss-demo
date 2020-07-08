package com.chason.bossdemo.infrastructure.filter;

import com.chason.bossdemo.infrastructure.redis.service.RedisService;
import com.chason.bossdemo.infrastructure.redis.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于拦截验证用户是否已经登录
 */
public class RbacUserLogonAuthInterceptor extends HandlerInterceptorAdapter {
private static final Logger logger = LoggerFactory.getLogger(RbacUserLogonAuthInterceptor.class);
    private RedisService redisService;

    public RbacUserLogonAuthInterceptor(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        if (!requestUri.equals("/bossUser/login") && !requestUri.equals("/bossUser/logout") && !requestUri.startsWith("/api")
                && !requestUri.equals("/bossUser/unauth")) {
            try {
                String token = request.getHeader("token");
                if (StringUtils.isNotEmpty(token)) {
                    String serverToken = redisService.getString(RedisKeyUtil.getUserLoginKey(token));
                    logger.info("auth,token -> {},result -> {}", token, serverToken);
                    if (StringUtils.isEmpty(serverToken)) {
                        response.sendRedirect(request.getContextPath() + "/bossUser/unauth");
                        return false;
                    } else {
                        redisService.expire(RedisKeyUtil.getUserLoginKey(token), 60 * 60 * 2);
                        return true;
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/bossUser/unauth");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/bossUser/unauth");
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
