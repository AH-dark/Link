package com.ahdark.code.link.controller.api.root;

import com.ahdark.code.link.pojo.SiteConfigRow;
import com.ahdark.code.link.service.SiteConfigService;
import com.ahdark.code.link.utils.ApiResult;
import com.ahdark.code.link.utils.CodeInfo;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PutMapping
    public JSONObject Update(@RequestBody String body) {
        SiteConfigRow[] siteConfigRows = gson.fromJson(body, SiteConfigRow[].class);
        List<SiteConfigRow> notSuccessSiteConfigRows = new ArrayList<>();
        for (SiteConfigRow siteConfigRow : siteConfigRows) {
            boolean result = siteConfigService.set(siteConfigRow.getName(), siteConfigRow.getValue());
            if (!result) {
                notSuccessSiteConfigRows.add(siteConfigRow);
            }
        }
        Map<String, List<SiteConfigRow>> map = new HashMap<>();
        map.put("failed", notSuccessSiteConfigRows);
        ApiResult<Object> apiResult = new ApiResult<>(notSuccessSiteConfigRows.isEmpty() ? CodeInfo.SUCCESS : CodeInfo.COMMON_FAIL);
        apiResult.setData(map);
        return apiResult.getJsonResult();
    }

}
