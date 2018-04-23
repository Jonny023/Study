package com.study;

/**
 *  通过枚举实现单例
 * @author Lee
 * @Date 2018/4/23 10:45
 * return
 */
public enum SingleInstance {

    INSTANCE;

    public String hello() {
        return "hello world";
    }

    public static void main(String[] args) {
        //调用方法
        String s = SingleInstance.INSTANCE.hello();
        System.out.println(s);
    }
}
