package com.ahdark.code.link.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component
public class ShortLink {
    private String key;
    private String origin;
    private int userId;
    private int view;
    private Timestamp createTime;
}
