package com.chason.bossdemo.domain.rbac.service.ipml;

import com.chason.bossdemo.common.util.DateTimeUtil;
import com.chason.bossdemo.common.util.UuidUtil;
import com.chason.bossdemo.domain.rbac.entity.RolePermission;
import com.chason.bossdemo.domain.rbac.entity.RolePermissionExample;
import com.chason.bossdemo.domain.rbac.repository.RolePermissionMapper;
import com.chason.bossdemo.domain.rbac.service.RolePermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

    @Override
    public List<String> getPermissionUuidsByRoleUuid(String roleUuid) throws Exception {
        if (StringUtils.isEmpty(roleUuid)) {
            return null;
        }

        List<RolePermission> list = getByRoleUuid(roleUuid);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<String> permissionUuids = new ArrayList<String>();
        for (RolePermission rolePermission : list) {
            permissionUuids.add(rolePermission.getPermissionUuid());
        }

        return permissionUuids;
    }

    @Override
    public List<RolePermission> getByRoleUuid(String roleUuid) throws Exception {
        if (StringUtils.isEmpty(roleUuid)) {
            return null;
        }

        RolePermissionExample example = new RolePermissionExample();
        RolePermissionExample.Criteria criteria = example.createCriteria();
        criteria.andRoleUuidEqualTo(roleUuid);

        return rolePermissionMapper.selectByExample(example);
    }

    @Override
    public boolean batchInsert(String roleUuid, List<String> permissionUuids) throws Exception {
        if (StringUtils.isEmpty(roleUuid) || CollectionUtils.isEmpty(permissionUuids)) {
            return false;
        }
        List<RolePermission> list = new ArrayList<RolePermission>();
        for (String permissionUuid : permissionUuids) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setUuid(UuidUtil.randomUUID());
            rolePermission.setRoleUuid(roleUuid);
            rolePermission.setPermissionUuid(permissionUuid);
            rolePermission.setCreateTime(DateTimeUtil.nowDate());
            rolePermission.setUpdateTime(DateTimeUtil.nowDate());
            list.add(rolePermission);
        }

        return rolePermissionMapper.batchInsert(list) > 0;
    }

    @Override
    public boolean delByRoleUuid(String roleUuid) throws Exception {
        if (StringUtils.isEmpty(roleUuid)) {
            return false;
        }

        RolePermissionExample example = new RolePermissionExample();
        RolePermissionExample.Criteria criteria = example.createCriteria();
        criteria.andRoleUuidEqualTo(roleUuid);

        return rolePermissionMapper.deleteByExample(example) > 0;
    }
}
