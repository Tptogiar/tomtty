package com.tptogiar.util;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 10:17:00
 */
public class ReflectUtil {


    public static Class getClassForName(String className) throws ClassNotFoundException {

        Class<?> clazz = Class.forName(className);

        return clazz;
    }









}
