package com.chason.bossdemo.domain.rbac.service.ipml;

import com.chason.bossdemo.domain.rbac.repository.PermissionMapper;
import com.chason.bossdemo.domain.rbac.service.PermissionService;
import com.chason.bossdemo.domain.rbac.service.RolePermissionService;
import com.chason.bossdemo.domain.rbac.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:06
 * @Description:
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService{
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public List<String> getPermissionUrlsByUserUuid(String userUuid) throws Exception {
        if (StringUtils.isEmpty(userUuid))
            return null;

        List<String> roleList = userRoleService.getRoleUuidsByUserUuid(userUuid);
        if (CollectionUtils.isEmpty(roleList))
            return null;

        List<String> permissionList = rolePermissionService.getPermissionUuidsByRoleUuids(roleList);

        if (CollectionUtils.isEmpty(permissionList))
            return null;

        List<String> permissionUrlList = permissionMapper.getPermissionUrlsByPermissionUuids(permissionList);

        return permissionUrlList;
    }
}
