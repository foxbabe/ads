package com.sztouyun.advertisingsystem.utils.mongo;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.util.Map;

/**
 * Created by szty on 2018/7/24.
 */
public class MongodbUtil {
    private static final Logger logger = LoggerFactory.getLogger(MongodbUtil.class);
    public static  <T extends Number> T  getCount(MongoTemplate mongoTemplate,String countKey,Aggregation aggregation,Class<?> inputType,Class<T> numberClass){
        Map map=mongoTemplate.aggregate(aggregation, inputType,Map.class).getUniqueMappedResult();
        return NumberFormatUtil.getNumber(getStringValue(countKey, map),numberClass);
    }
    public static  <T extends Number> T getCount(MongoTemplate mongoTemplate,Aggregation aggregation,Class<?> inputType ,Class<T> numberClass ){
        return getCount(mongoTemplate,null,aggregation,inputType,numberClass);
    }

    public static <T extends Number> T getCount(MongoTemplate mongoTemplate,String countKey,Aggregation aggregation,String inputTypeCollection,Class<T> numberClass  ){
        Map map=mongoTemplate.aggregate(aggregation, inputTypeCollection,Map.class).getUniqueMappedResult();
        return NumberFormatUtil.getNumber(getStringValue(countKey, map),numberClass);
    }

    private static String getStringValue(String countKey, Map map) {
        if(map==null)
            return "0";
        if(countKey==null){
            countKey="count";
        }
        if(!map.containsKey(countKey)){
            logger.info("aggregate count的key:"+countKey+"找不到");
            throw new BusinessException("查询出异常");
        }
        return map.get(countKey).toString();
    }


    public static <T extends Number> T getCount(MongoTemplate mongoTemplate,Aggregation aggregation,String inputTypeCollection ,Class<T> numberClass ){
        return getCount(mongoTemplate,null,aggregation,inputTypeCollection,numberClass);
    }
}