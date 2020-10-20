package com.flyingideal.atomikos.transaction.essentials.springmybatis.bean;

/**
 * @author yanchao
 * @date 2020-07-15 14:21
 */
public class DynamicProxyTest {

    public static void main(String[] args) {
        IHello hello = HelloProxy.getProxyInstance();
        String s = hello.sayHello("fuck");

        System.out.println(s);
    }
}
