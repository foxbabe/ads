package com.sztouyun.advertisingsystem.common.verificationCode;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DefaultVerificationCodeGenerator implements IVerificationCodeGenerator {
    @Value("${code.generator.seed}")
    private String seed;

    @Override
    public String generate(int length) {
        if(length <= 0)
            throw new BusinessException("验证码长度必须大于0");

        char[] seedArray = seed.toCharArray();
        int seedLength =seedArray.length;
        char[] result = new char[length];
        Random random=new Random();
        for (int i =0;i<length;i++){
            int index = random.nextInt(length) % seedLength;
            result[i] =seedArray[index];
        }
        return String.valueOf(result);
    }
}
