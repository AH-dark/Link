package com.ahdark.code.link.service.impl;

import com.ahdark.code.link.dao.UserMapper;
import com.ahdark.code.link.pojo.LimitData;
import com.ahdark.code.link.pojo.User;
import com.ahdark.code.link.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
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
    public List<User> getAllUser(int limit, int skip) {
        LimitData<List<User>> limitData = new LimitData<>();
        limitData.setLimit(limit);
        limitData.setOffset(skip);
        return this.userMapper.getAllUser(limitData);
    }

    @Override
    public Integer getNum() {
        return this.userMapper.getNum();
    }

    @Override
    public Integer getNum(Timestamp start, Timestamp end) {
        Map<String, Timestamp> timestampMap = new HashMap<>();
        timestampMap.put("start", start);
        timestampMap.put("end", end);
        return this.userMapper.getNumByTime(timestampMap);
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

    @Override
    public boolean updateUserInfo(User user) {
        int r = this.userMapper.updateUserInfo(user);
        return r == 1;
    }
}
