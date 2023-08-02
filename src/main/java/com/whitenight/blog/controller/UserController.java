package com.whitenight.blog.controller;
import com.whitenight.blog.service.UserService;
import com.whitenight.blog.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.annotation.Resource;


@Slf4j
@Controller
public class UserController {
    //将Service注入Web层
    @Resource
    UserService userService;

    //实现登录
    @RequestMapping("/login")
    public String show(){
        return "loginb";
    }

    @RequestMapping(value = "/loginIn", method = RequestMethod.POST)
    public String login(String username, String password){
        log.info("username:{}",username);
        log.info("password:{}",password);
        User user = userService.LoginIn(username, password);
        if(user!=null){
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping("/signup")
    public String disp(){
        return "signupb";
    }

    //实现注册功能
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String signUp(String username, String password){
        userService.Insert(username, password);
        return "success";
    }

}

