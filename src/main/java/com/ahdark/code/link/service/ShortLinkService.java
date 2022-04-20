package com.ahdark.code.link.service;

import com.ahdark.code.link.pojo.ShortLink;

import java.util.List;

public interface ShortLinkService {
    ShortLink getShortLinkByKey(String key);

    List<ShortLink> getShortLinkByUrl(String url);

    List<ShortLink> getShortLinksById(int userId);

    Boolean addView(ShortLink shortLink);

    Boolean setShortLinks(ShortLink shortLinks);
}
