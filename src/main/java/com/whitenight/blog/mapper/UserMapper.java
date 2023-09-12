package com.whitenight.blog.mapper;

import com.whitenight.blog.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    UserEntity getInfo(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM users WHERE username = #{username}")
    UserEntity getInfoByUserName(@Param("username") String username);

//    @Select("SELECT a.id FROM users u,authority a,user_authority ua " +
//            "WHERE u.id = #{id} and u.id = ua.user_id and a.id = ua.authority_id")
//    int getAuthorityId(@Param("id") int id);

    @Insert("insert into users(username,password)values(#{username},#{password})")
    void saveInfo(@Param("username") String username, @Param("password") String password);


    //@Select("SELECT a.authority FROM users u,authority a,user_authority ua WHERE user_id = #{user_id} and a.id = ua.authority_id") 为什么这条语句会有多个重复结果？
    @Select("SELECT a.authority FROM users u,authority a,user_authority ua WHERE u.id = #{user_id} and u.id=ua.user_id and a.id = ua.authority_id")
    List<String> getAuthoritiesByUserId(@Param("user_id") int userId);
}

