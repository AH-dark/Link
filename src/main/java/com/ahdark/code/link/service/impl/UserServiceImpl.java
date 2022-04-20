package com.ahdark.code.link.service.impl;

import com.ahdark.code.link.dao.UserMapper;
import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.CodeInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ahdark.code.link.utils.CodeInfo.*;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return this.userMapper.getUser(user).get(1);
    }

    @Override
    public User getUserById(int id) {
        User user = new User();
        user.setId(id);
        return this.userMapper.getUser(user).get(1);
    }

    @Override
    public boolean setUser(User user) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", user.getName());
        paramMap.put("email", user.getEmail());
        paramMap.put("password", user.getPassword());
        paramMap.put("register_ip", user.getRegister_ip());
        int result = this.userMapper.setUser(paramMap);
        return result!=0;
    }
}
