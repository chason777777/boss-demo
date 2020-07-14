package com.chason.bossdemo.domain.rbac.service;

import com.chason.bossdemo.domain.rbac.entity.Role;

import java.util.List;

/**
 * @Author: chason
 * @Date: 2020/7/9 20:03
 * @Description:
 */
public interface RoleService {
    /**
     * 查询所有角色
     * @return
     */
    List<Role> getAll();
 }
