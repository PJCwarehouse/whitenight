package com.whitenight.blog.service;

import com.whitenight.blog.entity.DirectoryEntity;
import com.whitenight.blog.entity.UserEntity;
import com.whitenight.blog.mapper.ArticleMapper;
import com.whitenight.blog.mapper.CommentMapper;
import com.whitenight.blog.mapper.DirectoryMapper;
import com.whitenight.blog.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    //将dao层属性注入service层
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    DirectoryService directoryService;

    private int id;
    private String username;

    public int getId(){
        return id;
    }

    public String getUsername(){
        return username;
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
        //先把数据加入到用户表里面
        userMapper.saveInfo(entity);
        //然后将用户表对应的id和权限存入权限表里面
        userMapper.saveInfoAuthority(entity.getId(),2);
        //在注册用户的时候就创建对应的个人目录
        directoryService.createDirectory(entity.getId(), 0, username);
    }

    public void logout(){
        this.id = 0;
        this.username = null;
        System.out.println("用户已登出");
    }

    public UserEntity selectUserNameById(int id){
        return userMapper.selectUserNameById(id);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 在此处根据用户名从数据库中获取用户信息并返回UserDetails对象
        // 注意，UserDetails是Spring Security提供的接口，您需要根据User实体类创建一个UserDetails对象并返回
        // 如果找不到用户，则可以抛出UsernameNotFoundException异常
        // 如果找到两个相同的用户名，也会报错
        UserEntity entity = userMapper.getInfoByUserName(username);
        this.username = username;//通过登录信息获取username和id
        this.id = entity.getId();
        System.out.println("当前登录用户名:" + entity.getUsername());
        List<String> authorties = userMapper.getAuthoritiesByUserId(entity.getId());
        System.out.println("当前用户权限为:" + authorties.toString());
        entity.setUserAuthorities(authorties);
        return entity;
    }

}

