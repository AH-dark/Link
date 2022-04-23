package com.ahdark.code.link.service;

import com.ahdark.code.link.pojo.SiteConfig;

import java.util.Map;

public interface SiteConfigService {
    SiteConfig get(SiteConfig siteConfig);

    Map<String, Object> get();

    Boolean set(String name, String value);
}
