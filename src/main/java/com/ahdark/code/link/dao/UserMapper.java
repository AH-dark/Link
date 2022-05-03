package com.ahdark.code.link.dao;

import com.ahdark.code.link.pojo.LimitData;
import com.ahdark.code.link.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<User> getUser(User user);

    List<User> getAllUser(LimitData limitData);

    int getUserNum();

    int setUser(User user);

    int updateLoginTime(User user);

    int updateUserInfo(User user); // according key
}
