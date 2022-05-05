package com.ahdark.code.link.controller.api.root;

import com.ahdark.code.link.pojo.LimitData;
import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static com.ahdark.code.link.utils.CodeInfo.*;

@RestController
@RequestMapping(path = "/api/root/user")
@Slf4j
public class RootUserManageController {
    private final Gson gson = new Gson();
    @Autowired
    HttpSession session;
    @Autowired
    UserService userService;

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

        if (id == 0) {
            return new ApiResult<>(USER_ACCOUNT_NOT_EXIST).getJsonResult();
        }

        User user = this.userService.getUserById(id);
        if (user == null) {
            return new ApiResult<>(USER_ACCOUNT_NOT_EXIST).getJsonResult();
        }

        ApiResult<User> result = new ApiResult<>(user);

        log.info("Get User success: {}", result);

        return result.getJsonResult();
    }

    @GetMapping("/all")
    public JSONObject GetAll(
            @RequestParam(defaultValue = "10", required = false) Integer limit,
            @RequestParam(defaultValue = "1", required = false) Integer page
    ) {
        log.info("GET all user event.");
        int userNum = this.userService.getNum();
        if (Math.max(0, (page - 1)) * limit > userNum) {
            return new ApiResult<>(COMMON_FAIL).getJsonResult();
        }

        List<User> users = this.userService.getAllUser(limit, Math.max(0, (page - 1)) * limit);
        if (users.isEmpty()) {
            return new ApiResult<>(COMMON_FAIL).getJsonResult();
        }

        LimitData<List<User>> limitData = new LimitData<>();
        limitData.setLimit(limit);
        limitData.setOffset(Math.max(0, (page - 1)) * limit);
        limitData.setTotal(userNum);
        limitData.setData(users);

        ApiResult<LimitData<List<User>>> result = new ApiResult<>(limitData);

        if (page * limit > userNum) {
            result.setExceptions("incomplete data");
        }

        log.info("Get Users success: {}", result);

        return result.getJsonResult();
    }

    @PutMapping()
    public JSONObject UpdateInfo(@RequestBody String body) {
        // Convert
        User inputData = gson.fromJson(body, User.class);

        log.info("Request data: {}", inputData);

        // Lock
        if (inputData.getId() == null) {
            return new ApiResult<>(PARAM_NOT_COMPLETE).getJsonResult();
        }

        inputData.setPassword(DigestUtils.md5DigestAsHex(inputData.getPassword().getBytes(StandardCharsets.UTF_8)).toUpperCase(Locale.ROOT));

        // Update Database
        boolean isSuccess = this.userService.updateUserInfo(inputData);
        if (isSuccess) {
            User user = this.userService.getUserById(inputData.getId());
            return new ApiResult<>(SUCCESS, user).getJsonResult();
        } else {
            return new ApiResult<>(COMMON_FAIL).getJsonResult();
        }
    }

    @PostMapping
    public JSONObject AddUser(@RequestBody String body) {
        User user = gson.fromJson(body, User.class);
        user.setId(null);
        user.setCreateTime(null);
        user.setLoginTime(null);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)).toUpperCase(Locale.ROOT));

        if (userService.getUserByEmail(user.getEmail()) != null) {
            return new ApiResult<>(DATA_IS_EXIST).getJsonResult();
        }

        boolean result = userService.setUser(user);
        if (!result) {
            return new ApiResult<>(COMMON_FAIL).getJsonResult();
        }
        User innerDbUser = userService.getUserByEmail(user.getEmail());
        return new ApiResult<>(SUCCESS, innerDbUser).getJsonResult();
    }
}
