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

import static java.lang.String.format;

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
        Boolean status = shortLinkService.addView(key);
        if (status) {
            log.info(format("Success to update short link view data where key is %s.", key));
        } else {
            log.error(format("Update short link view data error where key is %s.", key));
        }
    }

    @Async
    @Override
    public void updateUserLoginTime(int id) {
        boolean result = userService.updateLoginTime(id);
        if (result) {
            log.info("Update user login time succeed. ID: {}", id);
        } else {
            log.error("Update user login time failed. ID: {}", id);
        }
    }
}
