package com.ahdark.code.link.config;

import com.ahdark.code.link.service.SiteConfigService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class InitEvent implements ApplicationRunner {
    private final Gson gson = new Gson();
    @Autowired
    SiteConfigService siteConfigService;

    @Override
    public void run(ApplicationArguments args) {
        Map<String, Object> siteConfigs = siteConfigService.get();
        log.info("Get site config success: {}", gson.toJson(siteConfigs));
    }
}
