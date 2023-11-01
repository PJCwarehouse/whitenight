package com.whitenight.blog.mapper;

import com.whitenight.blog.entity.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    UserEntity getInfo(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM users WHERE username = #{username}")
    UserEntity getInfoByUserName(@Param("username") String username);


    @Insert("insert into users(username,password)values(#{username},#{password})")
    void saveInfo(@Param("username") String username, @Param("password") String password);

    @Select("SELECT a.authority FROM users u " +
            "INNER JOIN user_authority ua ON u.id = ua.user_id " +
            "INNER JOIN authority a ON a.id = ua.authority_id " +
            "WHERE u.id = #{user_id}")
    List<String> getAuthoritiesByUserId(@Param("user_id") int userId);

    @Delete("delete from users where id = #{userId}")
    void deleteUser(@Param("userId") int userId);
}

