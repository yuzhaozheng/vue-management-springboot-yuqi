package com.yuzhaozheng.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuzhaozheng.entity.Role;
import com.yuzhaozheng.mapper.RoleMapper;
import com.yuzhaozheng.query.RoleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>角色 服务实现类</p>
 *
 * @author yuzhaozheng
 * @since 2020-05-17
 */
@Service
@Transactional
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    public IPage<Role> page(RoleQuery query) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if (query.getRoleId() != null) {
            wrapper.eq("role_id", query.getRoleId());
        }
        if (StringUtils.isNotBlank(query.getName())) {
            wrapper.like("name", query.getName());
        }
        if (query.getSort() != null) {
            wrapper.eq("sort", query.getSort());
        }
        if (StringUtils.isNotBlank(query.getRemark())) {
            wrapper.like("remark", query.getRemark());
        }
        return super.page(query, wrapper);
    }

    @Override
    public boolean updateById(Role role) {
        Role dbRole = getById(role.getRoleId());
        dbRole.setName(role.getName());
        dbRole.setSort(role.getSort());
        dbRole.setRemark(role.getRemark());
        return super.updateById(role);
    }
}

