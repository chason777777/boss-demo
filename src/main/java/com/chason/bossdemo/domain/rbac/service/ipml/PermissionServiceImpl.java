package com.chason.bossdemo.domain.rbac.service.ipml;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.common.constant.ErrorType;
import com.chason.bossdemo.common.util.DateTimeUtil;
import com.chason.bossdemo.common.util.UuidUtil;
import com.chason.bossdemo.domain.rbac.entity.Permission;
import com.chason.bossdemo.domain.rbac.entity.PermissionExample;
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

    @Override
    public Result add(JSONObject requestJson) throws Exception {
        if (null == requestJson || StringUtils.isEmpty(requestJson.getString("url"))) {
            return Result.error(ErrorType.PARAM_ISNULL.getKey(), ErrorType.PARAM_ISNULL.getValue());
        }

        Permission permission = new Permission();
        permission.setUuid(UuidUtil.randomUUID());
        permission.setPermissionName(requestJson.getString("permissionName"));
        permission.setUrl(requestJson.getString("url"));
        permission.setPerCode(requestJson.getString("perCode"));
        permission.setDescription(requestJson.getString("description"));
        permission.setSortNum(requestJson.getInteger("sortNum"));
        permission.setCreateTime(DateTimeUtil.nowDate());
        permission.setUpdateTime(DateTimeUtil.nowDate());

        boolean flag = permissionMapper.insertSelective(permission) > 0;
        if (flag) {
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

        PermissionExample example = new PermissionExample();
        PermissionExample.Criteria criteria = example.createCriteria();
        criteria.andUuidEqualTo(requestJson.getString("uuid"));

        boolean flag = permissionMapper.deleteByExample(example) > 0;
        if (flag) {
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

        Permission permission = new Permission();
        permission.setUuid(requestJson.getString("uuid"));
        permission.setPermissionName(requestJson.getString("permissionName"));
        permission.setUrl(requestJson.getString("url"));
        permission.setPerCode(requestJson.getString("perCode"));
        permission.setDescription(requestJson.getString("description"));
        permission.setSortNum(requestJson.getInteger("sortNum"));
        permission.setUpdateTime(DateTimeUtil.nowDate());

        PermissionExample example = new PermissionExample();
        PermissionExample.Criteria criteria = example.createCriteria();
        criteria.andUuidEqualTo(requestJson.getString("uuid"));


        boolean flag = permissionMapper.updateByExampleSelective(permission, example) > 0;
        if (flag) {
            return Result.success("修改成功");
        } else {
            return Result.error(ErrorType.RESULT_FAIL.getKey(), ErrorType.RESULT_FAIL.getValue());
        }
    }

    @Override
    public Result getAll() throws Exception {
        return Result.success(getList(), "查询成功");
    }

    @Override
    public List<Permission> getList() throws Exception {
        PermissionExample example = new PermissionExample();
        example.setOrderByClause("IF(ISNULL(sort_num),1,0),sort_num");

        return permissionMapper.selectByExample(example);
    }
}
