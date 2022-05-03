package com.ahdark.code.link.service;

import com.ahdark.code.link.pojo.User;

import java.util.List;

public interface UserService {
    User getUserByEmail(String email);

    User getUserById(int id);

    List<User> getAllUser(int limit, int skip);

    int getUserNum();

    boolean setUser(User user);

    boolean updateUserInfo(User user);

    boolean updateLoginTime(int id);
}
