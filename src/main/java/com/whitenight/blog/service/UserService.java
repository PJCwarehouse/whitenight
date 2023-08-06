package com.whitenight.blog.service;

import com.whitenight.blog.entity.User;
import com.whitenight.blog.entity.UserEntity;
import com.whitenight.blog.mapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    //将dao层属性注入service层
    @Resource
    private UserMapper userMapper;

    public User LoginIn(String username, String password) {
        System.out.println("username,password");
        return userMapper.getInfo(username, password);
    }

    public void Insert(String username, String password) {
        System.out.println("username,password");

        userMapper.saveInfo(username, password);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 在此处根据用户名从数据库中获取用户信息并返回UserDetails对象
        // 注意，UserDetails是Spring Security提供的接口，您需要根据User实体类创建一个UserDetails对象并返回
        // 如果找不到用户，则可以抛出UsernameNotFoundException异常
        // 示例代码如下，具体根据您的实际数据库结构和逻辑来实现
        System.out.println("username:" + username);
        User user = userMapper.getInfoByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        System.out.println(user);
        UserEntity entity = new UserEntity(user);
// 明明自己User类继承了UserDetails,这里返回即可，对比一下你原来写的
        return entity;

//        return new org.springframework.security.core.userdetails.User(
//                user.getName(),
//                user.getPassword(),
//                // 这里可以添加用户的角色，授权等信息
//                authorities
//        );
    }
}

