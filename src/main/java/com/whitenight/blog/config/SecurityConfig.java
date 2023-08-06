package com.whitenight.blog.config;

import com.whitenight.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        System.out.println("configure http");
//        http
//                .authorizeRequests()
//                .antMatchers( "/signup","/success").permitAll() // 允许访问登录和注册页面
//                .anyRequest().authenticated() // 其他请求需要认证
//                .and()
//                .formLogin()
//                .loginPage("/login") // 指定自定义的登录页面
//                .loginProcessingUrl("/loginIn") // 配置登录表单提交路径为POST方法
//                .defaultSuccessUrl("/success") // 登录成功后跳转的页面
//                .permitAll()
//                .and()
//                .logout()
//                .logoutUrl("/logout") // 指定登出的URL
//                .logoutSuccessUrl("/login") // 登出成功后跳转的页面
//                .permitAll();
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/error").permitAll() //error 放开权限 ，不然登陆失败跳转不过来
                .anyRequest().authenticated();

        http.formLogin().loginPage("/login")
                .loginProcessingUrl("/loginIn") // 配置登录表单提交路径为POST方法
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.sendRedirect("/"); // 成功跳转到主页
                    }
                }).failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        httpServletResponse.sendRedirect("/error"); // 失败跳error
                    }
                }).permitAll() //这个permitAll的意思是 /login /loginIn 这两个接口不需要权限（不然未登录用户没法登录）
                .and().csrf().disable();


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("configure auth");
        auth.userDetailsService(userService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

}
