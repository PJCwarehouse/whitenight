package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE username = #{username} AND password = #{password}")
    User getInfo(@Param("username") String username, @Param("password") String password);

    @Insert("insert into user(username,password)values(#{username},#{password})")
    void saveInfo(@Param("username") String username, @Param("password") String password);
}

