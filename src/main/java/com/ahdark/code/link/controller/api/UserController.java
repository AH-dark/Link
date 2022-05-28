package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.ahdark.code.link.utils.CodeInfo.*;

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

    @SuppressWarnings("RedundantIfStatement")
    private boolean isPermission() {
        String sessionId = session.getId();
        if (sessionId == null) {
            return false;
        }

        Object sessionAttribute = session.getAttribute(sessionId);
        if (sessionAttribute == null) {
            return false;
        }

        User user = gson.fromJson(gson.toJsonTree(sessionAttribute), User.class);
        if (user == null) {
            return false;
        }

        return true;
    }

    @GetMapping
    public JSONObject GetByCookie() {
        String sessionId = session.getId();

        log.info("GET user event, through Cookie.");
        log.info("Cookie: {}", sessionId);

        Object data = session.getAttribute(sessionId);

        if (data == null) {
            log.warn("User not log in, use session id {}", sessionId);
            return new ApiResult<>(USER_NOT_LOGIN).getJsonResult();
        }

        log.info("Get session success {}", data);
        User user = gson.fromJson(gson.toJsonTree(data), User.class);

        if (!this.isPermission()) {
            user.setPassword(null);
            user.setLoginTime(null);
            user.setRegisterIP(null);
        }

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

        if (!this.isPermission()) {
            user.setPassword(null);
            user.setLoginTime(null);
            user.setRegisterIP(null);
        }

        ApiResult<User> result = new ApiResult<>(user);

        log.info("Get User success: {}", result);

        return result.getJsonResult();
    }

    @GetMapping(params = {"id"})
    public JSONObject GetById(@RequestParam("id") Integer id) {
        log.info("GET user event, through ID.");
        log.info("ID: {}", id);

        if (id == 0) {
            return new ApiResult<>(USER_ACCOUNT_NOT_EXIST).getJsonResult();
        }

        User user = this.userService.getUserById(id);
        if (user == null) {
            return new ApiResult<>(USER_ACCOUNT_NOT_EXIST).getJsonResult();
        }

        ApiResult<User> result = new ApiResult<>(user);

        if (!this.isPermission()) {
            user.setPassword(null);
            user.setLoginTime(null);
            user.setRegisterIP(null);
        }

        log.info("Get User success: {}", result);

        return result.getJsonResult();
    }

    @PutMapping()
    public JSONObject UpdateInfo(@RequestBody String body) {
        // Check
        String sessionId = session.getId();
        Object sessionAttribute = session.getAttribute(sessionId);
        log.info("Update user info request, session id {}", sessionId);
        if (sessionAttribute == null) {
            return new ApiResult<>(NO_PERMISSION).getJsonResult();
        }
        User currentUser = gson.fromJson(gson.toJsonTree(sessionAttribute), User.class);

        // Convert
        User inputData = gson.fromJson(body, User.class);

        log.info("Request data: {}", inputData);

        // Lock
        if (inputData.getId() != null && !Objects.equals(inputData.getId(), currentUser.getId())) {
            return new ApiResult<>(NO_PERMISSION).getJsonResult();
        }

        Integer userId = currentUser.getId();

        // Info can not change by user
        // password
        if (inputData.getPassword() != null && !Objects.equals(inputData.getPassword(), currentUser.getPassword()) && !Objects.equals(inputData.getPassword(), "")) {
            String changedPassword = String.valueOf(inputData.getPassword());
            log.info("User {} password will be changed to '{}'", userId, changedPassword);
            currentUser.setPassword(DigestUtils.md5DigestAsHex(changedPassword.getBytes()).toUpperCase());
        }
        // name
        if (inputData.getName() != null && !Objects.equals(inputData.getName(), currentUser.getName())) {
            String changedName = String.valueOf(inputData.getName());
            log.info("User (id: {}, email: {}) name will be changed to '{}'", userId, currentUser.getEmail(), changedName);
            currentUser.setName(changedName);
        }
        // description
        if (inputData.getDescription() != null && !Objects.equals(inputData.getDescription(), currentUser.getDescription())) {
            String changedDescription = String.valueOf(inputData.getDescription());
            log.info("User (id: {}, email: {}) description will be changed to '{}'", userId, currentUser.getEmail(), changedDescription);
            currentUser.setDescription(changedDescription);
        }

        // Update Database
        boolean isSuccess = this.userService.updateUserInfo(currentUser);
        if (isSuccess) {
            User user = this.userService.getUserById(userId);
            session.setAttribute(sessionId, user);
            return new ApiResult<>(SUCCESS, user).getJsonResult();
        } else {
            return new ApiResult<>(COMMON_FAIL).getJsonResult();
        }
    }
}
