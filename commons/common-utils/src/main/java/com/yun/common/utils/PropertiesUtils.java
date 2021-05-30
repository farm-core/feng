package com.yun.common.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author wxf
 * @date 2018/7/31 11:49
 */
public class PropertiesUtils {
    private Properties property;

    private PropertiesUtils() {
        property = new Properties();
        try {
            property.load(this.getClass().getClassLoader().getResourceAsStream("element.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static PropertiesUtils configManager = null;

    public static PropertiesUtils getInstance() {
        if (configManager == null) {
            configManager = new PropertiesUtils();
        }
        return configManager;
    }

    public String GetProperty(String key) {
        return property.getProperty(key);
    }
   /* public static void main(String[] args){
        String element = "TEMP";
        String str = element+"Str";
        String clr = element+"Clr";
        String[] StrArray = PropertiesUtil.getInstance().GetProperty(str).split(",");
        String[] ClrArray = PropertiesUtil.getInstance().GetProperty(clr).split(",");
        System.out.println(StrArray.length);
        System.out.println(ClrArray.length);
    }*/
}

