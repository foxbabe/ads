package com.sztouyun.advertisingsystem.utils;

import java.util.*;

/**
 * Created by wenfeng on 2017/10/16.
 */
public class MapUtil {
    /**
     * 0升序排列，1倒序排列
     */
    public final static Integer ASC=0;
    public final static Integer DESC=1;

    public static Map<String, Integer> sortMapByValue(Map<String, Integer> oriMap,Integer sort) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator(sort));

        Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
        Map.Entry<String, Integer> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    static class  MapValueComparator implements Comparator<Map.Entry<String, Integer>> {
        private Integer sort;
        public MapValueComparator(Integer sort){
            this.sort=sort;
        }
        @Override
        public int compare(Map.Entry<String, Integer> me1, Map.Entry<String, Integer> me2) {
            if(sort==0){
                return me1.getValue().compareTo(me2.getValue());
            }else{
                return me2.getValue().compareTo(me1.getValue());
            }

        }
    }


    /**
     * 获取最大值或最小值
     * @param oriMap
     * @param sort
     * @return
     */
    public static Map<String, Object> getFirstPairByValue(Map<String, Integer> oriMap,Integer sort){
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator(sort));

        Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
        Map.Entry<String, Integer> tmpEntry = null;
        if (iter.hasNext()) {
            tmpEntry = iter.next();
            resultMap.put("key",tmpEntry.getKey());
            resultMap.put("value",tmpEntry.getValue());
        }
        return resultMap;
    }

    public static String getKeys(Map map){
        if(map==null || map.isEmpty())
            return "";
        String temp=map.keySet().toString();
        return temp.substring(1,temp.length()-1);
    }

    public static <T>  void putOrIncreaseValue(Map<T,Long> map,T key){
        if(map.keySet().contains(key)){
            Long oldValue=map.get(key);
            map.put(key,oldValue+1);
        }else{
            map.put(key,1L);
        }
    }

    public static <T>  void putOrIncrease(Map<T,Integer> map,T key){
        if(map.keySet().contains(key)){
            Integer oldValue=map.get(key);
            map.put(key,oldValue+1);
        }else{
            map.put(key,1);
        }
    }
}
