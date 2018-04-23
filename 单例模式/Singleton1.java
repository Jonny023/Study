package com.study;

/**
 *  饿汉模式
 * @author Lee
 * @Date 2018/4/23 10:50
 */
public class Singleton1 {

    private Singleton1() {}

    private static Singleton1 instance = new Singleton1();

    public static Singleton1 getInstance() {
        return instance;
    }

}
