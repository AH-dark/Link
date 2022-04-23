package com.ahdark.code.link.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class SiteConfig {
    private String name;
    private String value;
    private String type;
}
