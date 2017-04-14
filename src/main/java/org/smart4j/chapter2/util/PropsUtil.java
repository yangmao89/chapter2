package org.smart4j.chapter2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by CPR014 on 2017-04-11.
 */
public class PropsUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PropsUtil.class);

    public static Properties loadProps(String fileName){
        Properties props = null;
        InputStream is = null;

        try{
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(is == null){
                throw new FileNotFoundException(fileName + " not exists!");
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            LOG.error("load properties file failure", e);
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("inputstream close failure", e);
                }
            }
        }
        return props;
    }

    public static String getString(Properties props, String key, String defaultValue){
        return props.getProperty(key, defaultValue);
    }

    public static String getString(Properties props, String key){
        return getString(props, key, "");
    }

    public static int getInt(Properties props, String key, int defalutValue){
        int intValue = defalutValue;
        if(props.containsKey(key)){
            intValue = CastUtil.castInt(props.getProperty(key));
        }
        return intValue;
    }

    public static int getInt(Properties props, String key){
        return getInt(props, key, 0);
    }

    public static boolean getBoolean(Properties props, String key, boolean defaultValue){
        boolean boolValue = defaultValue;
        if(props.containsKey(key)){
            boolValue = CastUtil.castBoolean(props.getProperty(key));
        }
        return boolValue;
    }

    public static boolean getBoolean(Properties props, String key){
        return getBoolean(props, key, false);
    }

    public static void main(String args[]){
        Properties props = loadProps("config.properties");
        System.out.println(getBoolean(props, "jdbc.url1"));
    }
}
