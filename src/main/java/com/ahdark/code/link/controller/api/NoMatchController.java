package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.utils.ApiResult;
import com.ahdark.code.link.utils.CodeInfo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/*", produces = {MediaType.APPLICATION_JSON_VALUE})
public class NoMatchController {
    @RequestMapping("")
    public JSONObject NotFound() {
        ApiResult<Object> apiResult = new ApiResult<>(CodeInfo.NO_DATA);
        apiResult.setExceptions("route no match");
        return apiResult.getJsonResult();
    }
}
