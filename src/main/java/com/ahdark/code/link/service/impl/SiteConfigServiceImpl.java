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
    public Object get(String name) {
        List<SiteConfig> siteConfigs = configMapper.get(name);
        if (!siteConfigs.isEmpty()) {
            String typeName = siteConfigs.get(0).getType();
            String value = siteConfigs.get(0).getValue();
            switch (typeName) {
                case "bool":
                case "boolean":
                    return Boolean.parseBoolean(value);
                case "number":
                case "int":
                case "integer":
                    return Integer.parseInt(value);
                case "json":
                case "obj":
                case "object":
                case "gson":
                    return gson.fromJson(value, Object.class);
                case "str":
                case "string":
                default:
                    return value;
            }
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
                case "bool":
                case "boolean":
                    map.put(siteConfig.getName(), Boolean.parseBoolean(siteConfig.getValue()));
                    break;
                case "number":
                case "int":
                case "integer":
                    map.put(siteConfig.getName(), Integer.parseInt(siteConfig.getValue()));
                    break;
                case "json":
                case "obj":
                case "object":
                case "gson":
                    map.put(siteConfig.getName(), gson.fromJson(siteConfig.getValue(), Object.class));
                    break;
                case "str":
                case "string":
                default:
                    map.put(siteConfig.getName(), siteConfig.getValue());
                    break;
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
