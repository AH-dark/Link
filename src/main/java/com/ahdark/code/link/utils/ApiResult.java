package com.ahdark.code.link.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.List;

import static com.ahdark.code.link.utils.CodeResult.SUCCESS;

/**
 * API Result JSON Message
 *
 * @author AH-dark
 */
public class ApiResult implements Serializable {
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
    private Object ObjectData;
    private List<Object> ListData;

    /**
     * 异常
     */
    private String Exceptions;

    public ApiResult() {
        super();
    }

    public ApiResult(CodeResult codeResult) {
        super();
        this.Code = codeResult.getCode();
        this.Message = codeResult.getMsg();
    }

    public ApiResult(CodeResult codeResult, JSONObject data) {
        super();
        this.Code = codeResult.getCode();
        this.Message = codeResult.getMsg();
        this.ObjectData = data;
    }

    public ApiResult(CodeResult codeResult, JSONArray data) {
        super();
        this.Code = codeResult.getCode();
        this.Message = codeResult.getMsg();
        this.ListData = data;
    }

    public ApiResult(CodeResult codeResult, JSONObject data, String exceptions) {
        super();
        this.Code = codeResult.getCode();
        this.Message = codeResult.getMsg();
        this.ObjectData = data;
        this.Exceptions = exceptions;
    }

    public ApiResult(CodeResult codeResult, JSONArray data, String exceptions) {
        super();
        this.Code = codeResult.getCode();
        this.Message = codeResult.getMsg();
        this.ListData = data;
        this.Exceptions = exceptions;
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

    public void setData(Object objectData) {
        this.ObjectData = objectData;
    }

    public void setData(List<Object> listData) {
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
        if (this.Exceptions != null) {
            json.put("exceptions", this.Exceptions);
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
