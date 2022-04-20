package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ahdark.code.link.utils.CodeInfo.USER_ACCOUNT_NOT_EXIST;

@RestController
@RequestMapping(path = "/api/user")
@Slf4j
public class UserController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private UserService userService;

    public UserController(HttpServletResponse response, HttpServletRequest request, UserService userService) {
        this.response = response;
        this.request = request;
        this.userService = userService;
    }

    @GetMapping(params = {"email"})
    public JSONObject GetByEmail(@RequestParam("email") String email) {
        log.info("GET user event, through Email.");
        log.info("Email: {}", email);
        User user = this.userService.getUserByEmail(email);
        if (user == null) {
            return new ApiResult<>(USER_ACCOUNT_NOT_EXIST).getJsonResult();
        }

        Gson gson = new Gson();
        ApiResult<String> result = new ApiResult<>(gson.toJson(user, User.class));

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
        Gson gson = new Gson();

        ApiResult<String> result = new ApiResult<>(gson.toJson(user, User.class));

        log.info("Get User success: {}", result);

        return result.getJsonResult();
    }
}
