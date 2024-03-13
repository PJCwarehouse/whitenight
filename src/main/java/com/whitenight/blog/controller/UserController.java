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
import org.thymeleaf.util.StringUtils;

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
        return "test";
    }

    @RequestMapping("/NoPermissions")
    public String NoPermissions(){
        return "NoPermissions";
    }


    @RequestMapping("/signup")
    public String disp(){
        System.out.println("跳转到注册页面");
        return "signup";
    }

    //实现注册功能
    //虽然都是signup，这里后端是通过post和get来分辨返回哪个函数
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signUp(String username, String password, Model model){
        if(StringUtils.isEmpty(username)){
            System.out.println("用户名不能为空，请重新输入");
            model.addAttribute("NullMessage", "用户名不能为空，请重新输入");
            return "signup";
        }

        UserEntity userEntity = userMapper.getInfoByUserName(username);
        if(userEntity != null){
            System.out.println("用户名已存在，注册失败");
            model.addAttribute("ErrorMessage", "用户名已存在，注册失败");
            return "signup";
        }
        System.out.println("sign:" + "/n" + username+ "/n" + password);

        userService.Insert(username, password);

        System.out.println("注册用户:(username:" + username + ")");
        model.addAttribute("SuccessSignMessage", "注册成功！");
        return "login";
    }

    //实现用户注销功能
    @RequestMapping(value = "deleteUser",method = RequestMethod.POST)
    public String deleteUser(@RequestParam int userID){
        boolean isUserDeleted = userService.deleteUser(userID);
        if(isUserDeleted){
            System.out.println("注销用户:(userid:" + userID + ")");
        }else {
            System.out.println("用户" + userID + "注销失败");
        }
        return "success";
    }

}

