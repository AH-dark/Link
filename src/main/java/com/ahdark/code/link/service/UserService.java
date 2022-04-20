package com.ahdark.code.link.service;

import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.utils.CodeInfo;

public interface UserService {
    User getUserByEmail(String email);

    User getUserById(int id);

    boolean setUser(User user);

    boolean updateLoginTime(int id);
}
