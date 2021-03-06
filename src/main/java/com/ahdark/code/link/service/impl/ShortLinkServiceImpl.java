package com.ahdark.code.link.service.impl;

import com.ahdark.code.link.dao.ShortLinkMapper;
import com.ahdark.code.link.pojo.LimitData;
import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.service.ShortLinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShortLinkServiceImpl implements ShortLinkService {
    @Resource
    private ShortLinkMapper shortLinkMapper;

    @Override
    public ShortLink getShortLinkByKey(String key) {
        ShortLink shortLink = new ShortLink();
        shortLink.setKey(key);
        List<ShortLink> results = this.shortLinkMapper.getShortLink(shortLink);
        if (results.isEmpty()) {
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
        shortLink.setUserId(userId);
        return this.shortLinkMapper.getShortLink(shortLink);
    }

    @Override
    public List<ShortLink> getLatestShortLink(int limit, int offset) {
        if (limit > 30 || limit < 1) {
            return null;
        }
        LimitData limitData = new LimitData<>();
        limitData.setLimit(limit);
        limitData.setOffset(offset);
        List<ShortLink> shortLinks = this.shortLinkMapper.getLatestShortLink(limitData);
        if (shortLinks.isEmpty()) {
            return null;
        }
        return shortLinks;
    }

    @Override
    public List<ShortLink> getLatestShortLink(int limit) {
        if (limit > 30 || limit < 1) {
            return null;
        }
        LimitData limitData = new LimitData<>();
        limitData.setLimit(limit);
        limitData.setOffset(0);
        List<ShortLink> shortLinks = this.shortLinkMapper.getLatestShortLink(limitData);
        if (shortLinks.isEmpty()) {
            return null;
        }
        return shortLinks;
    }

    @Override
    public Integer getNum() {
        return this.getNum(null);
    }

    @Override
    public Integer getNum(Integer userId) {
        return this.shortLinkMapper.getNum(userId);
    }

    @Override
    public Integer getNum(Timestamp start, Timestamp end) {
        Map<String, Timestamp> timestampMap = new HashMap<>();
        timestampMap.put("start", start);
        timestampMap.put("end", end);
        return this.shortLinkMapper.getNumByTime(timestampMap);
    }

    @Override
    public Boolean setShortLinks(ShortLink shortLink) {
        return shortLink != null && this.shortLinkMapper.setShortLink(shortLink);
    }

    @Override
    public Boolean updateShortLink(ShortLink shortLink) {
        return shortLink != null && this.shortLinkMapper.updateShortLink(shortLink);
    }

    @Override
    public Boolean addView(String key) {
        if (key == null) {
            return false;
        }

        ShortLink shortLink = new ShortLink();
        shortLink.setKey(key);
        return this.shortLinkMapper.addView(shortLink);
    }

    @Override
    public Boolean deleteShortLink(String key) {
        if (key == null) {
            return false;
        }

        ShortLink shortLink = new ShortLink();
        shortLink.setKey(key);
        return this.shortLinkMapper.deleteShortLink(shortLink);
    }

}
