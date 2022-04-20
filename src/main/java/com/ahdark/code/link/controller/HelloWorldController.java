package com.ahdark.code.link.controller;

import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ahdark.code.link.utils.CodeInfo.SUCCESS;

@RestController
@RequestMapping(path = "/api/helloWorld")
public class HelloWorldController {
    @RequestMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject Index() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state", "hello world");
        return new ApiResult<>(SUCCESS, jsonObject).getJsonResult();
    }
}
