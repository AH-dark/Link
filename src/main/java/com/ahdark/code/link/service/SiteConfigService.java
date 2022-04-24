package com.ahdark.code.link.service;

import com.ahdark.code.link.pojo.SiteConfig;

import java.util.Map;

public interface SiteConfigService {
    Object get(String name);

    Map<String, Object> get();

    Boolean set(String name, String value);
}
