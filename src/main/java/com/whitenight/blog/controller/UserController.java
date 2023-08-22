package com.whitenight.blog.controller;

import com.whitenight.blog.service.UserService;
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

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/success")
    public String success(){
        return "success";
    }

    @RequestMapping("/signup")
    public String disp(){
        System.out.println("跳转到注册页面");
        return "/signup";
    }

    @RequestMapping("/home page")
    public String home(){
        System.out.println("跳转到主页");
        return "/home page";
    }

    @RequestMapping("/management")
    public String man(){
        System.out.println("跳转到管理界面");//这里会跳两次，奇怪
        return "/management";
    }


    @RequestMapping("/test")
    public String test(){
//        System.out.println("测试");
        return "/test";
    }

    //实现注册功能
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String signUp(String username, String password){
        System.out.println("sign:" + "/n" + username+ "/n" + password);
        userService.Insert(username, password);
        return "success";
    }

    //实现登录
//    @RequestMapping(value = "/loginIn", method = RequestMethod.POST)
//    public String login(String username, String password){
//        log.info("username:{}",username);
//        log.info("password:{}",password);
//        System.out.println("调用loginIn");
//        UserEntity userEntity = userService.LoginIn(username,password);
//        if (userEntity != null) {
//            return "success";
//        } else {
//            return "error";
//        }
//    }//被.loginProcessingUrl("/login")截取了login的数据，这里不会调用到
}

