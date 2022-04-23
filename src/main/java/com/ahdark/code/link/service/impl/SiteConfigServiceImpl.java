package com.ahdark.code.link.service.impl;

import com.ahdark.code.link.dao.ConfigMapper;
import com.ahdark.code.link.pojo.SiteConfig;
import com.ahdark.code.link.service.SiteConfigService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SiteConfigServiceImpl implements SiteConfigService {
    @Autowired
    private ConfigMapper configMapper;

    private final Gson gson = new Gson();

    @Override
    public SiteConfig get(SiteConfig siteConfig) {
        List<SiteConfig> siteConfigs = configMapper.get(siteConfig.getName());
        if (!siteConfigs.isEmpty()) {
            siteConfig.setValue(siteConfigs.get(0).getValue());
            return siteConfig;
        }
        return null;
    }

    @Override
    public Map<String, Object> get() {
        List<SiteConfig> siteConfigs = configMapper.get();
        Map<String, Object> map = new HashMap<>();
        for (SiteConfig siteConfig : siteConfigs) {
            String typeName = siteConfig.getType().toLowerCase();
            switch (typeName) {
                case "boolean", "bool" -> map.put(siteConfig.getName(), Boolean.parseBoolean(siteConfig.getValue()));
                case "number", "int", "integer" -> map.put(siteConfig.getName(), Integer.parseInt(siteConfig.getValue()));
                case "json", "gson", "obj", "object" -> map.put(siteConfig.getName(), gson.fromJson(siteConfig.getValue(), Object.class));
                case "str", "string", default -> map.put(siteConfig.getName(), siteConfig.getValue());
            }
        }
        return map;
    }

    @Override
    public Boolean set(String name, String value) {
        SiteConfig siteConfig = new SiteConfig();
        siteConfig.setName(name);
        siteConfig.setValue(value);
        return configMapper.set(siteConfig);
    }
}
