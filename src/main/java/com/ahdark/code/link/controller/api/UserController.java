package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.controller.RedirectController;
import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ahdark.code.link.utils.CodeResult.USER_ACCOUNT_NOT_EXIST;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    @Autowired
    UserService userService;


    public UserController(HttpServletResponse response, HttpServletRequest request, UserService userService) {
        this.response = response;
        this.request = request;
        this.userService = userService;
    }

    @GetMapping(params = {"email"})
    public JSONObject GetByEmail(@RequestParam("email") String email) {
        List<User> results = this.userService.getUserByEmail(email);
        if (results.isEmpty()) {
            response.setStatus(404);
            return new ApiResult(USER_ACCOUNT_NOT_EXIST).getJsonResult();
        }
        User user = results.get(0);
        JSONObject json = new JSONObject();
        json.put("id", user.getId());
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        json.put("password", user.getPassword());
        json.put("create_time", user.getCreate_time());
        json.put("register_ip", user.getRegister_ip());
        json.put("login_time", user.getLogin_time());
        json.put("available", user.isAvailable());

        ApiResult result = new ApiResult(json);

        Logger logger = LoggerFactory.getLogger(RedirectController.class);
        logger.info("API Logger, Method: %s, Uri: %s, %s".formatted(request.getMethod(), request.getRequestURI(), result.toString()));

        return result.getJsonResult();
    }

    @GetMapping(params = {"id"})
    public JSONObject GetById(@RequestParam("id") Integer id) {
        List<User> results = this.userService.getUserById(id);
        if (results.isEmpty()) {
            response.setStatus(404);
            return new ApiResult(USER_ACCOUNT_NOT_EXIST).getJsonResult();
        }
        User user = results.get(0);
        JSONObject json = new JSONObject();
        json.put("id", user.getId());
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        json.put("password", user.getPassword());
        json.put("create_time", user.getCreate_time());
        json.put("register_ip", user.getRegister_ip());
        json.put("login_time", user.getLogin_time());
        json.put("available", user.isAvailable());

        ApiResult result = new ApiResult(json);

        Logger logger = LoggerFactory.getLogger(RedirectController.class);
        logger.info("API Logger, Method: %s, Uri: %s, %s".formatted(request.getMethod(), request.getRequestURI(), result.toString()));

        return result.getJsonResult();
    }
}
