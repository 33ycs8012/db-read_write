package com.javaboy.dbread_write.mapper;

import com.javaboy.dbread_write.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: 杨春升
 * @time: 2021/1/8 10:23
 */
@Mapper
public interface UserMapper {

    Integer addUser(User user);

    List<User> getAll();
}
