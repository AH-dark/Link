package com.ahdark.code.link.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * API Result JSON Message
 *
 * @author AH-dark
 */
public class ApiResult implements Serializable {
    /**
     * 返回码
     */
    private int Code = 0; // 返回码

    /**
     * 消息
     */
    private String Message = "success";
    /**
     * 数据
     */
    private Object ObjectData;
    private List<Object> ListData;

    /**
     * 异常
     */
    private String Exceptions;

    public ApiResult() {
        super();
    }

    public ApiResult(int code, String message) {
        super();
        this.Code = code;
        this.Message = message;
    }

    public ApiResult(int code, String message, JSONObject data) {
        super();
        this.Code = code;
        this.Message = message;
        this.ObjectData = data;
    }

    public ApiResult(int code, String message, JSONArray data) {
        super();
        this.Code = code;
        this.Message = message;
        this.ListData = data;
    }

    public ApiResult(int code, String message, JSONObject data, String exceptions) {
        super();
        this.Code = code;
        this.Message = message;
        this.ObjectData = data;
        this.Exceptions = exceptions;
    }

    public ApiResult(int code, String message, JSONArray data, String exceptions) {
        super();
        this.Code = code;
        this.Message = message;
        this.ListData = data;
        this.Exceptions = exceptions;
    }

    public ApiResult(String message) {
        super();
        this.Message = message;
    }

    public ApiResult(JSONObject data) {
        super();
        this.ObjectData = data;
    }

    public ApiResult(JSONArray data) {
        super();
        this.ListData = data;
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

    public void setObjectData(Object objectData) {
        this.ObjectData = objectData;
    }

    public void setListData(List<Object> listData) {
        ListData = listData;
    }

    public void setExceptions(String exceptions) {
        this.Exceptions = exceptions;
    }

    public JSONObject getJsonResult() {
        JSONObject json = new JSONObject();
        json.put("code", this.Code);
        json.put("message", this.Message);
        if (this.ObjectData != null) {
            json.put("data", this.ObjectData);
        } else if (this.ListData != null) {
            json.put("data", this.ListData);
        }
        return json;
    }

    @Override
    public String toString() {
        if (this.ObjectData != null) {
            return "ApiResult [code=" + this.Code + ", info=" + this.Message + ", data=" + this.ObjectData + ", exceptions=" + this.Exceptions + "]";
        } else if (this.ListData != null) {
            return "ApiResult [code=" + this.Code + ", info=" + this.Message + ", data=" + this.ListData + ", exceptions=" + this.Exceptions + "]";
        } else {
            return "ApiResult [code=" + this.Code + ", info=" + this.Message + "]";
        }
    }
}
