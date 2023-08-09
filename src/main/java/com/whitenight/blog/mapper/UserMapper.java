package com.whitenight.blog.mapper;

import com.whitenight.blog.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    UserEntity getInfo(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM users WHERE username = #{username}")
    UserEntity getInfoByUserName(@Param("username") String username);

    @Select("select u.username,a.authority from t_user u,t_authority a," +
            "t_user_authority ua where ua.user_id=u.id " +
            "and ua.authority_id=a.id and u.username =?")
    UserEntity hasAuthorities(@Param("username") String username);

    @Insert("insert into users(username,password)values(#{username},#{password})")
    void saveInfo(@Param("username") String username, @Param("password") String password);
}

