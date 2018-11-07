package com.sztouyun.advertisingsystem.service.system;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.account.Role;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.system.Menu;
import com.sztouyun.advertisingsystem.model.system.QMenu;
import com.sztouyun.advertisingsystem.repository.system.MenuRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by szty on 2017/7/25.
 */
@Service
public class MenuService extends BaseService {

    private static final String ROOT_MENU_ID = "0";//表示顶级菜单
    @Autowired
    private MenuRepository menuRepository;

    private final QMenu qMenu = QMenu.menu;

    public Menu getMenu(String id){
        Menu menu= menuRepository.findOne(qMenu.id.eq(id));
        if(null == menu)
            throw new BusinessException("菜单id不存在");
        return menu;
    }

    @Transactional
    public void createMenu(Menu menu){
        if (!ROOT_MENU_ID.equals(menu.getParentId())){
            boolean isExistsParentId = menuRepository.exists(qMenu.id.eq(menu.getParentId()) );
            if(!isExistsParentId)
                throw new BusinessException("父级菜单不存在");
        }
        boolean isExistsName = menuRepository.exists(qMenu.menuName.eq(menu.getMenuName()));
        if(isExistsName)
            throw new BusinessException("菜单名称已存在");

        boolean isExistsUrl = menuRepository.exists(qMenu.url.eq(menu.getUrl()));
        if(isExistsUrl)
            throw new BusinessException("菜单url已存在");

        menuRepository.save(menu);
    }

    @Transactional
    public void updateMenu(Menu menu){
        if (!ROOT_MENU_ID.equals(menu.getParentId())){
            boolean isExistsParentId = menuRepository.exists(qMenu.id.eq(menu.getParentId()) );
            if(!isExistsParentId)
                throw new BusinessException("父级菜单不存在");
        }
        //菜单url在同级不能重复
        boolean isExistsUrl = menuRepository.exists(qMenu.url.eq(menu.getUrl()).and(qMenu.id.eq(menu.getId()).not()) );
        if(isExistsUrl)
            throw new BusinessException("菜单url已存在");

        boolean isExists = menuRepository.exists(qMenu.menuName.eq(menu.getMenuName()).and(qMenu.id.eq(menu.getId()).not()));
        if (isExists)
            throw new BusinessException("菜单名称已存在");

        menu.setUpdatedTime(new Date());
        menuRepository.save(menu);
    }

    @Transactional
    public void deleteMenu(String id){
        if(!menuRepository.exists(qMenu.id.eq(id)))
            throw new BusinessException("菜单不存在");

        boolean isExistsParentId = menuRepository.exists(qMenu.parentId.eq(id));
        if(isExistsParentId)
            throw new BusinessException("请先删除该项的子菜单");

        menuRepository.deleteById(id);
    }

    public List<Menu> getAllMenu(){
        List<Menu> menus = menuRepository.findAllByOrderBySortNumberAsc();
        return menus;
    }

    public Page<Menu> findByPage(Pageable pageable){
        Page<Menu> pages= menuRepository.findAll(pageable);
        return  pages;
    }
}
