package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.controller.RedirectController;
import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.ShortLinkService;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

import static com.ahdark.code.link.utils.CodeResult.*;

@RestController
@RequestMapping(path = "/api/shortLink")
public class ShortLinkController {
    final
    HttpServletRequest request;

    private final HttpServletResponse response;

    private final ShortLinkService shortLinkService;

    private final UserService userService;

    public ShortLinkController(ShortLinkService shortLinkService, HttpServletResponse response, HttpServletRequest request, UserService userService) {
        this.shortLinkService = shortLinkService;
        this.response = response;
        this.request = request;
        this.userService = userService;
    }

    private boolean isOriginMatch(String origin) {
        String originPattern = "^https?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";
        return Pattern.matches(originPattern, origin);
    }

    @GetMapping(path = "")
    public JSONObject GetWithNoParam() {
        return new ApiResult(NO_DATA).getJsonResult();
    }

    @GetMapping(path = "", params = {"key"})
    public JSONObject GetByKey(@RequestParam("key") String key) {
        List<ShortLink> results = shortLinkService.getShortLinkByKey(key);
        if (results.isEmpty()) {
            response.setStatus(404);
            return new ApiResult(NO_DATA).getJsonResult();
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
            return new ApiResult(PARAM_NOT_COMPLETE).getJsonResult();
        }
        if (userId == 0) {
            response.setStatus(403);
            return new ApiResult(NO_PERMISSION).getJsonResult();
        }

        JSONArray array = new JSONArray();
        array.addAll(results);

        ApiResult result = new ApiResult(array);

        Logger logger = LoggerFactory.getLogger(RedirectController.class);
        logger.info("API Logger, Method: %s, Uri: %s, %s".formatted(request.getMethod(), request.getRequestURI(), result.toString()));

        return result.getJsonResult();
    }

    @GetMapping(path = "", params = {"origin"})
    public JSONObject GetByUrl(@RequestParam("origin") String origin) {
        if (!this.isOriginMatch(origin)) {
            response.setStatus(400);
            return new ApiResult(PARAM_NOT_VALID).getJsonResult();
        }
        List<ShortLink> results = shortLinkService.getShortLinkByUrl(origin);
        if (results.isEmpty()) {
            response.setStatus(404);
            return new ApiResult(NO_DATA).getJsonResult();
        }

        JSONArray array = new JSONArray();
        array.addAll(results);

        ApiResult result = new ApiResult(array);

        Logger logger = LoggerFactory.getLogger(RedirectController.class);
        logger.info("API Logger, Method: %s, Uri: %s, %s".formatted(request.getMethod(), request.getRequestURI(), result.toString()));

        return result.getJsonResult();
    }

    @PostMapping(path = "")
    public JSONObject Post(@RequestBody String body) {
        JSONObject json = JSON.parseObject(body);
        ShortLink shortLinks = new ShortLink();

        // Data Check
        if (json.getString("key") != null && json.getString("key").length() > 3) {
            shortLinks.setKey(json.getString("key"));
        } else {
            response.setStatus(400);
            return new ApiResult(PARAM_NOT_COMPLETE).getJsonResult();
        }

        if (json.getString("origin") != null && isOriginMatch(json.getString("origin"))) {
            shortLinks.setOrigin(json.getString("origin"));
        } else {
            return new ApiResult(PARAM_NOT_COMPLETE).getJsonResult();
        }

        if (json.getInteger("user_id") != null && json.getInteger("user_id") > 0) {
            List<User> results = userService.getUserById(json.getInteger("user_id"));
            if (results.isEmpty()) {
                return new ApiResult(USER_ACCOUNT_NOT_EXIST).getJsonResult();
            }
            shortLinks.setUser_id(json.getInteger("user_id"));
        } else {
            shortLinks.setUser_id(null);
        }

        // Data Duplication Detection
        List<ShortLink> find = shortLinkService.getShortLinkByUrl(json.getString("origin"));
        if (!find.isEmpty()) {
            JSONObject tmp;
            ShortLink oldData = find.get(0);
            JSONObject d = new JSONObject();
            d.put("key", oldData.getKey());
            d.put("origin", oldData.getOrigin());
            d.put("user_id", oldData.getUser_id());
            d.put("view", oldData.getView());
            d.put("create_time", oldData.getCreate_time());
            tmp = new ApiResult(d).getJsonResult();
            Logger logger = LoggerFactory.getLogger(RedirectController.class);
            logger.info("API Logger, Method: %s, Uri: %s, Result: %s".formatted(request.getMethod(), request.getRequestURI(), tmp.toString()));
            logger.warn("The request is a duplicate, returning an existing short link.");
            return tmp;
        }

        // Insert
        Boolean isSetSuccess = shortLinkService.setShortLinks(shortLinks);

        // Check and response
        JSONObject r;
        if (isSetSuccess) {
            ShortLink generateLink = shortLinkService.getShortLinkByKey(json.getString("key")).get(0);
            JSONObject d = new JSONObject();
            d.put("key", generateLink.getKey());
            d.put("origin", generateLink.getOrigin());
            d.put("user_id", generateLink.getUser_id());
            d.put("view", generateLink.getView());
            d.put("create_time", generateLink.getCreate_time());
            r = new ApiResult(d).getJsonResult();
        } else {
            r = new ApiResult(COMMON_FAIL).getJsonResult();
        }

        Logger logger = LoggerFactory.getLogger(RedirectController.class);
        logger.info("API Logger, Method: %s, Uri: %s, Result: %s".formatted(request.getMethod(), request.getRequestURI(), r.toString()));

        return r;
    }
}
