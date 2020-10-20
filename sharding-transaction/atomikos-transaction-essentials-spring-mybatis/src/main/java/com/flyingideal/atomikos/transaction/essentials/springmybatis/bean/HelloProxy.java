package com.flyingideal.atomikos.transaction.essentials.springmybatis.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author yanchao
 * @date 2020-07-15 14:17
 */
public class HelloProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Hello World~");
        return null;
    }

    public static IHello getProxyInstance() {
        return (IHello) Proxy.newProxyInstance(IHello.class.getClassLoader(), new Class[]{IHello.class}, new HelloProxy());
    }
}
