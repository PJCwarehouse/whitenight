package com.whitenight.blog.controller;

import com.whitenight.blog.entity.UserEntity;
import com.whitenight.blog.mapper.UserMapper;
import com.whitenight.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;


@Slf4j
@Controller
public class UserController {

    @Resource
    UserMapper userMapper;
    //将Service注入Web层
    @Resource
    UserService userService;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/loginError")
    public String loginError(){
        return "loginError";
    }
    @RequestMapping("/success")
    public String success(){
        return "success";
    }

    @RequestMapping("/signup")
    public String disp(){
        System.out.println("跳转到注册页面");
        return "signup";
    }

    @RequestMapping("/home page")
    public String home(){
        System.out.println("跳转到主页");
        return "home page";
    }

    @RequestMapping("/management")
    public String man(){
        System.out.println("跳转到管理界面");
        return "management";
    }


    @RequestMapping("/test")
    public String test(){
//        System.out.println("测试");
        return "test";
    }

    //实现注册功能
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String signUp(String username, String password, Model model){
        UserEntity userEntity = userMapper.getInfoByUserName(username);
        if(userEntity != null){
            System.out.println("用户名已存在，注册失败");
            model.addAttribute("errorMessage", "用户名已存在，注册失败");
            return "signupError";
        }
        System.out.println("sign:" + "/n" + username+ "/n" + password);

        userService.Insert(username, password);

        System.out.println("注册用户:(username:" + username + ")");

        return "success";
    }

    //实现用户注销功能
    @RequestMapping(value = "deleteUser",method = RequestMethod.POST)
    public ResponseEntity<String> deleteUser(@RequestParam int userId){
        boolean isUserDeleted = userService.deleteUser(userId);
        if(isUserDeleted) {
            System.out.println("注销用户:(userid:" + userId + ")");
            return ResponseEntity.ok("用户删除成功");
        }else{
            return ResponseEntity.notFound().build();
        }

    }

}

