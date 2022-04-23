package com.ahdark.code.link.dao;

import com.ahdark.code.link.pojo.ShortLink;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShortLinkMapper {
    List<ShortLink> getShortLink(ShortLink shortLink);

    /**
     * Set view ++
     *
     * @param shortLink 参数，需要 Key
     * @return SQL执行返回值
     */
    boolean addView(ShortLink shortLink);

    boolean setShortLinks(ShortLink shortLink);
}
