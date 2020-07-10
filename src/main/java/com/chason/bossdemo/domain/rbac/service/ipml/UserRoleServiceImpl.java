package com.chason.bossdemo.domain.rbac.service.ipml;

import com.chason.bossdemo.domain.rbac.entity.UserRoleExample;
import com.chason.bossdemo.domain.rbac.repository.UserRoleMapper;
import com.chason.bossdemo.domain.rbac.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:05
 * @Description:
 */
@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService{
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<String> getRoleUuidsByUserUuid(String userUuid) throws Exception {
        if (StringUtils.isEmpty(userUuid))
            return null;

        UserRoleExample example = new UserRoleExample();
        UserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andUserUuidEqualTo(userUuid);

        return userRoleMapper.getRoleUuidsByUserUuid(userUuid);
    }
}
