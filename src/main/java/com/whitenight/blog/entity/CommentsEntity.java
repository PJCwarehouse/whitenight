package com.whitenight.blog.entity;

public class CommentsEntity {
    int id;//评论id
    int userId;//评论用户id
    String username;//评论用户昵称
    String content;//评论内容
    int articleId;//评论文章id
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public String getContent(){
        return this.content;
    }
    public int getArticleId(){
        return this.articleId;
    }

    public String getUsername(){
        return this.username;
    }
}
