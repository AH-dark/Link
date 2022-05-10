package com.ahdark.code.link.controller.api.root;

import com.ahdark.code.link.pojo.LimitData;
import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.ShortLinkService;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ahdark.code.link.utils.CodeInfo.*;
import static java.lang.String.format;

@RestController
@RequestMapping(path = "/api/root/link")
@Slf4j
public class RootLinkManageController {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;
    @Autowired
    ShortLinkService shortLinkService;
    private final Gson gson = new Gson();
    @Autowired
    HttpSession session;
    private final Pattern pattern = Pattern.compile("^https?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$", Pattern.CASE_INSENSITIVE);
    @Autowired
    UserService userService;

    @GetMapping("/all")
    public JSONObject GetAll(
            @RequestParam(defaultValue = "10", required = false) Integer limit,
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "0", required = false) Integer user
    ) {
        log.info("GET all user event.");
        int userNum = this.shortLinkService.getNum(user);
        if (Math.max(0, (page - 1)) * limit > userNum) {
            return new ApiResult<>(COMMON_FAIL).getJsonResult();
        }

        List<ShortLink> links = this.shortLinkService.getLatestShortLink(limit, Math.max(0, (page - 1)) * limit);
        if (links == null || links.isEmpty()) {
            return new ApiResult<>(COMMON_FAIL).getJsonResult();
        }

        LimitData<List<ShortLink>> limitData = new LimitData<>();
        limitData.setLimit(limit);
        limitData.setOffset(Math.max(0, (page - 1)) * limit);
        limitData.setTotal(userNum);
        limitData.setData(links);

        ApiResult<LimitData<List<ShortLink>>> result = new ApiResult<>(limitData);

        if (page * limit > userNum) {
            result.setExceptions("incomplete data");
        }

        log.info("Get Users success: {}", result);

        return result.getJsonResult();
    }

    @GetMapping(path = "", params = {"key"})
    public JSONObject GetByKey(@RequestParam("key") String key) {
        log.info("GET Short link event, through Key.");
        log.info("Key: {}", key);

        ShortLink data = shortLinkService.getShortLinkByKey(key);
        if (data == null) {
            return new ApiResult<>(NO_DATA).getJsonResult();
        } else {
            return new ApiResult<>(SUCCESS, data).getJsonResult();
        }
    }

    @PutMapping
    public JSONObject Update(@RequestBody String body) {
        ShortLink shortLink = gson.fromJson(body, ShortLink.class);
        String linkKey = shortLink.getKey();
        int userId = shortLink.getUserId();
        User creator = userService.getUserById(userId);
        if (creator == null) {
            return new ApiResult<>(USER_ACCOUNT_NOT_EXIST).getJsonResult();
        }
        Boolean result = shortLinkService.updateShortLink(shortLink);
        if (result) {
            ShortLink queryShortLink = shortLinkService.getShortLinkByKey(linkKey);
            return new ApiResult<>(SUCCESS, queryShortLink).getJsonResult();
        } else {
            return new ApiResult<>(COMMON_FAIL).getJsonResult();
        }
    }

    @PostMapping(path = "")
    public JSONObject Post(@RequestBody String body) {
        ShortLink shortLinks = gson.fromJson(body, ShortLink.class);

        // Check Key
        if (shortLinks.getKey() == null || Objects.equals(shortLinks.getKey(), "")) {
            log.info("Key will be random");
            shortLinks.setKey(RandomStringUtils.randomAlphabetic(6));
            while (shortLinkService.getShortLinkByKey(shortLinks.getKey()) != null) {
                shortLinks.setKey(RandomStringUtils.randomAlphabetic(6));
            }
        } else if (shortLinks.getKey().length() <= 3) {
            log.error("Error: Key is not found or too short");
            httpServletResponse.setStatus(400);
            return new ApiResult<>(PARAM_NOT_COMPLETE).getJsonResult();
        }

        // Check Origin
        if (shortLinks.getOrigin() == null || !isOriginMatch(shortLinks.getOrigin())) {
            log.error("Error: Origin is not found or does not match the rule");
            httpServletResponse.setStatus(400);
            return new ApiResult<>(PARAM_NOT_COMPLETE).getJsonResult();
        }

        // Check Creator
        if (shortLinks.getUserId() > 0) {
            // Check user existed
            User result = userService.getUserById(shortLinks.getUserId());
            if (result == null) { // Check user exist
                log.error("Error: User does not exist");
                httpServletResponse.setStatus(400);
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
            log.info(format("API log, Method: %s, Uri: %s, Result: %s", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), tmp));
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

        log.info(format("API log, Method: %s, Uri: %s, Result: %s", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), r));

        return r.getJsonResult();
    }

    private boolean isOriginMatch(String origin) {
        Matcher matcher = pattern.matcher(origin);
        return matcher.matches();
    }

    @DeleteMapping(params = "key")
    public JSONObject Delete(@RequestParam String key) {
        Boolean isSuccess = shortLinkService.deleteShortLink(key);
        return new ApiResult<>(isSuccess ? SUCCESS : COMMON_FAIL).getJsonResult();
    }
}
