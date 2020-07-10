package com.chason.bossdemo.domain.rbac.service;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.domain.rbac.entity.BossUser;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:01
 * @Description:
 */
public interface BossUserService {
    /**
     * 用户登录
     *
     * @param requestJson
     * @return
     * @throws Exception
     */
    Result login(JSONObject requestJson) throws Exception;

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     * @throws Exception
     */
    BossUser getBossUserByUsername(String userName)throws Exception;

    /**
     * 根据用户uuid修改用户信息
     * @param bossUser
     * @return
     * @throws Exception
     */
    boolean updateByUuid(BossUser bossUser)throws Exception;
}
