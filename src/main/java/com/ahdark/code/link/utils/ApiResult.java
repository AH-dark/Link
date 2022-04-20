package com.ahdark.code.link.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.io.Serializable;

import static com.ahdark.code.link.utils.CodeInfo.SUCCESS;

/**
 * API Result JSON Message
 *
 * @author AH-dark
 */
public class ApiResult<T> implements Serializable {
    /**
     * 返回码
     */
    private int Code = SUCCESS.getCode(); // 返回码

    /**
     * 消息
     */
    private String Message = SUCCESS.getMsg();
    /**
     * 数据
     */
    private T Data;

    /**
     * 异常
     */
    private String Exceptions;

    public ApiResult() {
        super();
    }

    public ApiResult(CodeInfo codeInfo) {
        super();
        this.Code = codeInfo.getCode();
        this.Message = codeInfo.getMsg();
    }

    public ApiResult(CodeInfo codeInfo, T data) {
        super();
        this.Code = codeInfo.getCode();
        this.Message = codeInfo.getMsg();
        this.Data = data;
    }

    public ApiResult(CodeInfo codeInfo, T data, String exceptions) {
        super();
        this.Code = codeInfo.getCode();
        this.Message = codeInfo.getMsg();
        this.Data = data;
        this.Exceptions = exceptions;
    }

    public ApiResult(T data) {
        super();
        this.Data = data;
    }

    public void setCode(int code) {
        this.Code = code;
    }

    public void setCode(String code) {
        this.Code = Integer.parseInt(code);
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public void setData(T data) {
        this.Data = data;
    }

    public void setExceptions(String exceptions) {
        this.Exceptions = exceptions;
    }

    public JSONObject getJsonResult() {
        JSONObject json = new JSONObject();
        json.put("code", this.Code);
        json.put("message", this.Message);
        if (this.Data != null) {
            Gson gson = new Gson();
            json.put("data", JSON.parse(gson.toJson(this.Data)));
        }
        if (this.Exceptions != null) {
            json.put("exceptions", this.Exceptions);
        }
        return json;
    }

    @Override
    public String toString() {
        if (this.Data != null) {
            return "ApiResult [code=" + this.Code + ", info=" + this.Message + ", data=" + this.Data + ", exceptions=" + this.Exceptions + "]";
        } else {
            return "ApiResult [code=" + this.Code + ", info=" + this.Message + "]";
        }
    }
}
