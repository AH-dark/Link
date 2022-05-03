package com.ahdark.code.link.dao;

import com.ahdark.code.link.pojo.SiteConfigRow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ConfigMapper {
    List<SiteConfigRow> get(String name);

    List<SiteConfigRow> get();

    boolean set(SiteConfigRow siteConfigRow);
}
