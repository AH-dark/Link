package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.AsyncService;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.ahdark.code.link.utils.CodeInfo.*;

@RestController
@RequestMapping(path = "/api/login")
@Slf4j
public class LoginController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private UserService userService;
    @Autowired
    private AsyncService asyncService;

    @Autowired
    private HttpSession session;

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject Login(@RequestBody String body) {
        log.info("Login event detected.");

        JSONObject data = JSON.parseObject(body);

        if (body.isEmpty()) { // Check param
            return new ApiResult<>(PARAM_IS_BLANK).getJsonResult();
        }

        String email = data.getString("email");
        String password = data.getString("password");
        if (email == null || password == null) { // Check param
            return new ApiResult<>(PARAM_NOT_COMPLETE).getJsonResult();
        }

        log.info("email: {}", email);
        log.info("password: {}", password);

        User userData = userService.getUserByEmail(email);

        String md5EncodePassword = DigestUtils.md5DigestAsHex(password.getBytes()).toUpperCase();
        log.info("md5 encoded password: {}", md5EncodePassword);

        if (userData == null) {
            return new ApiResult<>(USER_ACCOUNT_NOT_EXIST).getJsonResult();
        } else if (!Objects.equals(userData.getPassword(), md5EncodePassword)) {
            return new ApiResult<>(USER_CREDENTIALS_ERROR).getJsonResult();
        } else {
            String sessionId = session.getId();
            session.setAttribute(sessionId, userData);
            session.setMaxInactiveInterval(1800);

            asyncService.updateUserLoginTime(userData.getId());

            log.info("Login success, session has been created: {}", session.getAttribute(sessionId));

            Gson gson = new Gson();
            return new ApiResult<>(SUCCESS, gson.toJson(userData)).getJsonResult();
        }
    }
}
