package com.ahdark.code.link.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component
public class ShortLink {
    private String key = null;
    private String origin = null;
    private Integer user_id = null;
    private Integer view = null;
    private Timestamp create_time = null;
}
