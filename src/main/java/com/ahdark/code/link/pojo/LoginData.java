package com.ahdark.code.link.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LoginData {
    private String email;
    private String password;
    private boolean remember;
}
