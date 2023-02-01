package com.ljw.client.vo;


import lombok.Data;

//@Data
public class RpcRequest {
    /**
     * 请求ID 用来标识本次请求以匹配RPC服务器的响应
     */
    private String requestId;
    /**
     * 调用的类(接口)权限定名称
     */
    private String className;

    public String getRequestId() {
        return requestId;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    /**
     * 调用的方法名
     */
    private String methodName;
    /**
     * 方法参类型列表
     */
    private Class<?>[] parameterTypes;
    /**
     * 方法参数
     */
    private Object[] parameters;

}
