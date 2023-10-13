package com.whitenight.blog.controller;

import com.whitenight.blog.service.CommentsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
@Controller
public class CommentsController {
    @Resource
    CommentsService commentsService;

    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public String comment(int userId,String username,int articleId,String content){
        commentsService.insert(userId,username,articleId,content);

        return "/success";
    }


}
