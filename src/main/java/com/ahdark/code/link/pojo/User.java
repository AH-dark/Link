package com.ahdark.code.link.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component
public class User {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private Integer role;
    private String description;
    private String registerIP;
    private Timestamp createTime;
    private Timestamp loginTime;
    private Boolean available;
}
