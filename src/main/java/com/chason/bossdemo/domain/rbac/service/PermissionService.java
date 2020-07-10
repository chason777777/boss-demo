package com.chason.bossdemo.domain.rbac.service;

import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:03
 * @Description:
 */
public interface PermissionService {
    /**
     * 根据用户uuid，查询权限url
     * @param userUuid
     * @return
     * @throws Exception
     */
    List<String> getPermissionUrlsByUserUuid(String userUuid)throws Exception;
}
