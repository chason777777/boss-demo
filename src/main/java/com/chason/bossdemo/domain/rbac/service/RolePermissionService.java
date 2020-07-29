package com.chason.bossdemo.domain.rbac.service;

import com.chason.bossdemo.domain.rbac.entity.RolePermission;

import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:04
 * @Description:
 */
public interface RolePermissionService {
    /**
     * 根据roleUuids查询permissionUuids
     * @param roleUuids
     * @return
     */
    List<String> getPermissionUuidsByRoleUuids(List<String> roleUuids) throws Exception;

    /**
     * 根据角色uuid，查询所有权限uuid
     *
     * @param roleUuid
     * @return
     * @throws Exception
     */
    List<String> getPermissionUuidsByRoleUuid(String roleUuid) throws Exception;

    /**
     * 根据角色uuid 查询所有关联列表
     *
     * @param roleUuid
     * @return
     * @throws Exception
     */
    List<RolePermission> getByRoleUuid(String roleUuid) throws Exception;

    /**
     * 批量插入
     *
     * @param roleUuid
     * @param permissionUuids
     * @return
     * @throws Exception
     */
    boolean batchInsert(String roleUuid, List<String> permissionUuids) throws Exception;

    /**
     * 批量删除
     *
     * @param roleUuid
     * @return
     * @throws Exception
     */
    boolean delByRoleUuid(String roleUuid) throws Exception;
}
