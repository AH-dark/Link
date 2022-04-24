package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.pojo.SiteConfig;
import com.ahdark.code.link.service.SiteConfigService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.ahdark.code.link.utils.CodeInfo.NO_DATA;

@RestController
@RequestMapping(path = "/api/siteConfig", produces = MediaType.APPLICATION_JSON_VALUE)
public class SiteConfigController {
    @Autowired
    SiteConfigService siteConfigService;

    @GetMapping("")
    public JSONObject Get() {
        Map<String, Object> configs = siteConfigService.get();
        return new ApiResult<>(configs).getJsonResult();
    }

    @GetMapping(value = "", params = {"name"})
    public JSONObject Get(@RequestParam("name") String name) {
        Object result = siteConfigService.get(name);
        if (result == null) {
            return new ApiResult<>(NO_DATA).getJsonResult();
        }
        return new ApiResult<>(result).getJsonResult();
    }
}
