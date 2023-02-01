package com.ljw.client.util;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
@Component
public class SysUtil {

    private static RpcProxy rpcProxy;

    @Autowired
    public SysUtil(RpcProxy rpcProxy){
        this.rpcProxy=rpcProxy;
    }
    /*
   @Autowired
   public void setRpcProxy(RpcProxy rpcProxy){
       this.rpcProxy=rpcProxy;
   }*/

    public static <T> T getRemoteInstance(Class<T> intfClass){
        //return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{intfClass}, new RpcProxy());
        //return (T) new RpcFactoryBean(intfClass).getObject();
        return (T) Proxy.newProxyInstance(intfClass.getClassLoader(), new Class<?>[]{intfClass}, rpcProxy);

    }

}
