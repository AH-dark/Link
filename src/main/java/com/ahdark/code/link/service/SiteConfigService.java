package com.ahdark.code.link.service;

import java.util.Map;

public interface SiteConfigService {
    Object get(String name);

    Map<String, Object> get();

    Boolean set(String name, String value);
}
