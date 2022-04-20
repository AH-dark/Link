package com.ahdark.code.link.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component
public class User {
    private Integer id = null;
    private String name = null;
    private String email = null;
    private String password = null;
    private Timestamp create_time = null;
    private String register_ip = null;
    private Timestamp login_time = null;
    private Boolean available = null;
}
