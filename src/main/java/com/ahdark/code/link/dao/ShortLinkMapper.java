package com.ahdark.code.link.dao;

import com.ahdark.code.link.pojo.ShortLink;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShortLinkMapper {
    List<ShortLink> getShortLinkByKey(ShortLink shortLink);

    List<ShortLink> getShortLinksByUserId(ShortLink shortLink);

    List<ShortLink> getShortLinksByUrl(ShortLink shortLink);

    int setShortLinks(ShortLink shortLink);
}
