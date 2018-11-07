package com.sztouyun.advertisingsystem.api.system;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.system.Menu;
import com.sztouyun.advertisingsystem.service.system.MenuService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.system.CreateMenuViewModel;
import com.sztouyun.advertisingsystem.viewmodel.system.MenuListItemViewModel;
import com.sztouyun.advertisingsystem.viewmodel.system.MenuPageInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.system.MenuViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by szty on 2017/7/25.
 */
@Api("菜单接口")
@RestController
@RequestMapping("/api/menu")
public class MenuApiController extends BaseApiController {

    @Autowired
    private MenuService menuService;

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "详情查询", notes = "创建人：陈化静")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public InvokeResult<MenuViewModel> getMenu(@PathVariable String id) {
        if(StringUtils.isEmpty(id))
            throw new BusinessException("菜单ID不能为空！");

        MenuViewModel menuViewModel = new MenuViewModel();
        BeanUtils.copyProperties(menuService.getMenu(id), menuViewModel);
        return InvokeResult.SuccessResult(menuViewModel);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "查询所有", notes = "创建人：陈化静")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public InvokeResult<List<MenuViewModel>> getAllMenu(){
        List<Menu> menus = menuService.getAllMenu();
        List<MenuViewModel> rootTreeList = ApiBeanUtils.convertToTreeList(menus, menu -> ApiBeanUtils.copyProperties(menu,MenuViewModel.class), Menu.ROOT_PARENT_ID);
        return InvokeResult.SuccessResult(rootTreeList);

    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "分页查询", notes = "创建人：陈化静")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public InvokeResult<PageList<MenuListItemViewModel>> getMenuByPage(@Validated @RequestBody MenuPageInfoViewModel menuPageInfo, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(menuPageInfo.getPageIndex(), menuPageInfo.getPageSize());
        Page<Menu> pages = menuService.findByPage(pageable);
        List<Menu> menus = menuService.getAllMenu();
        PageList<MenuListItemViewModel> resultList = ApiBeanUtils.convertToPageList(pages, menu -> {
            MenuListItemViewModel menuListItemViewModel = new MenuListItemViewModel();
            //设置父级菜单名称
            if(StringUtils.isEmpty(menu.getParentId()) || Menu.ROOT_PARENT_ID.equals(menu.getParentId())){
                menuListItemViewModel.setParentMenu("");
                menuListItemViewModel.setRootMenu(true);
            }else{
                Menu patentMenu = Linq4j.asEnumerable(menus).firstOrDefault(pMenu->pMenu.getId().equals(menu.getParentId()));
                menuListItemViewModel.setParentMenu(patentMenu==null ? "":patentMenu.getMenuName());
                menuListItemViewModel.setRootMenu(false);
            }
            menuListItemViewModel.setCreatorName(getUserNickname(menu.getCreatorId()));
            BeanUtils.copyProperties(menu, menuListItemViewModel);
            return menuListItemViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "添加", notes = "创建人：陈化静")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public InvokeResult createMenu(@Validated @RequestBody CreateMenuViewModel createMenuViewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Menu menu = new Menu();
        BeanUtils.copyProperties(createMenuViewModel, menu);
        menuService.createMenu(menu);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "修改", notes = "创建人：陈化静")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public InvokeResult updateMenu(@Validated @RequestBody MenuViewModel updateMenuViewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);

        Menu menu = menuService.getMenu(updateMenuViewModel.getId());
        BeanUtils.copyProperties(updateMenuViewModel, menu);
        menuService.updateMenu(menu);
        return InvokeResult.SuccessResult();
    }


    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "删除", notes = "创建人：陈化静")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public InvokeResult deleteMenu(@PathVariable String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        menuService.deleteMenu(id);
        return InvokeResult.SuccessResult();
    }

}
