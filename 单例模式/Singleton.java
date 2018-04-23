package com.study;

/**
 * 静态内部类
 * @Author Lee
 * @Date 2018/4/23 11:58
 */
public class Singleton {

    private static class SingletonLoader {
        private static final Singleton INSTANCE = new Singleton();
    }

    private Singleton() {}

    public static final Singleton getInstance() {
        return SingletonLoader.INSTANCE;
    }
}
