package com.ahdark.code.link.service.impl;

import com.ahdark.code.link.dao.ShortLinkMapper;
import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.service.ShortLinkService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShortLinkServiceImpl implements ShortLinkService {
    @Resource
    private ShortLinkMapper shortLinkMapper;

    @Override
    public List<ShortLink> getShortLinks(String key) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", key);
        return this.shortLinkMapper.getShortLinks(paramMap);
    }

    @Override
    public List<ShortLink> getShortLinksById(int userId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        return this.shortLinkMapper.getShortLinksById(paramMap);
    }

    @Override
    public Boolean setShortLinks(ShortLink shortLinks) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", shortLinks.getKey());
        paramMap.put("origin", shortLinks.getOrigin());
        paramMap.put("user_id", shortLinks.getUser_id());
        List<Map<String, Object>> result = this.shortLinkMapper.setShortLinks(paramMap);
        return !result.isEmpty();
    }

}
