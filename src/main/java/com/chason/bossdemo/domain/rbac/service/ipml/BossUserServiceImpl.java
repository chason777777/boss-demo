package com.chason.bossdemo.domain.rbac.service.ipml;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.common.constant.ErrorType;
import com.chason.bossdemo.common.util.Md5Util;
import com.chason.bossdemo.common.util.UuidUtil;
import com.chason.bossdemo.domain.rbac.entity.BossUser;
import com.chason.bossdemo.domain.rbac.entity.BossUserExample;
import com.chason.bossdemo.domain.rbac.repository.BossUserMapper;
import com.chason.bossdemo.domain.rbac.service.BossUserService;
import com.chason.bossdemo.domain.rbac.service.PermissionService;
import com.chason.bossdemo.infrastructure.redis.service.RedisService;
import com.chason.bossdemo.infrastructure.redis.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

        if (redisFlag){
            // 更新最后登陆时间
            BossUser upUser = new BossUser();
            upUser.setUuid(bossUser.getUuid());
            upUser.setLastLoginTime(new Date());
            updateByUuid(upUser);
            return Result.success(userName, "登录成功");
        } else{
            return Result.error(ErrorType.LOGIN_FAIL.getKey(), ErrorType.LOGIN_FAIL.getValue());
        }
    }

    @Override
    public BossUser getBossUserByUsername(String userName) throws Exception {
        if (StringUtils.isEmpty(userName))
            return null;

        BossUserExample example = new BossUserExample();
        BossUserExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(userName);

        List<BossUser> list = bossUserMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list))
            return null;
        else
            return list.get(0);
    }

    @Override
    public boolean updateByUuid(BossUser bossUser) throws Exception {
        if  (null == bossUser || StringUtils.isEmpty(bossUser.getUuid())){
            return false;
        }

        BossUserExample example = new BossUserExample();
        BossUserExample.Criteria criteria = example.createCriteria();
        criteria.andUuidEqualTo(bossUser.getUuid());

        return bossUserMapper.updateByExampleSelective(bossUser, example) > 0;
    }
}
