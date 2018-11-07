package com.sztouyun.advertisingsystem.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static boolean validateMobile(String mobile,String regex){
        if(org.apache.commons.lang3.StringUtils.isEmpty(mobile))
            return false;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
}
