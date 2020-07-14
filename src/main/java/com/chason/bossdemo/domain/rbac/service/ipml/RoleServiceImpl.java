package com.chason.bossdemo.domain.rbac.service.ipml;

import com.chason.bossdemo.domain.rbac.entity.Role;
import com.chason.bossdemo.domain.rbac.entity.RoleExample;
import com.chason.bossdemo.domain.rbac.repository.RoleMapper;
import com.chason.bossdemo.domain.rbac.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Role> getAll() {
        RoleExample example = new RoleExample();
        example.setOrderByClause("IF(ISNULL(sort_num),1,0),sort_num");
        return roleMapper.selectByExample(example);
    }
}
