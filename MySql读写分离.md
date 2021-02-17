[TOC]

## MySql读写分离

使用外部工具：[https://github.com/baomidou/dynamic-datasource-spring-boot-starter](https://github.com/baomidou/dynamic-datasource-spring-boot-starter)



测试案例项目结构：

![image-20210108104213711](C:\Users\杨春升\AppData\Roaming\Typora\typora-user-images\image-20210108104213711.png)

### 1. 添加依赖

```xml
 <dependency>
     <groupId>com.baomidou</groupId>
     <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
     <version>3.3.1</version>
 </dependency>
```

### 2. 添加application.yml文件，配置主从数据库

![image-20210108104323088](C:\Users\杨春升\AppData\Roaming\Typora\typora-user-images\image-20210108104323088.png)

```yml
spring:
  datasource:
    dynamic:
      primary: master #设置默认的数据源或者数据源组,默认值即为master
      strict: false #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源.
      datasource:
        master:
          url: jdbc:mysql://123.57.109.177:33061/testdb
          username: root
          password: 123
        slave:
          url: jdbc:mysql://123.57.109.177:33062/testdb
          username: root
          password: 123
```

### 3. 创建model、service、mapper文件

#### User.java

```java
public class User {

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

#### UserService.java

```java
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
```

#### UserMapper.java

```java
@Mapper
public interface UserMapper {
    Integer addUser(User user);
    List<User> getAll();
}
```

#### UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        " http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.javaboy.dbread_write.mapper.UserMapper">

    <insert id="addUser" keyProperty="id" useGeneratedKeys="true">
        insert into user (name) values(#{name});
    </insert>
    <select id="getAll" resultType="com.javaboy.dbread_write.model.User">
        select * from user;
    </select>
</mapper>
```

#### pom.xml

在build中加入：

```xml
<resources>
    <resource>
        <directory>src/main/java</directory>
        <includes>
            <include>**/*.xml</include>
        </includes>
    </resource>
    <resource>
        <directory>src/main/resources</directory>
    </resource>
</resources>
```

### 4. 测试

```java
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
```

