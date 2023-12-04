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
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")//想一想怎么能把自增的id取出来
    void saveInfo(UserEntity entity);

    @Insert("insert into user_authority(user_id,authority_id)values(#{user_id},#{authority_id})")
    void saveInfoAuthority(@Param("user_id") int user_id, @Param("authority_id") int authority_id);

    @Select("SELECT a.authority FROM users u " +
            "INNER JOIN user_authority ua ON u.id = ua.user_id " +
            "INNER JOIN authority a ON a.id = ua.authority_id " +
            "WHERE u.id = #{user_id}")
    List<String> getAuthoritiesByUserId(@Param("user_id") int userId);

    @Delete("delete from users where id = #{userId}")
    void deleteUser(@Param("userId") int userId);

    @Delete("delete from user_authority where user_id = #{userId}")
    void deleteUserAuthority(@Param("userId") int userId);
}

