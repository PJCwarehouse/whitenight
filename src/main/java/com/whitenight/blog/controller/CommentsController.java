package com.whitenight.blog.controller;

import com.whitenight.blog.service.CommentsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class CommentsController {
    @Resource
    CommentsService commentsService;

    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public String comment( int userId, String username, int articleId, String content){
        commentsService.insert(userId,username,articleId,content);
        System.out.println("用户id为" + userId + "的用户" + "在id为" + articleId + "文章中发布了评论，内容为:" + content);
        return "Basic/success";
    }

    //删除评论
    @GetMapping("/deleteComment")
    public String deleteComment(@RequestParam int commentId) {
        boolean isDeleted = commentsService.deleteComment(commentId);

        System.out.println("删除id为：" + commentId + " 的评论");
        if (isDeleted) {
            return "Basic/success"; //对于@DeleteMapping，通常不直接返回"success"或"error"这样的字符串
        } else {
            return "Basic/error"; //在RESTful架构中，DELETE请求应该返回适当的HTTP状态码，以指示操作的结果
        }
    }

}
