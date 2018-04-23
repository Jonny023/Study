package com.study;

/**
 * 懒汉模式
 * @Author Lee
 * @Date 2018/4/23 10:59
 */
public class Singleton2 {

    private static Singleton2 instance = null;

    private Singleton2() {}

    public static Singleton2 getInstance() {
        if(instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }
}
