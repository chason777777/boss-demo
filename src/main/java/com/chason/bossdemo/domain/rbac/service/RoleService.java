package com.chason.bossdemo.domain.rbac.service;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.domain.rbac.entity.Role;

import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:03
 * @Description:
 */
public interface RoleService {
    /**
     * 查询所有角色
     * @return
     */
    List<Role> getAll();

    /**
     * 添加角色
     *
     * @param requestJson
     * @return
     * @throws Exception
     */
    Result add(JSONObject requestJson) throws Exception;

    /**
     * 根据uuid删除角色
     *
     * @param requestJson
     * @return
     * @throws Exception
     */
    Result del(JSONObject requestJson) throws Exception;

    /**
     * 修改角色
     *
     * @param requestJson
     * @return
     * @throws Exception
     */
    Result update(JSONObject requestJson) throws Exception;

    /**
     * 查询所有角色
     *
     * @return
     * @throws Exception
     */
    Result getAll4Result() throws Exception;

    /**
     * 查询角色详情
     *
     * @param requestJson
     * @return
     * @throws Exception
     */
    Result detail(JSONObject requestJson) throws Exception;

    /**
     * 查询所有角色
     *
     * @return
     * @throws Exception
     */
    List<Role> getList() throws Exception;
 }
