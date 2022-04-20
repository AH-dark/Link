package com.ahdark.code.link.service.impl;

import com.ahdark.code.link.dao.ShortLinkMapper;
import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.service.AsyncService;
import com.ahdark.code.link.service.ShortLinkService;
import com.ahdark.code.link.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService {
    @Autowired
    private ShortLinkService shortLinkService;

    @Autowired
    private UserService userService;

    @Async
    @Override
    public void addLinkView(String key) {
        ShortLink shortLink = new ShortLink();
        shortLink.setKey(key);
        Boolean status = shortLinkService.addView(shortLink);
        if (status) {
            log.info("Success to update short link view data where key is %s.".formatted(key));
        } else {
            log.error("Update short link view data error where key is %s.".formatted(key));
        }
    }

    @Async
    @Override
    public void updateUserLoginTime(int id) {
        userService.updateLoginTime(id);
    }
}
