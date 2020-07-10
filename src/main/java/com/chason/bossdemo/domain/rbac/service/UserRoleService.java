package com.chason.bossdemo.domain.rbac.service;

import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:03
 * @Description:
 */
public interface UserRoleService {
    /**
     * 根据用户uuid，查询角色uuids
     * @param userUuid
     * @return
     * @throws Exception
     */
    List<String> getRoleUuidsByUserUuid(String userUuid)throws Exception;
}
