package com.ahdark.code.link.service.impl;

import com.ahdark.code.link.dao.UserMapper;
import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        List<User> results = this.userMapper.getUser(user);
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0); // Email is unique
        }
    }

    @Override
    public User getUserById(int id) {
        User user = new User();
        user.setId(id);
        List<User> results = this.userMapper.getUser(user);
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0); // ID is unique
        }
    }

    @Override
    public boolean setUser(User user) {
        int result = this.userMapper.setUser(user);
        return result != 0;
    }

    @Override
    public boolean updateLoginTime(int id) {
        User user = new User();
        user.setId(id);
        int result = this.userMapper.updateLoginTime(user);
        return result != 0;
    }
}
