package com.ahdark.code.link.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LimitData<T> {
    private Integer limit;
    private Integer offset;
    private Integer total;
    private T data = null;
}
