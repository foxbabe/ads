package com.sztouyun.advertisingsystem.utils;

import com.sztouyun.advertisingsystem.common.IAreaTreeList;
import com.sztouyun.advertisingsystem.common.ITree;
import com.sztouyun.advertisingsystem.common.ITreeList;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ApiBeanUtils {

    public static <S,T> PageList<T> convertToPageList(Page<S> pages,Function<S, T> mapper){
        PageList<T> pageList=new PageList<>();
        pageList.setTotalElement(pages.getTotalElements());
        pageList.setTotalPageSize(pages.getTotalPages());
        pageList.setPageIndex(pages.getNumber());
        pageList.setPageSize(pages.getSize());
        if(mapper!=null){
            pageList.setList(Linq4j.asEnumerable(pages.getContent()).select(mapper::apply).toList());
        }else{
            pageList.setList((List<T>) pages.getContent());
        }

        return pageList;
    }

    public static <S,T> PageList<T> convertToPageList(Page<S> pages){
       return convertToPageList(pages,null);
    }

    //三层树, 去除直辖市中间节点
    public static <S extends ITree<V>,T extends IAreaTreeList<T>,V> List<T> convertToAreaTreeList(Iterable<S> dataList, Function<S, T> mapper, V rootId){
        List<T> areas = convertToTreeList(dataList,mapper,rootId);
        return Linq4j.asEnumerable(areas).select(a->{
            if(a.getChildren() != null && a.getChildren().size() > 0 && SpringUtil.getBean(AreaService.class).isMunicipalityCode(a.getCode()))
                return a.getChildren().get(0);
            return a;
        }).toList();
    }

    //四层树, 去除直辖市中间节点, 顶层的节点名字为: "全部"
    public static <S extends ITree<V>,T extends IAreaTreeList<T>,V> List<T> convertToAreaTreeListWithRootNode(Iterable<S> dataList, Function<S, T> mapper, V rootId){
        List<T> areas = convertToTreeList(dataList,mapper,rootId);
        Linq4j.asEnumerable(areas).forEach(area -> {
            Linq4j.asEnumerable(area.getChildren()).forEach(children -> {
                if(children.getChildren() != null && children.getChildren().size()>0 && SpringUtil.getBean(AreaService.class).isMunicipalityCode(children.getCode())) {
                    children.setChildren(children.getChildren().get(0).getChildren());
                }
            });
        });
        return areas;
    }


    public static <S extends ITree<V>,T extends ITreeList<T>,V> List<T> convertToTreeList(Iterable<S> dataList, Function<S, T> mapper, V rootId){
        if(dataList ==null)
            return new ArrayList<>();

        Enumerable<S> list=  Linq4j.asEnumerable(dataList);
        List<T> children = getChildren(list,rootId,mapper);
        S rootItem = list.firstOrDefault(l->l.getId().equals(rootId));
        if(rootItem == null)
            return children;

        T targetItem = mapper.apply(rootItem);
        targetItem.setChildren(children);
        return Collections.singletonList(targetItem);
    }

    public static <S,T> T copyProperties(S source ,Class<T> targetClass){
        try {
            if(source ==null)
                return null;

            T target =targetClass.newInstance();
            BeanUtils.copyProperties(source,target);
            return target;
        }catch (Exception ex){
            return null;
        }
    }

    private static <S extends ITree<V>,T extends ITreeList<T>,V> List<T> getChildren(Enumerable<S> list,V parenId,Function<S, T> mapper){
     return list.where(l-> parenId.equals( l.getParentId())).select(item->{
            T viewModel =mapper.apply(item);
            List<T> children =getChildren(list,item.getId(),mapper);
             if (children != null && children.size() >0) {
                 viewModel.setChildren(children);
             }
            return viewModel;
        }).toList();
    }

    public static <T,R> List<R> toList(List<T> data, Function<T, R> mapFunc) {
        return data.stream().map(mapFunc).collect(Collectors.toList());
    }
}
