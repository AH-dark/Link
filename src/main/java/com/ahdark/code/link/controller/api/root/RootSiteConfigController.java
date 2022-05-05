package com.ahdark.code.link.controller.api.root;

import com.ahdark.code.link.service.SiteConfigService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.ahdark.code.link.utils.CodeInfo.*;

@RestController
@RequestMapping(path = "/api/root/siteConfig")
@Slf4j
public class RootSiteConfigController {
    private final Gson gson = new Gson();
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;
    @Autowired
    SiteConfigService siteConfigService;
    @Autowired
    HttpSession session;

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

    @PutMapping(value = "")
    public JSONObject Update(@RequestBody String body) {
        Map<String, Object> siteConfigMap = gson.fromJson(body, Map.class);
        AtomicBoolean err = new AtomicBoolean(false);
        siteConfigMap.forEach((key, obj) -> {
            if (!siteConfigService.set(key, obj.toString())) {
                err.set(true);
            }
        });

        Map<String, Object> dataMap = siteConfigService.get();

        return new ApiResult<>(err.get() ? COMMON_FAIL : SUCCESS, dataMap).getJsonResult();
    }

}
