package com.chason.bossdemo.domain.rbac.controller;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.BaseController;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.domain.rbac.service.BossUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chason
 * @Date: 2020/7/13 20:04
 * @Description:
 */
@RestController
@RequestMapping("/bossuser")
public class BossUserController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BossUserController.class);

    @Autowired
    private BossUserService bossUserService;

    @RequestMapping("/list")
    public Result list() throws Exception {
        return bossUserService.list();
    }

    @RequestMapping("/add")
    public Result add(@RequestBody JSONObject requestJson)throws Exception{
        return bossUserService.add(requestJson);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody JSONObject requestJson)throws Exception{
        return bossUserService.update(requestJson);
    }

    @RequestMapping("/delete")
    public Result delete(@RequestBody JSONObject requestJson)throws Exception{
        return bossUserService.delete(requestJson);
    }

    @RequestMapping("/detaill")
    public Result detaill(@RequestBody JSONObject requestJson)throws Exception{
        return bossUserService.detaill(requestJson);
    }


}
