package com.ahdark.code.link.config;

import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.utils.ApiResult;
import com.ahdark.code.link.utils.CodeInfo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class RootApiPermissionChecker implements HandlerInterceptor {
    private final Gson gson = new Gson();
    @Autowired
    HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId = session.getId();
        if (sessionId == null) {
            writeContent(response, new ApiResult<>(CodeInfo.USER_NOT_LOGIN));
            return false;
        }

        Object sessionData = session.getAttribute(sessionId);
        if (sessionData == null) {
            writeContent(response, new ApiResult<>(CodeInfo.USER_NOT_LOGIN));
            return false;
        }

        User user = gson.fromJson(gson.toJsonTree(sessionData), User.class);
        if (user.getRole() != 1) {
            writeContent(response, new ApiResult<>(CodeInfo.NO_PERMISSION));
        }

        return true;
    }

    private void writeContent(HttpServletResponse response, ApiResult apiResult) throws IOException {
        log.info(apiResult.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.print(apiResult.getJsonResult());
        writer.close();
    }
}
