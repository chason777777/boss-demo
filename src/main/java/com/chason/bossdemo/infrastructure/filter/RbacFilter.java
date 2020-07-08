package com.chason.bossdemo.infrastructure.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaadas.orangeiotboss.infrastructure.redis.service.RedisService;
import com.kaadas.orangeiotboss.infrastructure.redis.util.RedisKeyUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

//注册器名称为customFilter,拦截的url为所有
@WebFilter(filterName = "RbacFilter", urlPatterns = {"/*"})
public class RbacFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RbacFilter.class);
    @Autowired
    private RedisService redisService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String reqStr = req.getRequestURI();
        if (!reqStr.equals("/bossUser/login") && !reqStr.equals("/bossUser/logout") && !reqStr.startsWith("/api") && !reqStr.startsWith("/util")
                && !reqStr.startsWith("/file") && !reqStr.equals("/bossUser/updatepwd")) {
            try {
                String token = req.getHeader("token");
                if (StringUtils.isNotEmpty(token)) {
//                    String tokenStr = new String(Base64.getDecoder().decode(token));
//                    JSONObject jsonObject = JSONObject.parseObject(tokenStr);
                    String serverToken = redisService.getString(RedisKeyUtil.getUserLoginKey(token));
                    logger.info("auth,token -> {},result -> {}", token, serverToken);
                    if (StringUtils.isEmpty(serverToken)){
                        servletRequest.getRequestDispatcher("/bossUser/unauth").forward(servletRequest, servletResponse);
                    } else {
                        JSONObject userInfo = JSONObject.parseObject(serverToken);
                        // 权限校验
                        if (userInfo.containsKey("level") && userInfo.getInteger("level") == 1) {// 超级管理员
                            redisService.expire(RedisKeyUtil.getUserLoginKey(token),60 * 60 * 2);
                            filterChain.doFilter(servletRequest, servletResponse);
                        }else {
                            JSONArray permissionUrlList =userInfo.getJSONArray("urls");
                            if (permissionUrlList != null && permissionUrlList.contains(reqStr)){
                                redisService.expire(RedisKeyUtil.getUserLoginKey(token),60 * 60 * 2);
                                filterChain.doFilter(servletRequest, servletResponse);
                            }else {
                                servletRequest.getRequestDispatcher("/bossUser/unauthorized").forward(servletRequest, servletResponse);
                            }
                        }
                    }
                }else {
                    servletRequest.getRequestDispatcher("/bossUser/unauth").forward(servletRequest, servletResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                servletRequest.getRequestDispatcher("/bossUser/unauth").forward(servletRequest, servletResponse);
            }
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
