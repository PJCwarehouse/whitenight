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

//    private UserService userService;
//    public UserEntity(String username, UserService userService, int id) {
//        this.username = username;
//        this.userService = userService;
//        this.id = id;
//    }这儿有问题，解决了看能不能运行
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        String authority = "ROLE_visitor";
//        int AuthorityId = userService.getAuthorityId(1);
//        if(AuthorityId == 1){
//            authority = "ROLE_admin";
//        } else if (AuthorityId == 2) {
//            authority = "ROLE_visitor";
//        }
//
//        authorities.add(new SimpleGrantedAuthority(authority));//authorities需要GrantedAuthority类型
//        return authorities;
//    }

//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return userService.getAuthorities(this.getId());
//    }
//

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
//
//    private Collection<String> getUserAuthoritiesFromDatabase(int userId) //通过id查询权限
//    {
//        Collection<String> authorities = new ArrayList<>();
//
//        String url = "jdbc:mysql://localhost:3306/wndb";
//        String username = "root";
//        String password = "123";
//        System.out.println("已连接数据库wndb");
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            String query = "SELECT a.authority FROM users u,authority a,user_authority ua WHERE user_id = ? and a.id = ua.authority_id";
//
//            try (PreparedStatement statement = connection.prepareStatement(query)) {
//                statement.setLong(1, userId);
//                try (ResultSet resultSet = statement.executeQuery()) {
//                    while (resultSet.next()) {
//                        authorities.add(resultSet.getString("authority"));
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            // 处理异常
//            e.printStackTrace();
//            // 或者将异常包装成自定义异常抛出
//            throw new RuntimeException("Error while querying user authorities from database", e);
//        }
//
//        return authorities;
//    }


}
