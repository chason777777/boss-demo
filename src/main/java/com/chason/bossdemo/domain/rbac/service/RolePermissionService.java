package com.chason.bossdemo.domain.rbac.service;

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
}
