package com.chason.bossdemo.domain.rbac.repository;

import com.chason.bossdemo.domain.rbac.entity.BossUser;
import com.chason.bossdemo.domain.rbac.entity.BossUserExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BossUserMapper {
    int countByExample(BossUserExample example);

    int deleteByExample(BossUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BossUser record);

    int insertSelective(BossUser record);

    List<BossUser> selectByExample(BossUserExample example);

    BossUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BossUser record, @Param("example") BossUserExample example);

    int updateByExample(@Param("record") BossUser record, @Param("example") BossUserExample example);

    int updateByPrimaryKeySelective(BossUser record);

    int updateByPrimaryKey(BossUser record);
}