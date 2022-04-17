package com.ahdark.code.link.service;

import com.ahdark.code.link.pojo.User;

import java.util.List;

public interface UserService {
    List<User> getUserByEmail(String email);

    List<User> getUserById(int id);

    boolean setUser(User user);
}
