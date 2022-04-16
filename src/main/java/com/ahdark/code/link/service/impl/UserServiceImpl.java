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
    public List<User> getUser(String email) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("email", email);
        return this.userMapper.getUser(paramMap);
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
