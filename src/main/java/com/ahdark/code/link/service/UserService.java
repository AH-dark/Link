package com.ahdark.code.link.service;

import com.ahdark.code.link.pojo.User;

public interface UserService {
    User getUserByEmail(String email);

    User getUserById(int id);

    boolean setUser(User user);

    boolean updateUserInfo(User user);

    boolean updateLoginTime(int id);
}
