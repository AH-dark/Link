package com.ahdark.code.link.service;

import com.ahdark.code.link.pojo.ShortLink;

import java.sql.Timestamp;
import java.util.List;

public interface ShortLinkService {
    ShortLink getShortLinkByKey(String key);

    List<ShortLink> getShortLinkByUrl(String url);

    List<ShortLink> getShortLinksById(int userId);

    List<ShortLink> getLatestShortLink(int limit, int offset);

    List<ShortLink> getLatestShortLink(int limit);

    Integer getNum();

    Integer getNum(Integer userId);

    Integer getNum(Timestamp start, Timestamp end);

    Boolean updateShortLink(ShortLink shortLink);

    Boolean addView(String key);

    Boolean setShortLinks(ShortLink shortLinks);

    Boolean deleteShortLink(String key);
}
