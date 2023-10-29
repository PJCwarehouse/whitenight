package com.whitenight.blog.service;

import com.whitenight.blog.entity.CommentsEntity;
import com.whitenight.blog.mapper.CommentMapper;
import com.whitenight.blog.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
@Service
public class CommentsService {
    @Resource
    CommentMapper commentMapper;
    @Resource
    UserMapper userMapper;
    public List<CommentsEntity> selectAllCommentsByArticleId(int articleId) {
        return commentMapper.selectAllCommentsByArticleId(articleId);
    }

    public void insert(int userId, String username,int articleId , String content) {
        commentMapper.insert(userId,username,articleId,content);
    }

    public boolean deleteComment(int commentId) {
        commentMapper.deleteCommentById(commentId);
        return true;
    }
}
