package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.ShortLinkService;
import com.ahdark.code.link.service.SiteConfigService;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ahdark.code.link.utils.CodeInfo.*;

@RestController
@RequestMapping(path = "/api/shortLink", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ShortLinkController {
    private final Gson gson = new Gson();
    private final Pattern pattern = Pattern.compile("^https?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$", Pattern.CASE_INSENSITIVE);
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private ShortLinkService shortLinkService;
    @Autowired
    private UserService userService;
    @Autowired
    private SiteConfigService siteConfigService;
    @Autowired
    private HttpSession session;

    private boolean isOriginMatch(String origin) {
        Matcher matcher = pattern.matcher(origin);
        return matcher.matches();
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
        if (data == null) {
            return new ApiResult<>(NO_DATA).getJsonResult();
        }
        log.info("{}", data);

        ApiResult<ShortLink> result = new ApiResult<>(data);

        log.info("Get Uri success: {}", result);

        return result.getJsonResult();
    }

    @GetMapping(path = "", params = {"userId"})
    public JSONObject GetById(@RequestParam("userId") int userId) {
        Object sessionAttribute = session.getAttribute(session.getId());
        User sessionUser = gson.fromJson(gson.toJsonTree(sessionAttribute), User.class);
        if(sessionAttribute == null) {
            return new ApiResult<>(NO_PERMISSION).getJsonResult();
        } else if (sessionUser.getRole() != 1 && sessionUser.getId() != userId) {
            return new ApiResult<>(NO_PERMISSION).getJsonResult();
        }

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
        log.info("There is a new short link generation request.");
        ShortLink shortLinks = gson.fromJson(body, ShortLink.class);

        if (Objects.equals(siteConfigService.get("enableTouristShorten"), false)) {
            String sessionId = session.getId();
            Object data = session.getAttribute(sessionId);
            if (data == null) {
                log.warn("User not log in, use session id {}", sessionId);
                return new ApiResult<>(USER_NOT_LOGIN).getJsonResult();
            }
        }

        // Data Check

        // Check Key
        if (shortLinks.getKey() == null || Objects.equals(shortLinks.getKey(), "")) {
            log.info("Key will be random");
            shortLinks.setKey(RandomStringUtils.randomAlphabetic(6));
            while (shortLinkService.getShortLinkByKey(shortLinks.getKey()) != null) {
                shortLinks.setKey(RandomStringUtils.randomAlphabetic(6));
            }
        } else if (shortLinks.getKey().length() <= 3) {
            log.error("Error: Key is not found or too short");
            response.setStatus(400);
            return new ApiResult<>(PARAM_NOT_COMPLETE).getJsonResult();
        }

        // Check Origin
        if (shortLinks.getOrigin() == null || !isOriginMatch(shortLinks.getOrigin())) {
            log.error("Error: Origin is not found or does not match the rule");
            response.setStatus(400);
            return new ApiResult<>(PARAM_NOT_COMPLETE).getJsonResult();
        }

        // Check Creator
        if (shortLinks.getUserId() > 0) {
            // Check user existed
            User result = userService.getUserById(shortLinks.getUserId());
            if (result == null) { // Check user exist
                log.error("Error: User does not exist");
                response.setStatus(400);
                return new ApiResult<>(USER_ACCOUNT_NOT_EXIST).getJsonResult();
            }
        } else {
            shortLinks.setUserId(0);
        }

        // Deduplication
        shortLinks.setOrigin(shortLinks.getOrigin().trim()); // remove spaces
        shortLinks.setOrigin(shortLinks.getOrigin().replace("/+$", "")); // remove trailing '/'
        // TODO: Protocol processing to avoid generating different links for different protocols

        // Data Duplication Detection
        List<ShortLink> find = shortLinkService.getShortLinkByUrl(shortLinks.getOrigin());
        if (!find.isEmpty()) {
            JSONObject tmp;
            ShortLink oldData = find.get(0);
            tmp = new ApiResult<>(oldData).getJsonResult();
            log.info("API log, Method: %s, Uri: %s, Result: %s".formatted(request.getMethod(), request.getRequestURI(), tmp.toString()));
            log.warn("The request is a duplicate, returning an existing short link.");
            return tmp;
        }

        // Check if exist
        if (shortLinkService.getShortLinkByKey(shortLinks.getKey()) != null) {
            return new ApiResult<>(DATA_IS_EXIST).getJsonResult();
        }

        // Insert
        Boolean isSetSuccess = shortLinkService.setShortLinks(shortLinks);

        // Check and response
        ApiResult<ShortLink> r;
        if (isSetSuccess) {
            ShortLink generateLink = shortLinkService.getShortLinkByKey(shortLinks.getKey());
            log.info("Result: {}", generateLink);
            r = new ApiResult<>(generateLink);
        } else {
            r = new ApiResult<>(COMMON_FAIL);
        }

        log.info("API log, Method: %s, Uri: %s, Result: %s".formatted(request.getMethod(), request.getRequestURI(), r.toString()));

        return r.getJsonResult();
    }
}
