package com.ahdark.code.link.controller.api;

import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ahdark.code.link.utils.CodeInfo.SUCCESS;
import static com.ahdark.code.link.utils.CodeInfo.USER_NOT_LOGIN;

@RestController
@RequestMapping(path = "/api/logout", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class LogoutController {
    private final Gson gson = new Gson();
    @Autowired
    private HttpSession session;

    @PostMapping(path = "")
    public JSONObject Logout() {
        log.info("Logout event detected.");
        String sessionId = session.getId();
        Object sessionAttribute = session.getAttribute(sessionId);
        if(sessionAttribute == null) {
            return new ApiResult<>(USER_NOT_LOGIN).getJsonResult();
        }

        session.invalidate();
        session.removeAttribute(sessionId);

        log.info("Session {} has been removed.", sessionId);

        return new ApiResult<>(SUCCESS).getJsonResult();
    }
}
