package com.whitenight.blog.controller;
import com.whitenight.blog.service.UserService;
import com.whitenight.blog.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
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

    @RequestMapping("/login")
    public String loginb(){
        return "loginb";
    }

    @RequestMapping("/success")
    public String success(){
        return "success";
    }

    //实现登录
    @RequestMapping(value = "/loginIn", method = RequestMethod.POST)
    public String login(String username, String password){
        log.info("username:{}",username);
        log.info("password:{}",password);
        System.out.println("调用loginIn");
        UserDetails user = userService.loadUserByUsername(username);

        if(user!=null){
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping("/signup")
    public String disp(){
        System.out.println("跳转到注册页面");
        return "signupb";
    }

    //实现注册功能
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String signUp(String username, String password){
        System.out.println("调用register");

        userService.Insert(username, password);
        return "success";
    }

}

