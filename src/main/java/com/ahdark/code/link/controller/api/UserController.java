package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ahdark.code.link.utils.CodeInfo.USER_ACCOUNT_NOT_EXIST;
import static com.ahdark.code.link.utils.CodeInfo.USER_NOT_LOGIN;

@RestController
@RequestMapping(path = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;

    private final Gson gson = new Gson();

    @GetMapping
    public JSONObject GetByCookie() {
        String sessionId = session.getId();

        log.info("GET user event, through Cookie.");
        log.info("Cookie: {}", sessionId);

        Object data =  session.getAttribute(sessionId);

        if (data == null) {
            log.warn("User not log in, use session id {}", sessionId);
            return new ApiResult<>(USER_NOT_LOGIN).getJsonResult();
        }

        log.info("Get session success {}", data);
        User user = gson.fromJson(gson.toJsonTree(data), User.class);

        ApiResult<User> result = new ApiResult<>(user);

        log.info("Get User success: {}", result);

        return result.getJsonResult();
    }

    @GetMapping(params = {"email"})
    public JSONObject GetByEmail(@RequestParam("email") String email) {
        log.info("GET user event, through Email.");
        log.info("Email: {}", email);

        User user = this.userService.getUserByEmail(email);
        if (user == null) {
            return new ApiResult<>(USER_ACCOUNT_NOT_EXIST).getJsonResult();
        }

        ApiResult<User> result = new ApiResult<>(user);

        log.info("Get User success: {}", result);

        return result.getJsonResult();
    }

    @GetMapping(params = {"id"})
    public JSONObject GetById(@RequestParam("id") Integer id) {
        log.info("GET user event, through ID.");
        log.info("ID: {}", id);

        User user = this.userService.getUserById(id);
        if (user == null) {
            return new ApiResult<>(USER_ACCOUNT_NOT_EXIST).getJsonResult();
        }

        ApiResult<User> result = new ApiResult<>(user);

        log.info("Get User success: {}", result);

        return result.getJsonResult();
    }
}
