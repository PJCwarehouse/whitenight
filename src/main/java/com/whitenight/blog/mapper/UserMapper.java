package com.whitenight.blog.mapper;

import com.whitenight.blog.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM t_user WHERE username = #{username} AND password = #{password}")
    User getInfo(@Param("username") String username, @Param("password") String password);
    @Insert("insert into t_user(username,password)values(#{username},#{password})")
    void saveInfo(@Param("username") String username, @Param("password") String password);
}

