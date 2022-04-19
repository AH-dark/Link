package com.ahdark.code.link.service.impl;

import com.ahdark.code.link.dao.ShortLinkMapper;
import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncServiceImpl implements AsyncService {
    private final Logger logger = LoggerFactory.getLogger(AsyncService.class);
    @Autowired
    private ShortLinkMapper shortLinkMapper;

    @Async
    @Override
    public void addLinkView(String key) {
        ShortLink shortLink = new ShortLink();
        shortLink.setKey(key);
        int status = shortLinkMapper.addView(shortLink);
        if (status == 0) {
            logger.error("Update short link view data error where key is %s.".formatted(key));
        } else {
            logger.info("Success to update short link view data where key is %s.".formatted(key));
        }
    }
}
