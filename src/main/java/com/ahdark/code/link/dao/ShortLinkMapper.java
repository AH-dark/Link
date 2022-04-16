package com.ahdark.code.link.dao;

import com.ahdark.code.link.pojo.ShortLink;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ShortLinkMapper {
    List<ShortLink> getShortLinks(Map<String, Object> paramMap);

    List<ShortLink> getShortLinksById(Map<String, Object> paramMap);

    List<Map<String, Object>> setShortLinks(Map<String, Object> paramMap);
}
