package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.controller.RedirectController;
import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.service.ShortLinkService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/shortLink")
public class ShortLinkController {
    final
    HttpServletRequest request;

    final
    HttpServletResponse response;

    final
    ShortLinkService shortLinkService;

    public ShortLinkController(ShortLinkService shortLinkService, HttpServletResponse response, HttpServletRequest request) {
        this.shortLinkService = shortLinkService;
        this.response = response;
        this.request = request;
    }

    @GetMapping(path = "")
    public JSONObject GetWithNoParam() {
        return new ApiResult(-1, "none.").getJsonResult();
    }

    @GetMapping(path = "", params = {"key"})
    public JSONObject GetByKey(@RequestParam("key") String key) {
        List<ShortLink> results = shortLinkService.getShortLinks(key);
        if (results.isEmpty()) {
            response.setStatus(404);
            return new ApiResult(-1, "not found.").getJsonResult();
        }

        ShortLink data = results.get(0);
        JSONObject json = new JSONObject();
        json.put("key", data.getKey());
        json.put("origin", data.getOrigin());
        json.put("user_id", data.getUser_id());
        json.put("view", data.getView());
        json.put("create_time", data.getCreate_time().toString());

        ApiResult result = new ApiResult(json);

        Logger logger = LoggerFactory.getLogger(RedirectController.class);
        logger.info("API Logger, Method: %s, Uri: %s, %s".formatted(request.getMethod(), request.getRequestURI(), result.toString()));

        return result.getJsonResult();
    }

    @GetMapping(path = "", params = {"userId"})
    public JSONObject GetById(@RequestParam("userId") int userId) {
        List<ShortLink> results = shortLinkService.getShortLinksById(userId);
        if (results.isEmpty()) {
            response.setStatus(404);
            return new ApiResult(-1, "not found.").getJsonResult();
        }
        if (userId == 0) {
            response.setStatus(403);
            return new ApiResult(-1, "not allowed.").getJsonResult();
        }

        JSONArray array = new JSONArray();
        array.addAll(results);

        ApiResult result = new ApiResult(array);

        Logger logger = LoggerFactory.getLogger(RedirectController.class);
        logger.info("API Logger, Method: %s, Uri: %s, %s".formatted(request.getMethod(), request.getRequestURI(), result.toString()));

        return result.getJsonResult();
    }

    @PostMapping(path = "")
    public JSONObject Post(@RequestBody String body) throws IOException {
        JSONObject json = JSON.parseObject(body);
        ShortLink shortLinks = new ShortLink();

        if (json.getBooleanValue("key")) {
            shortLinks.setKey(json.getString("key"));
        } else {
            response.sendError(400);
            return new JSONObject();
        }
        if (json.getBooleanValue("origin")) {
            shortLinks.setOrigin(json.getString("origin"));
        } else {
            response.sendError(400);
            return new JSONObject();
        }
        if (json.getBooleanValue("user_id")) {
            shortLinks.setUser_id(json.getInteger("user_id"));
        } else {
            shortLinks.setUser_id(null);
        }

        Boolean isSetSuccess = shortLinkService.setShortLinks(shortLinks);

        JSONObject r = isSetSuccess ? new ApiResult().getJsonResult() : new ApiResult(-1, "sql insert error.").getJsonResult();

        Logger logger = LoggerFactory.getLogger(RedirectController.class);
        logger.info("API Logger, Method: %s, Uri: %s, Result: %s".formatted(request.getMethod(), request.getRequestURI(), r.toString()));

        return r;
    }
}
