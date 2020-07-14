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
     *
     * @param userUuid
     * @return
     * @throws Exception
     */
    List<String> getRoleUuidsByUserUuid(String userUuid) throws Exception;

    /**
     * 批量插入用户、角色关联关系
     * @param userUuid
     * @param roleUuidList
     * @return
     * @throws Exception
     */
    boolean batchInsert(String userUuid, List<String> roleUuidList) throws Exception;

    /**
     * 根据用户UUID，删除用户-角色关联关系
     * @param userUuid
     * @return
     * @throws Exception
     */
    boolean delByUserUuid(String userUuid)throws Exception;
}
