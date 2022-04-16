package com.ahdark.code.link.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ShortLink {
    private String key;
    private String origin;
    private Integer user_id;
    private Integer view;
    private Timestamp create_time;
}
