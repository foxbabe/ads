package com.sztouyun.advertisingsystem.service.system;


import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.system.Permission;
import com.sztouyun.advertisingsystem.model.system.QMenu;
import com.sztouyun.advertisingsystem.model.system.QPermission;
import com.sztouyun.advertisingsystem.repository.system.MenuRepository;
import com.sztouyun.advertisingsystem.repository.system.PermissionRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by szty on 2017/7/28.
 */
@Service
@CacheConfig(cacheNames = "permissions")
public class PermissionService extends BaseService{

    private static final String ROOT_PERMISSION_ID = "0";//表示顶级权限
    private static final String AllPermissionApiUrlsKey ="'AllPermissionApiUrls'";
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MenuRepository menuRepository;

    private final QPermission qPermission = QPermission.permission;

    private final QMenu qMenu = QMenu.menu;

    @CacheEvict(key = AllPermissionApiUrlsKey)
    @Transactional
    public String createPermission(Permission permission){
        //权限名称、url不能重复
        boolean isExists = permissionRepository.exists(qPermission.permissionName.eq(permission.getPermissionName()).and(qPermission.menuId.eq(permission.getMenuId())));
        if(isExists)
            throw new BusinessException("权限已存在");

        if(!menuRepository.exists(qMenu.id.eq(permission.getMenuId())) )
            throw new BusinessException("菜单不存在");

        permissionRepository.save(permission);
        return permission.getId();
    }


    @Caching(evict = {@CacheEvict(value = "roles",allEntries = true), @CacheEvict(key = AllPermissionApiUrlsKey)})
    @Transactional
    public void updatePermission(Permission permission){
        boolean isExists = permissionRepository.exists(qPermission.permissionName.eq(permission.getPermissionName()).and(qPermission.menuId.eq(permission.getMenuId())).and(qPermission.id.ne(permission.getId())));
        if(isExists)
            throw new BusinessException("权限已存在");

        if(!menuRepository.exists(qMenu.id.eq(permission.getMenuId())) )
            throw new BusinessException("菜单不存在");

        permissionRepository.save(permission);
    }

    @Caching(evict = {@CacheEvict(value = "roles",allEntries = true), @CacheEvict(key = AllPermissionApiUrlsKey)})
    @Transactional
    public void deletePermission(String id){
        boolean isExists = permissionRepository.exists(qPermission.id.eq(id));
        if(!isExists)
            throw new BusinessException("权限不存在");
        permissionRepository.deleteById(id);
    }

    public List<Permission> getAllPermission(){
        return permissionRepository.findAll(q->q.selectFrom(qPermission).orderBy(qPermission.sortNumber.asc()));
    }

    @Cacheable(key =AllPermissionApiUrlsKey)
    public List<String> getAllPermissionApiUrlsFromCache(){
        return permissionRepository.findAll(q->q.select(qPermission.apiUrl).from(qPermission));
    }

    public Permission getPermission(String id){
        if(StringUtils.isEmpty(id))
            throw new BusinessException("权限ID不能为空！");

        Permission permission= permissionRepository.findOne(qPermission.id.eq(id));
        if(null == permission)
            throw new BusinessException("权限不存在！");
        return permission;
    }

    public Page<Permission> findByPage(Pageable pageable){
        return permissionRepository.findAll(new BooleanBuilder(), pageable, new JoinDescriptor().innerJoin(qPermission.menu));
    }
}
