package com.example.daily.network;

/**
 * ProjectName: Daily_accumulation
 * Package: com.example.daily.network
 * ClassName: BaseResponse
 * Author: zlq
 * CreateDate: 2019-09-05 11:18
 * Version: 1.0
 * Desc: 基类
 **/
public class BaseResponse<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
