package com.ahdark.code.link.controller;

import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.service.AsyncService;
import com.ahdark.code.link.service.ShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path = "/go/")
public class RedirectController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    @Autowired
    ShortLinkService shortLinkService;

    @Autowired
    AsyncService asyncService;

    private final Logger logger = LoggerFactory.getLogger(RedirectController.class);

    @RequestMapping(path = "/{key}")
    public void Redirect(@PathVariable String key) throws IOException {
        List<ShortLink> result = shortLinkService.getShortLinkByKey(key);
        String origin = result.get(0).getOrigin();
        response.sendRedirect(origin);

        logger.info("Request " + request.getRequestURI() + " has been redirected to " + origin);
        asyncService.addLinkView(key);
    }
}
