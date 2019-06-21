package com.zrsy.threepig.domain;

/**
 * 前端的返回值都用此类返回
 * @param <T>
 * status为请求的成功与否，0为成功，1为失败。
 * data 为请求返回的数据
 * message 为请求的返回说明信息
 */
public class ParserResult<T> {
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    private int status;
    private T data;
    private String message;

    public ParserResult() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
