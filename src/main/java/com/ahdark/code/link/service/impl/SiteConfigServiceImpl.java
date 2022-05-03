package com.ahdark.code.link.service.impl;

import com.ahdark.code.link.dao.ConfigMapper;
import com.ahdark.code.link.pojo.SiteConfigRow;
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
        List<SiteConfigRow> siteConfigRows = configMapper.get(name);
        if (!siteConfigRows.isEmpty()) {
            String typeName = siteConfigRows.get(0).getType();
            String value = siteConfigRows.get(0).getValue();
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
        List<SiteConfigRow> siteConfigRows = configMapper.get();
        Map<String, Object> map = new HashMap<>();
        for (SiteConfigRow siteConfigRow : siteConfigRows) {
            String typeName = siteConfigRow.getType().toLowerCase();
            switch (typeName) {
                case "bool":
                case "boolean":
                    map.put(siteConfigRow.getName(), Boolean.parseBoolean(siteConfigRow.getValue()));
                    break;
                case "number":
                case "int":
                case "integer":
                    map.put(siteConfigRow.getName(), Integer.parseInt(siteConfigRow.getValue()));
                    break;
                case "json":
                case "obj":
                case "object":
                case "gson":
                    map.put(siteConfigRow.getName(), gson.fromJson(siteConfigRow.getValue(), Object.class));
                    break;
                case "str":
                case "string":
                default:
                    map.put(siteConfigRow.getName(), siteConfigRow.getValue());
                    break;
            }
        }
        return map;
    }

    @Override
    public Boolean set(String name, String value) {
        SiteConfigRow siteConfigRow = new SiteConfigRow();
        siteConfigRow.setName(name);
        siteConfigRow.setValue(value);
        return configMapper.set(siteConfigRow);
    }
}
