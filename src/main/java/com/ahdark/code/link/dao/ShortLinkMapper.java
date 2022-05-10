package com.ahdark.code.link.dao;

import com.ahdark.code.link.pojo.LimitData;
import com.ahdark.code.link.pojo.ShortLink;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ShortLinkMapper {
    /**
     * @param shortLink 短链接信息，可根据 Key、Origin、UserId 查找
     * @return ShortLink 列表
     */
    List<ShortLink> getShortLink(ShortLink shortLink);

    List<ShortLink> getLatestShortLink(LimitData limitData);

    int getNum(Integer userId);

    int getNumByTime(Map<String, Timestamp> timestampMap);

    /**
     * Set view ++
     *
     * @param shortLink 参数，需要 Key
     * @return 是否成功
     */
    boolean addView(ShortLink shortLink);

    /**
     * Update information of short link, based on key
     *
     * @param shortLink 参数，需要 Key
     * @return 是否成功
     */
    boolean updateShortLink(ShortLink shortLink);

    /**
     * @param shortLink 参数，需要完整
     * @return 是否成功
     */
    boolean setShortLink(ShortLink shortLink);

    /**
     * @param shortLink 参数，需要完整
     * @return 是否成功
     */
    boolean deleteShortLink(ShortLink shortLink);
}
