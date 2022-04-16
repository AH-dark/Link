package com.ahdark.code.link.service;

import com.ahdark.code.link.pojo.ShortLink;

import java.util.List;

public interface ShortLinkService {
    List<ShortLink> getShortLinks(String key);

    List<ShortLink> getShortLinksById(int userId);

    Boolean setShortLinks(ShortLink shortLinks);
}
