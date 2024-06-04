package com.whitenight.blog.controller;

import com.whitenight.blog.entity.ArticleEntity;
import com.whitenight.blog.service.ArticleService;
import com.whitenight.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HtmlController {
    @Autowired
    UserService userService;

    @Autowired
    ArticleService articleService;

    @RequestMapping("/success")
    public String success(){
        return "success";
    }

    @RequestMapping("/loginError")
    public String loginError(){
        return "loginError";
    }

    @RequestMapping("/home page")
    public String home(){
        System.out.println("跳转到主页");
        return "home page";
    }

    @RequestMapping("/test")
    public String test(){
        return "test";
    }

    @RequestMapping("/NoPermissions")
    public String NoPermissions(){
        return "NoPermissions";
    }


    @RequestMapping("/personalHome")
    public String personalHome(Model model){
        int userId = userService.getId();
        String username = userService.getUsername();
        List<ArticleEntity> articles = articleService.selectArticlesByUserId(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("username", username);
        model.addAttribute("articles", articles);
        return "personalHome";
    }
}
