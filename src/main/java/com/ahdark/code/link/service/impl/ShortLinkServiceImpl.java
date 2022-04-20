package com.ahdark.code.link.service.impl;

import com.ahdark.code.link.dao.ShortLinkMapper;
import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.service.ShortLinkService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortLinkServiceImpl implements ShortLinkService {
    @Resource
    private ShortLinkMapper shortLinkMapper;

    @Override
    public ShortLink getShortLinkByKey(String key) {
        ShortLink shortLink = new ShortLink();
        shortLink.setKey(key);
        List<ShortLink> results = this.shortLinkMapper.getShortLink(shortLink);
        if(results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    @Override
    public List<ShortLink> getShortLinkByUrl(String url) {
        ShortLink shortLink = new ShortLink();
        shortLink.setOrigin(url);
        return this.shortLinkMapper.getShortLink(shortLink);
    }

    @Override
    public List<ShortLink> getShortLinksById(int userId) {
        ShortLink shortLink = new ShortLink();
        shortLink.setUser_id(userId);
        return this.shortLinkMapper.getShortLink(shortLink);
    }

    @Override
    public Boolean setShortLinks(ShortLink shortLinks) {
        int result = this.shortLinkMapper.setShortLinks(shortLinks);
        return result != 0;
    }

    @Override
    public Boolean addView(ShortLink shortLink) {
        int result = this.shortLinkMapper.addView(shortLink);
        return result!=0;
    }

}
