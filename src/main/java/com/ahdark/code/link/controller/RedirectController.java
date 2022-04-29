package com.ahdark.code.link.controller;

import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.service.AsyncService;
import com.ahdark.code.link.service.ShortLinkService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import static com.ahdark.code.link.utils.CodeInfo.NO_DATA;

@Controller
@RequestMapping(path = "/go/")
@Slf4j
public class RedirectController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;
    @Autowired
    ShortLinkService shortLinkService;
    @Autowired
    AsyncService asyncService;

    /**
     * /go/ 短链接跳转 核心部分
     *
     * @param key 链接标记
     */
    @RequestMapping(path = "/{key}")
    public String Redirect(@PathVariable String key) {
        ShortLink result = shortLinkService.getShortLinkByKey(key);
        if(result == null) {
            return NO_DATA.getMsg();
        }
        String origin = result.getOrigin();
        try {
            response.sendRedirect(origin);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.info("Request {} has been redirected to {}", request.getRequestURL(), origin);
        asyncService.addLinkView(key);
        return null;
    }
}
