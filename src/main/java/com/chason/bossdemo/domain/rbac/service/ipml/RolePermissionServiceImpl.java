package com.chason.bossdemo.domain.rbac.service.ipml;

import com.chason.bossdemo.domain.rbac.repository.RolePermissionMapper;
import com.chason.bossdemo.domain.rbac.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:06
 * @Description:
 */
@Service("rolePermissionService")
public class RolePermissionServiceImpl implements RolePermissionService {
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<String> getPermissionUuidsByRoleUuids(List<String> roleUuids) throws Exception {
        if (CollectionUtils.isEmpty(roleUuids))
            return null;

        return rolePermissionMapper.getPermissionUuidsByRoleUuids(roleUuids);
    }
}
