package com.chason.bossdemo.domain.rbac.service.ipml;

import com.chason.bossdemo.common.constant.StatusType;
import com.chason.bossdemo.common.util.UuidUtil;
import com.chason.bossdemo.domain.rbac.entity.UserRole;
import com.chason.bossdemo.domain.rbac.entity.UserRoleExample;
import com.chason.bossdemo.domain.rbac.repository.UserRoleMapper;
import com.chason.bossdemo.domain.rbac.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:05
 * @Description:
 */
@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService {
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

    @Override
    public boolean batchInsert(String userUuid, List<String> roleUuidList) throws Exception {
        if (StringUtils.isEmpty(userUuid) || CollectionUtils.isEmpty(roleUuidList)) {
            return false;
        }

        List<UserRole> userRoleList = new ArrayList<>();
        for (String roleUuid : roleUuidList) {
            UserRole userRole = new UserRole();
            userRole.setUuid(UuidUtil.randomUUID());
            userRole.setUserUuid(userUuid);
            userRole.setRoleUuid(roleUuid);
            userRole.setStatus(StatusType.STATUS_OK);
            userRole.setCreateTime(new Date());
            userRole.setUpdateTime(new Date());
            userRoleList.add(userRole);
        }

        return userRoleMapper.batchInsert(userRoleList) > 0;
    }

    @Override
    public boolean delByUserUuid(String userUuid) throws Exception {
        if (StringUtils.isEmpty(userUuid)) {
            return false;
        }
        UserRoleExample example = new UserRoleExample();
        UserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andUserUuidEqualTo(userUuid);
        return userRoleMapper.deleteByExample(example) > 0;
    }
}
