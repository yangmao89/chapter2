package org.smart4j.chapter2.util;

/**
 * Created by CPR014 on 2017-04-12.
 */
public class CastUtil {
    public static String castString(Object obj, String defaultValue){
        return obj == null ? defaultValue : obj.toString();
    }

    public static String castString(Object obj){
        return castString(obj, "");
    }

    public static double castDouble(Object obj, double defaultValue){
        double doubleValue = defaultValue;
        if(obj != null){
            String strValue = obj.toString();
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    doubleValue = Double.parseDouble(strValue);
                } catch (NumberFormatException e){

                }
            }
        }
        return doubleValue;
    }

    public static double castDouble(Object obj){
        return castDouble(obj, 0);
    }

    public static long castLong(Object obj, long defaultValue){
        long longValue = defaultValue;
        if(obj != null){
            String strValue = obj.toString();
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    longValue = Long.parseLong(strValue);
                } catch (NumberFormatException e){

                }
            }
        }
        return longValue;
    }

    public static long castLong(Object obj){
        return castLong(obj, 0);
    }

    public static int castInt(Object obj, int defaultValue){
        int intValue = defaultValue;
        if(obj != null){
            String strValue = obj.toString();
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    intValue = Integer.parseInt(strValue);
                } catch (NumberFormatException e){

                }
            }
        }
        return intValue;
    }

    public static int castInt(Object obj){
        return castInt(obj, 0);
    }

    public static boolean castBoolean(Object obj, boolean defaultValue){
        boolean boolValue = defaultValue;
        if(obj != null){
            boolValue = Boolean.parseBoolean(castString(obj));
        }
        return boolValue;
    }

    public static boolean castBoolean(Object obj){
        return castBoolean(obj, false);
    }

    public static void main(String args[]){
        System.out.println(castBoolean("1"));
        System.out.println(Boolean.parseBoolean("0"));
    }
}
