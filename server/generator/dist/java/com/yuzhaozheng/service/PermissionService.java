package com.yuzhaozheng.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuzhaozheng.entity.Permission;
import com.yuzhaozheng.mapper.PermissionMapper;
import com.yuzhaozheng.query.PermissionQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>权限 服务实现类</p>
 *
 * @author yuzhaozheng
 * @since 2020-05-17
 */
@Service
@Transactional
public class PermissionService extends ServiceImpl<PermissionMapper, Permission> {

    public IPage<Permission> page(PermissionQuery query) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        if (query.getPermissionId() != null) {
            wrapper.eq("permission_id", query.getPermissionId());
        }
        if (query.getParentId() != null) {
            wrapper.eq("parent_id", query.getParentId());
        }
        if (StringUtils.isNotBlank(query.getName())) {
            wrapper.like("name", query.getName());
        }
        if (query.getMenu() != null) {
            wrapper.eq("menu", query.getMenu());
        }
        if (StringUtils.isNotBlank(query.getUrl())) {
            wrapper.like("url", query.getUrl());
        }
        if (StringUtils.isNotBlank(query.getIcon())) {
            wrapper.like("icon", query.getIcon());
        }
        if (query.getSort() != null) {
            wrapper.eq("sort", query.getSort());
        }
        return super.page(query, wrapper);
    }

    @Override
    public boolean updateById(Permission permission) {
        Permission dbPermission = getById(permission.getPermissionId());
        dbPermission.setParentId(permission.getParentId());
        dbPermission.setName(permission.getName());
        dbPermission.setMenu(permission.getMenu());
        dbPermission.setUrl(permission.getUrl());
        dbPermission.setIcon(permission.getIcon());
        dbPermission.setSort(permission.getSort());
        return super.updateById(permission);
    }
}

