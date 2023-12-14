package com.whitenight.blog.service;

import com.whitenight.blog.entity.UserEntity;
import com.whitenight.blog.mapper.ArticleMapper;
import com.whitenight.blog.mapper.CommentMapper;
import com.whitenight.blog.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.html.parser.Entity;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    //将dao层属性注入service层
    @Resource
    private UserMapper userMapper;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CommentMapper commentMapper;
    private String username;
    private int id;
    public String getUsername(){
        return username;
    }
    public int getId(){
        return id;
    }

    public UserEntity LoginIn(String username, String password) {
        System.out.println("username,password");
        return userMapper.getInfo(username, password);
    }

    public boolean deleteUser(int userId) {
        if(userId == 1){
            return false;
        }
        commentMapper.deleteCommentByUserId(userId);
        userMapper.deleteUserAuthority(userId);
        userMapper.deleteUser(userId);

        return true;
    }
    public void Insert(String username, String password) {
        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setPassword(password);
        userMapper.saveInfo(entity);
        userMapper.saveInfoAuthority(entity.getId(),2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 在此处根据用户名从数据库中获取用户信息并返回UserDetails对象
        // 注意，UserDetails是Spring Security提供的接口，您需要根据User实体类创建一个UserDetails对象并返回
        // 如果找不到用户，则可以抛出UsernameNotFoundException异常
        // 如果找到两个相同的用户名，也会报错
        System.out.println("username:" + username);
        UserEntity entity = userMapper.getInfoByUserName(username);
        this.username = username;//通过登录信息获取username和id
        this.id = entity.getId();
        System.out.println("当前登录用户名:" + entity.getUsername());
        List<String> authorties = userMapper.getAuthoritiesByUserId(entity.getId());
        System.out.println("authorties=" + authorties.toString());
        entity.setUserAuthorities(authorties);
        return entity;
    }

}

