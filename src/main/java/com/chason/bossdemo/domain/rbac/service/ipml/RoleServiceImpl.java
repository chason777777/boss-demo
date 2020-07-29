package com.chason.bossdemo.domain.rbac.service.ipml;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.common.constant.ErrorType;
import com.chason.bossdemo.common.util.DateTimeUtil;
import com.chason.bossdemo.common.util.UuidUtil;
import com.chason.bossdemo.domain.rbac.entity.Permission;
import com.chason.bossdemo.domain.rbac.entity.Role;
import com.chason.bossdemo.domain.rbac.entity.RoleExample;
import com.chason.bossdemo.domain.rbac.repository.RoleMapper;
import com.chason.bossdemo.domain.rbac.service.PermissionService;
import com.chason.bossdemo.domain.rbac.service.RolePermissionService;
import com.chason.bossdemo.domain.rbac.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:04
 * @Description:
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public List<Role> getAll() {
        RoleExample example = new RoleExample();
        example.setOrderByClause("IF(ISNULL(sort_num),1,0),sort_num");
        return roleMapper.selectByExample(example);
    }

    @Override
    public Result add(JSONObject requestJson) throws Exception {
        if (null == requestJson || StringUtils.isEmpty(requestJson.getString("roleName"))) {
            return Result.error(ErrorType.PARAM_ISNULL.getKey(), ErrorType.PARAM_ISNULL.getValue());
        }
        Role role = new Role();
        role.setUuid(UuidUtil.randomUUID());
        role.setRoleName(requestJson.getString("roleName"));
        role.setDescription(requestJson.getString("description"));
        role.setSortNum(requestJson.getInteger("sortNum"));
        role.setStatus((byte) 0);
        role.setCreateTime(DateTimeUtil.nowDate());
        role.setUpdateTime(DateTimeUtil.nowDate());

        boolean flag = roleMapper.insertSelective(role) > 0;
        if (flag) {
            // 添加角色权限关联关系
            if (StringUtils.isNotEmpty(requestJson.getString("permissionUuids"))) {
                List<String> permissionUuids = Arrays.asList(requestJson.getString("permissionUuids").split(","));
                if (!CollectionUtils.isEmpty(permissionUuids)) {
                    rolePermissionService.batchInsert(role.getUuid(), permissionUuids);
                }
            }
            return Result.success("添加成功");
        } else {
            return Result.error(ErrorType.RESULT_FAIL.getKey(), ErrorType.RESULT_FAIL.getValue());
        }
    }

    @Override
    public Result del(JSONObject requestJson) throws Exception {
        if (null == requestJson || StringUtils.isEmpty(requestJson.getString("uuid"))) {
            return Result.error(ErrorType.PARAM_ISNULL.getKey(), ErrorType.PARAM_ISNULL.getValue());
        }

        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andUuidEqualTo(requestJson.getString("uuid"));

        boolean flag = roleMapper.deleteByExample(example) > 0;

        if (flag) {
            // 删除角色权限关联关系
            rolePermissionService.delByRoleUuid(requestJson.getString("uuid"));
            return Result.success("删除成功");
        } else {
            return Result.error(ErrorType.RESULT_FAIL.getKey(), ErrorType.RESULT_FAIL.getValue());
        }
    }

    @Override
    public Result update(JSONObject requestJson) throws Exception {
        if (null == requestJson || StringUtils.isEmpty(requestJson.getString("uuid"))) {
            return Result.error(ErrorType.PARAM_ISNULL.getKey(), ErrorType.PARAM_ISNULL.getValue());
        }
        Role role = new Role();
        role.setUuid(requestJson.getString("uuid"));
        role.setRoleName(requestJson.getString("roleName"));
        role.setDescription(requestJson.getString("description"));
        role.setSortNum(requestJson.getInteger("sortNum"));
        role.setUpdateTime(DateTimeUtil.nowDate());

        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andUuidEqualTo(requestJson.getString("uuid"));

        boolean flag = roleMapper.updateByExampleSelective(role, example) > 0;
        if (flag) {
            // 添加角色权限关联关系
            rolePermissionService.delByRoleUuid(requestJson.getString("uuid"));

            // 添加角色权限关联关系
            if (StringUtils.isNotEmpty(requestJson.getString("permissionUuids"))) {
                List<String> permissionUuids = Arrays.asList(requestJson.getString("permissionUuids").split(","));
                if (!CollectionUtils.isEmpty(permissionUuids)) {
                    rolePermissionService.batchInsert(role.getUuid(), permissionUuids);
                }
            }
            return Result.success("修改成功");
        } else {
            return Result.error(ErrorType.RESULT_FAIL.getKey(), ErrorType.RESULT_FAIL.getValue());
        }
    }

    @Override
    public Result getAll4Result() throws Exception {
        List<Role> list = getList();
        return Result.success(list, "查询成功");
    }

    @Override
    public Result detail(JSONObject requestJson) throws Exception {
        if (null == requestJson || StringUtils.isEmpty(requestJson.getString("uuid"))) {
            return Result.error(ErrorType.PARAM_ISNULL.getKey(), ErrorType.PARAM_ISNULL.getValue());
        }

        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andUuidEqualTo(requestJson.getString("uuid"));
        List<Role> list = roleMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            return Result.error(ErrorType.RESULT_ISNULL.getKey(), ErrorType.RESULT_ISNULL.getValue());
        } else {
            Role role = list.get(0);
            List<Permission> permissionList = permissionService.getList();
            if (!CollectionUtils.isEmpty(permissionList)) {
                List<String> permissionUuids = rolePermissionService.getPermissionUuidsByRoleUuid(requestJson.getString("uuid"));
                if (!CollectionUtils.isEmpty(permissionUuids)) {
                    for (Permission permission : permissionList) {
                        if (permissionUuids.contains(permission.getUuid())) {
                            permission.setIsOwn(true);
                        }
                    }
                }
            }
            role.setPermissionList(permissionList);
            return Result.success(role, "查询成功");
        }
    }

    @Override
    public List<Role> getList() throws Exception {
        RoleExample example = new RoleExample();
        example.setOrderByClause("IF(ISNULL(sort_num),1,0),sort_num");

        return roleMapper.selectByExample(example);
    }
}
