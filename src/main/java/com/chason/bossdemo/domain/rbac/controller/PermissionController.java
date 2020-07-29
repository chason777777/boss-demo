package com.chason.bossdemo.domain.rbac.controller;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.BaseController;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.domain.rbac.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chason
 * @Date: 2020/7/29 16:25
 * @Description:
 */
@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("add")
    public Result add(@RequestBody JSONObject requestJson) throws Exception {
        return permissionService.add(requestJson);
    }

    @RequestMapping("del")
    public Result del(@RequestBody JSONObject requestJson) throws Exception {
        return permissionService.del(requestJson);
    }

    @RequestMapping("update")
    public Result update(@RequestBody JSONObject requestJson) throws Exception {
        return permissionService.update(requestJson);
    }

    @RequestMapping("getAll")
    public Result getAll() throws Exception {
        return permissionService.getAll();
    }
}
