package com.chason.bossdemo.domain.rbac.service.ipml;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.common.constant.ErrorType;
import com.chason.bossdemo.common.util.DateTimeUtil;
import com.chason.bossdemo.common.util.Md5Util;
import com.chason.bossdemo.common.util.UuidUtil;
import com.chason.bossdemo.domain.rbac.entity.BossUser;
import com.chason.bossdemo.domain.rbac.entity.BossUserExample;
import com.chason.bossdemo.domain.rbac.entity.Role;
import com.chason.bossdemo.domain.rbac.repository.BossUserMapper;
import com.chason.bossdemo.domain.rbac.service.BossUserService;
import com.chason.bossdemo.domain.rbac.service.PermissionService;
import com.chason.bossdemo.domain.rbac.service.RoleService;
import com.chason.bossdemo.domain.rbac.service.UserRoleService;
import com.chason.bossdemo.infrastructure.redis.service.RedisService;
import com.chason.bossdemo.infrastructure.redis.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:02
 * @Description:
 */
@Service("bossUserService")
public class BossUserServiceImpl implements BossUserService {
    @Autowired
    private BossUserMapper bossUserMapper;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    @Override
    public Result login(JSONObject requestJson) throws Exception {
        String password = requestJson.getString("password");
        String userName = requestJson.getString("userName");
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(userName)) {
            return Result.error(ErrorType.PARAM_ISNULL.getKey(), ErrorType.PARAM_ISNULL.getValue());
        }

        BossUser bossUser = getBossUserByUsername(userName);
        String passwordStr = Md5Util.getMD5((bossUser.getSalt() + password).getBytes());

        if (passwordStr.equals(bossUser.getPassword())) {
            return Result.error(ErrorType.USERNAME_PASSWORD_IS_WRONG.getKey(), ErrorType.USERNAME_PASSWORD_IS_WRONG.getValue());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", bossUser.getUserName());
        jsonObject.put("uuid", bossUser.getUuid());
        jsonObject.put("level", bossUser.getLevel());
        jsonObject.put("time", System.currentTimeMillis() / 1000);

        // 查询用户权限
        List<String> perUrlList = permissionService.getPermissionUrlsByUserUuid(bossUser.getUuid());
        jsonObject.put("urls", perUrlList);
        bossUser.setPermissionUrlList(perUrlList);

        String token = UuidUtil.randomUUID();
        bossUser.setToken(token);
        // 保存redis
        boolean redisFlag = redisService.set(RedisKeyUtil.getUserLoginKey(token), jsonObject.toJSONString(), 60 * 60 * 2);

        if (redisFlag) {
            // 更新最后登陆时间
            BossUser upUser = new BossUser();
            upUser.setUuid(bossUser.getUuid());
            upUser.setLastLoginTime(new Date());
            updateByUuid(upUser);
            return Result.success(userName, "登录成功");
        } else {
            return Result.error(ErrorType.LOGIN_FAIL.getKey(), ErrorType.LOGIN_FAIL.getValue());
        }
    }

    @Override
    public Result list() throws Exception {
        BossUserExample example = new BossUserExample();
        example.setOrderByClause("id desc");
//        BossUserExample.Criteria criteria = example.createCriteria();

        List<BossUser> list = bossUserMapper.selectByExample(example);
        return Result.success(list, "查询成功");
    }

    @Override
    public Result add(JSONObject requestJson) throws Exception {
        if (null == requestJson || StringUtils.isEmpty(requestJson.getString("userName")) || StringUtils.isEmpty(requestJson.getString("password"))) {
            return Result.error(ErrorType.PARAM_ISNULL.getKey(), ErrorType.PARAM_ISNULL.getValue());
        }

        // 判定用户是否已经存在
        BossUser old = getBossUserByUsername(requestJson.getString("userName"));
        if (null != old) {
            return Result.error(ErrorType.USER_EXISTS.getKey(), ErrorType.USER_EXISTS.getValue());
        }

        BossUser bossUser = new BossUser();
        bossUser.setUuid(UuidUtil.randomUUID());
        bossUser.setUserName(requestJson.getString("userName"));

        bossUser.setSalt(UuidUtil.randomSalt());
        bossUser.setPassword(Md5Util.getMD5((bossUser.getSalt() + requestJson.getString("password")).getBytes()));
        bossUser.setCreateTime(DateTimeUtil.nowDate());
        bossUser.setUpdateTime(DateTimeUtil.nowDate());

        boolean flag = bossUserMapper.insertSelective(bossUser) > 0;
        if (flag) {
            // 添加角色权限关联关系
            if (StringUtils.isNotEmpty(requestJson.getString("roleUuids"))) {
                List<String> roleUuids = Arrays.asList(requestJson.getString("roleUuids").split(","));
                if (!CollectionUtils.isEmpty(roleUuids)) {
                    userRoleService.batchInsert(bossUser.getUuid(), roleUuids);
                }
            }
            return Result.success("添加成功");
        } else {
            return Result.error(ErrorType.RESULT_FAIL.getKey(), ErrorType.RESULT_FAIL.getValue());
        }
    }

    @Override
    public Result update(JSONObject requestJson) throws Exception {
        if (null == requestJson || StringUtils.isEmpty(requestJson.getString("uuid"))) {
            return Result.error(ErrorType.PARAM_ISNULL.getKey(), ErrorType.PARAM_ISNULL.getValue());
        }

        // 判定用户是否已经存在
        BossUser old = null;
        if (StringUtils.isNotEmpty(requestJson.getString("userName"))) {
            old = getBossUserByUsername(requestJson.getString("userName"));
            if (null != old && !old.getUuid().equals(requestJson.getString("uuid"))) {
                return Result.error(ErrorType.USER_EXISTS.getKey(), ErrorType.USER_EXISTS.getValue());
            }
        }

        old = getBossUserByUuid(requestJson.getString("uuid"));
        if (null == old){
            return Result.error(ErrorType.USER_UNEXISTS.getKey(), ErrorType.USER_UNEXISTS.getValue());
        }

        BossUser bossUser = new BossUser();
        bossUser.setUuid(requestJson.getString("uuid"));
        if (StringUtils.isNotEmpty(requestJson.getString("password"))) {
            bossUser.setPassword(Md5Util.getMD5((old.getSalt() + requestJson.getString("password")).getBytes()));
        }
//        bossUser.setCreateTime(DateTimeUtil.nowDate());
        bossUser.setUpdateTime(DateTimeUtil.nowDate());

        BossUserExample example = new BossUserExample();
        BossUserExample.Criteria criteria = example.createCriteria();
        criteria.andUuidEqualTo(requestJson.getString("uuid"));

        boolean flag = bossUserMapper.updateByExampleSelective(bossUser, example) > 0;
        if (flag) {
            // 添加角色权限关联关系
            userRoleService.delByUserUuid(requestJson.getString("uuid"));

            // 添加角色权限关联关系
            if (StringUtils.isNotEmpty(requestJson.getString("roleUuids"))) {
                List<String> roleUuids = Arrays.asList(requestJson.getString("roleUuids").split(","));
                if (!CollectionUtils.isEmpty(roleUuids)) {
                    userRoleService.batchInsert(bossUser.getUuid(), roleUuids);
                }
            }
            return Result.success("修改成功");
        } else {
            return Result.error(ErrorType.RESULT_FAIL.getKey(), ErrorType.RESULT_FAIL.getValue());
        }
    }

    @Override
    public Result delete(JSONObject requestJson) throws Exception {
        if (null == requestJson || StringUtils.isEmpty(requestJson.getString("uuid"))) {
            return Result.error(ErrorType.PARAM_ISNULL.getKey(), ErrorType.PARAM_ISNULL.getValue());
        }

        BossUserExample example = new BossUserExample();
        BossUserExample.Criteria criteria = example.createCriteria();
        criteria.andUuidEqualTo(requestJson.getString("uuid"));

        boolean flag = bossUserMapper.deleteByExample(example) > 0;

        if (flag) {
            // 删除角色权限关联关系
            userRoleService.delByUserUuid(requestJson.getString("uuid"));
            return Result.success("删除成功");
        } else {
            return Result.error(ErrorType.RESULT_FAIL.getKey(), ErrorType.RESULT_FAIL.getValue());
        }
    }

    @Override
    public Result detaill(JSONObject requestJson) throws Exception {
        if (null == requestJson || StringUtils.isEmpty(requestJson.getString("uuid"))) {
            return Result.error(ErrorType.PARAM_ISNULL.getKey(), ErrorType.PARAM_ISNULL.getValue());
        }

        BossUser bossUser = getBossUserByUuid(requestJson.getString("uuid"));
        if (null == bossUser){
            return Result.error(ErrorType.USER_UNEXISTS.getKey(), ErrorType.USER_UNEXISTS.getValue());
        }
        List<Role> roleList = roleService.getAll();
        if (!CollectionUtils.isEmpty(roleList)) {
            List<String> roleUuids = userRoleService.getRoleUuidsByUserUuid(requestJson.getString("uuid"));
            if (!CollectionUtils.isEmpty(roleUuids)) {
                for (Role role : roleList) {
                    if (roleUuids.contains(role.getUuid())) {
                        role.setIsOwn(true);
                    }
                }
            }
        }
        bossUser.setRoleList(roleList);
        return Result.success(bossUser, "查询成功");
    }

    @Override
    public BossUser getBossUserByUsername(String userName) throws Exception {
        if (StringUtils.isEmpty(userName))
            return null;

        BossUserExample example = new BossUserExample();
        BossUserExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(userName);

        List<BossUser> list = bossUserMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public BossUser getBossUserByUuid(String uuid) throws Exception {
        if (StringUtils.isEmpty(uuid))
            return null;

        BossUserExample example = new BossUserExample();
        BossUserExample.Criteria criteria = example.createCriteria();
        criteria.andUuidEqualTo(uuid);
        List<BossUser> list = bossUserMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public boolean updateByUuid(BossUser bossUser) throws Exception {
        if (null == bossUser || StringUtils.isEmpty(bossUser.getUuid())) {
            return false;
        }

        BossUserExample example = new BossUserExample();
        BossUserExample.Criteria criteria = example.createCriteria();
        criteria.andUuidEqualTo(bossUser.getUuid());

        return bossUserMapper.updateByExampleSelective(bossUser, example) > 0;
    }
}
