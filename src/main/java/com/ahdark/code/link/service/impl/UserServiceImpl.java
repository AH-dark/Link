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
    public List<User> getUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return this.userMapper.getUserByEmail(user);
    }

    @Override
    public List<User> getUserById(int id) {
        User user = new User();
        user.setId(id);
        return this.userMapper.getUserById(user);
    }

    @Override
    public boolean setUser(User user) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", user.getName());
        paramMap.put("email", user.getEmail());
        paramMap.put("password", user.getPassword());
        paramMap.put("register_ip", user.getRegister_ip());
        List<Map<String, Object>> result = this.userMapper.setUser(paramMap);
        return !result.isEmpty();
    }
}
