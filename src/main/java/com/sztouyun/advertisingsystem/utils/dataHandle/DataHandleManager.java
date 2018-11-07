package com.sztouyun.advertisingsystem.utils.dataHandle;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by fengwen on 22/08/2018.
 */
@Data
public class DataHandleManager<IN extends DataInput,OUT extends DataOutput>{
    private final Logger logger = LoggerFactory.getLogger(getClass());
        public OUT handle(IN in){
            try {
                Class<?> clazz=in.getOperationType().getHandler();
                return invoke(clazz.getMethod(in.getOperationType().getTargetMethod(),in.getClass()),clazz.newInstance(),in);
            } catch (BusinessException be){
                throw new BusinessException(be.getMessage());
            }catch (Exception e) {
                logger.error("数据处理异常",e);
                throw new BusinessException("数据处理异常");
            }
        }

        private OUT invoke(Method method,Object object, IN in){
            try {
                return (OUT) method.invoke(object,in);
            }catch (InvocationTargetException be){
                Throwable targetException = be.getTargetException();
                throw new BusinessException(targetException.getMessage());
            } catch (Exception e) {
                logger.error("数据处理异常",e);
                throw new BusinessException("数据处理异常");
            }
        }
}
