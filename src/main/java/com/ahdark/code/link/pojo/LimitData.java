package com.ahdark.code.link.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LimitData {
    private int limit;
    private int offset;
}
