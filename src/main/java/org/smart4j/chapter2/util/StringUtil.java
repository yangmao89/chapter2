package org.smart4j.chapter2.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by CPR014 on 2017-04-11.
 */
public class StringUtil {

    public static boolean isEmpty(String str){
        if(str != null){
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    public static  boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static void main(String args[]){
        System.out.println(StringUtils.isEmpty(" "));
    }
}
