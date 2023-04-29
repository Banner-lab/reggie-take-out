package com.leexm.reggie.common;

/**
 * @ClassName BaseContext
 * @Description 封装threadlocal的工具类，保存和获取登录用户的id
 * @Author leexm
 * @Version 1.0
 **/
public class BaseContext {
    private static final ThreadLocal<Long> threadlocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadlocal.set(id);
    }

    public static Long getCurrentId(){
        return threadlocal.get();
    }
}
