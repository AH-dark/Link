package com.ahdark.code.link.dao;

import com.ahdark.code.link.pojo.SiteConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ConfigMapper {
    List<SiteConfig> get(String name);

    List<SiteConfig> get();

    boolean set(SiteConfig siteConfig);
}
