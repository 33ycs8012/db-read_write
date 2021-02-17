package com.javaboy.dbread_write.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.javaboy.dbread_write.mapper.UserMapper;
import com.javaboy.dbread_write.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: 杨春升
 * @time: 2021/1/8 10:27
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @DS("master")
    public Integer addUser(User user){
        return userMapper.addUser(user);
    }

    @DS("slave")
    public List<User> getAll(){
        return userMapper.getAll();
    }

}
