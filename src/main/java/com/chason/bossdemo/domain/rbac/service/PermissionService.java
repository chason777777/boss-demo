package com.chason.bossdemo.domain.rbac.service;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.domain.rbac.entity.Permission;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:03
 * @Description:
 */
@Service("permissionService")
public interface PermissionService {
    /**
     * 根据用户uuid，查询权限url
     * @param userUuid
     * @return
     * @throws Exception
     */
    List<String> getPermissionUrlsByUserUuid(String userUuid)throws Exception;

    /**
     * 添加权限
     *
     * @param requestJson
     * @return
     * @throws Exception
     */
    Result add(JSONObject requestJson) throws Exception;

    /**
     * 根据uuid删除权限
     *
     * @param requestJson
     * @return
     * @throws Exception
     */
    Result del(JSONObject requestJson) throws Exception;

    /**
     * 修改权限
     *
     * @param requestJson
     * @return
     * @throws Exception
     */
    Result update(JSONObject requestJson) throws Exception;

    /**
     * 查询所有权限
     *
     * @return
     * @throws Exception
     */
    Result getAll() throws Exception;

    /**
     * 查询所有权限
     *
     * @return
     * @throws Exception
     */
    List<Permission> getList() throws Exception;
}
