package com.chason.bossdemo.domain.rbac.controller;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.BaseController;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.domain.rbac.service.BossUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:00
 * @Description:
 */
@RestController
@RequestMapping("/bossUser")
public class LoginController extends BaseController {
    @Autowired
    private BossUserService bossUserService;

    @PostMapping("login")
    public Result login(@RequestBody JSONObject requestJson) throws Exception{
        return bossUserService.login(requestJson);
    }

    @PostMapping("logout")
    public Result logout(HttpServletRequest request) throws Exception{
        return bossUserService.logout(request);
    }
}
