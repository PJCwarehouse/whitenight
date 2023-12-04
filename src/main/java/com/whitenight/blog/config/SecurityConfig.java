package com.whitenight.blog.config;

import com.whitenight.blog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired//依赖注入
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//表示开启权限设置
                .antMatchers("login","/success","/error","/articles",
                        "../lib/css/bootstrap.min.css","../bootstrap.bundle.min.js","/signup","/loginError","/register","/signupError","signupError")
                .permitAll() //error 放开权限 ，不然登陆失败跳转不过来
                .antMatchers("/home page").hasAnyRole("visitor","admin")
                .antMatchers("/management").hasRole("admin");//数据库权限名加了ROLE前缀，这里用hasRole方便，不用hasAnyAuthority
//                .anyRequest().permitAll();//方便测试
//                .anyRequest().authenticated();
        //authenticated()要求在执行该请求时， 必须已经登录了应用。
        // 如果用户没有认证的话，Spring Security的Filter将会捕获该请求，并将用户重定向到应用的登录页面。
        // 同时，permitAll()方法允许请求没有任何的安全限制。

        http.formLogin().loginPage("/login")
//                .loginProcessingUrl("/login") // 默认提交路径为login，可以不用配置
                .usernameParameter("username")
                .passwordParameter("password")// 成功跳转到主页


                //强行把登录成功的跳转页面定位到https，不然会跳到http://www.masteryy.top/index
//                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
//                    httpServletResponse.sendRedirect("/index");
//                })
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    httpServletResponse.sendRedirect("https://www.masteryy.top/index");
                })

                .failureHandler((httpServletRequest, httpServletResponse, e) -> {
                    log.error("Login failed: {}", e.getMessage());
                    httpServletResponse.sendRedirect("/loginError"); // 失败跳error
//                        String errorMessage = "用户名或密码错误，请重试"; // 自定义错误消息，之后在html界面利用Thymeleaf设置一下，使其显示在网页上
//                        httpServletRequest.getSession().setAttribute("errorMessage", errorMessage);
//                        // 将用户重定向到登录页面并显示错误消息
//                        httpServletResponse.sendRedirect("/login");

                })
                .permitAll() //这个permitAll的意思是 /login /loginIn 这两个接口不需要权限（不然未登录用户没法登录）
                .and().csrf().disable();

        super.configure(http);
        http.headers().frameOptions().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("configure auth");
        auth.userDetailsService(userService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }



}
