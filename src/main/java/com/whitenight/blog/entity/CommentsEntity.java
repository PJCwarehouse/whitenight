package com.whitenight.blog.entity;

import lombok.Data;

@Data
public class CommentsEntity {
    //评论id
    int id;

    //评论用户id
    int userId;

    //评论用户昵称
    String username;

    //评论内容
    String content;

    //评论文章id
    int articleId;

    @Override
    public String toString() {
        return "CommentsEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", articleId=" + articleId +
                '}';
    }
}
