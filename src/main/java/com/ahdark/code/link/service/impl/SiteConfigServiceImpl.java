package com.ahdark.code.link.service.impl;

import com.ahdark.code.link.dao.ConfigMapper;
import com.ahdark.code.link.pojo.SiteConfig;
import com.ahdark.code.link.service.SiteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SiteConfigServiceImpl implements SiteConfigService {
    @Autowired
    private ConfigMapper configMapper;

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
    public Map<String, String> get() {
        List<SiteConfig> siteConfigs = configMapper.get();
        Map<String, String> map = new HashMap<>();
        for (SiteConfig siteConfig : siteConfigs) {
            map.put(siteConfig.getName(), siteConfig.getValue());
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
