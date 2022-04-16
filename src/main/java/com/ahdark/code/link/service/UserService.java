package com.ahdark.code.link.service;

import com.ahdark.code.link.pojo.User;

import java.util.List;

public interface UserService {
    List<User> getUser(String email);

    boolean setUser(User user);
}
