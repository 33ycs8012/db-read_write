package com.javaboy.dbread_write;

import com.javaboy.dbread_write.model.User;
import com.javaboy.dbread_write.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DbReadWriteApplicationTests {
    @Autowired
    UserService userService;
    @Test
    void contextLoads() {
        User user = new User();
        user.setName("小明");
        Integer integer = userService.addUser(user);
        System.out.println(integer);
    }
    @Test
    void getAll(){
        List<User> all = userService.getAll();
        System.out.println(all.toString());
    }
}
