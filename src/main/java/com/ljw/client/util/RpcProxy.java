package com.ljw.client.util;

import com.alibaba.fastjson.JSONObject;
import com.ljw.client.vo.RpcRequest;
import com.ljw.client.vo.RpcResponse;
import com.ljw.iobus.SayHelloOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Component
public class RpcProxy implements InvocationHandler {
    @Autowired
    private RpcClient rpcClient;
    private final Logger logger = LoggerFactory.getLogger(RpcProxy.class);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setParameterTypes(method.getParameterTypes());
        logger.info("参数类型:{}", method.getParameterTypes());
        RpcResponse rpcResponse = rpcClient.send(rpcRequest);
        logger.info("返回结果:{}",rpcResponse.getResult());
        Object output;
        //服务端返回的对象结果，经过序列化之后转为json报文
        // 客户端将json报文反序列化之后，rpcResponse.getResult()的对象结果变成了JSONObject，需要按其类型转换为Object
        if(rpcResponse.getResult() instanceof JSONObject) {
            output = JSONObject.toJavaObject((JSONObject) rpcResponse.getResult(), method.getReturnType());
        } else {
            output= rpcResponse.getResult();
        }
        return output;//由于Proxy.newProxyInstance()方法中传入的接口类对象作为代理对象，因此这里的返回应与接口类的方法返回相匹配
    }
}
