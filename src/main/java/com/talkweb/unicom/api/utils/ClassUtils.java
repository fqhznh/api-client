package com.talkweb.unicom.api.utils;

public class ClassUtils {

    public static boolean exists(String className) {
        try {
            System.out.println(Class.forName(className));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
