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

//    @Select("SELECT a.id FROM users u,authority a,user_authority ua " +
//            "WHERE u.id = #{id} and u.id = ua.user_id and a.id = ua.authority_id")
//    int getAuthorityId(@Param("id") int id);

    @Insert("insert into users(username,password)values(#{username},#{password})")
    void saveInfo(@Param("username") String username, @Param("password") String password);
}

