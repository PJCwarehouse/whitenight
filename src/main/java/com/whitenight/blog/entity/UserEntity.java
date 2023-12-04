package com.whitenight.blog.entity;

import com.whitenight.blog.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserEntity implements UserDetails {


    private int id;
    private String username;
    private String password;

    public void setUserAuthorities(List<String> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    List<String> userAuthorities;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Collection<String> userAuthorities = this.userAuthorities;
        for (String authority : userAuthorities) {
            System.out.println("getAuthorities = " + authority);
            authorities.add(new SimpleGrantedAuthority(authority));//authorities需要GrantedAuthority类型
            // SimpleGrantedAuthority是框架里面授权核心接口GrantedAuthority的实现，所以一般要把authority转换为SimpleGrantedAuthority类型
        }

        return authorities;
    }



}
