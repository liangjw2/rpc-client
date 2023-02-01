package com.ljw.client.vo;

import lombok.Data;

//@Data
public class RpcResponse {
    /**
     * 响应对应的请求ID
     */
    private String requestId;

    public String getRequestId() {
        return requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Object getResult() {
        return result;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * 调用是否成功的标识
     */
    private boolean success = true;
    /**
     * 调用错误信息
     */
    private String errorMessage;
    /**
     * 调用结果
     */
    private Object result;
}
