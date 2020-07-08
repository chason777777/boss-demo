package com.chason.bossdemo.infrastructure.filter;

import com.chason.bossdemo.infrastructure.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// 该项目使用filter作为token、权限校验
//@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    //service注入在拦截器的注册器中
    @Autowired
    private RedisService redisService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RbacUserLogonAuthInterceptor(redisService)).addPathPatterns("/**");
    }
}
