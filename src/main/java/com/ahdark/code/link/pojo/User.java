package com.ahdark.code.link.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Timestamp create_time;
    private String register_ip;
    private Timestamp login_time;
    private boolean available;
}
