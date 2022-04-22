package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.ShortLinkService;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

import static com.ahdark.code.link.utils.CodeInfo.*;

@RestController
@RequestMapping(path = "/api/shortLink", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ShortLinkController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private ShortLinkService shortLinkService;
    @Autowired
    private UserService userService;

    private static boolean isOriginMatch(String origin) {
        String originPattern = "^https?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";
        return Pattern.matches(originPattern, origin);
    }

    @GetMapping(path = "")
    public JSONObject GetWithNoParam() {
        return new ApiResult<>(NO_DATA).getJsonResult();
    }

    @GetMapping(path = "", params = {"key"})
    public JSONObject GetByKey(@RequestParam("key") String key) {
        log.info("GET Short link event, through Key.");
        log.info("Key: {}", key);

        ShortLink data = shortLinkService.getShortLinkByKey(key);
        if(data == null) {
            return new ApiResult<>(NO_DATA).getJsonResult();
        }

        ApiResult<ShortLink> result = new ApiResult<>(data);

        log.info("Get Uri success: {}", result);

        return result.getJsonResult();
    }

    @GetMapping(path = "", params = {"userId"})
    public JSONObject GetById(@RequestParam("userId") int userId) {
        log.info("GET Short link event, through userId.");
        log.info("User ID: {}", userId);
        List<ShortLink> results = shortLinkService.getShortLinksById(userId);
        if (results.isEmpty()) {
            response.setStatus(404);
            return new ApiResult<>(PARAM_NOT_COMPLETE).getJsonResult();
        }
        if (userId == 0) {
            response.setStatus(403);
            return new ApiResult<>(NO_PERMISSION).getJsonResult();
        }

        ApiResult<List<ShortLink>> result = new ApiResult<>(results);

        log.info("Get Uri success: {}", result);

        return result.getJsonResult();
    }

    @GetMapping(path = "", params = {"origin"})
    public JSONObject GetByUrl(@RequestParam("origin") String origin) {
        log.info("GET Short link event, through Origin.");
        log.info("Origin: {}", origin);
        if (!isOriginMatch(origin)) {
            response.setStatus(400);
            return new ApiResult<>(PARAM_NOT_VALID).getJsonResult();
        }
        List<ShortLink> results = shortLinkService.getShortLinkByUrl(origin);
        if (results.isEmpty()) {
            response.setStatus(404);
            return new ApiResult<>(NO_DATA).getJsonResult();
        }

        ApiResult<List<ShortLink>> result = new ApiResult<>(results);

        log.info("Get Uri success: {}", result);

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
            return new ApiResult<>(PARAM_NOT_COMPLETE).getJsonResult();
        }

        if (json.getString("origin") != null && isOriginMatch(json.getString("origin"))) {
            shortLinks.setOrigin(json.getString("origin"));
        } else {
            return new ApiResult<>(PARAM_NOT_COMPLETE).getJsonResult();
        }

        if (json.getInteger("user_id") != null && json.getInteger("user_id") > 0) {
            User result = userService.getUserById(json.getInteger("user_id"));
            if (result == null) {
                return new ApiResult<>(USER_ACCOUNT_NOT_EXIST).getJsonResult();
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
            Gson gson = new Gson();
            tmp = new ApiResult<>(oldData).getJsonResult();
            log.info("API log, Method: %s, Uri: %s, Result: %s".formatted(request.getMethod(), request.getRequestURI(), tmp.toString()));
            log.warn("The request is a duplicate, returning an existing short link.");
            return tmp;
        }

        // Insert
        Boolean isSetSuccess = shortLinkService.setShortLinks(shortLinks);

        // Check and response
        JSONObject r;
        if (isSetSuccess) {
            ShortLink generateLink = shortLinkService.getShortLinkByKey(json.getString("key"));
            r = new ApiResult<>(generateLink).getJsonResult();
        } else {
            r = new ApiResult<>(COMMON_FAIL).getJsonResult();
        }

        log.info("API log, Method: %s, Uri: %s, Result: %s".formatted(request.getMethod(), request.getRequestURI(), r.toString()));

        return r;
    }
}
