package com.ahdark.code.link.dao;

import com.ahdark.code.link.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMapper {
    List<User> getUser(User user);

    List<Map<String, Object>> setUser(Map<String, Object> paramMap);
}
