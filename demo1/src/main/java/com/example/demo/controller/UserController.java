package com.example.demo.controller;
import lombok.extern.log4j.Log4j2;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.annotation.Resource;


@Log4j2
@Controller
public class UserController {
    //将Service注入Web层
    @Resource
    UserService userService;

    //实现登录
    @RequestMapping("/login")
    public String show(){
        return "login";
    }
    @RequestMapping(value = "/loginIn",method = RequestMethod.POST)
    public String login(String username,String password){
        User user = userService.LoginIn(username, password);
        log.info("name:{}",username);
        log.info("password:{}",password);
        if(user!=null){
            return "redirect:/index.html";
        }else {
            return "error";
        }
    }
    @RequestMapping("/index.html")
    public String mainPage(){
        return "index";
    }
    @RequestMapping("/signup")
    public String disp(){
        return "signup";
    }

    //实现注册功能
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String signUp(String username,String password){
        userService.Insert(username, password);
        return "success";
    }
}

