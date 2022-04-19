package com.ahdark.code.link.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ShortLink {
    private String key = null;
    private String origin = null;
    private Integer user_id = null;
    private Integer view = null;
    private Timestamp create_time = null;
}
