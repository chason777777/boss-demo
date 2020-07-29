package com.chason.bossdemo.domain.rbac.controller;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.BaseController;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.domain.rbac.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chason
 * @Date: 2020/7/29 16:34
 * @Description:
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @RequestMapping("add")
    public Result add(@RequestBody JSONObject requestJson) throws Exception {
        return roleService.add(requestJson);
    }

    @RequestMapping("del")
    public Result del(@RequestBody JSONObject requestJson) throws Exception {
        return roleService.del(requestJson);
    }

    @RequestMapping("update")
    public Result update(@RequestBody JSONObject requestJson) throws Exception {
        return roleService.update(requestJson);
    }

    @RequestMapping("getAll")
    public Result getAll() throws Exception {
        return roleService.getAll4Result();
    }

    @RequestMapping("detail")
    public Result detail(@RequestBody JSONObject requestJson) throws Exception {
        return roleService.detail(requestJson);
    }
}
