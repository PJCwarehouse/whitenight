package com.whitenight.blog.service;

import com.whitenight.blog.entity.ArticleEntity;
import com.whitenight.blog.mapper.ArticleMapper;
import com.whitenight.blog.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ArticleService {
    @Resource
    ArticleMapper articleMapper;
    @Resource
    CommentMapper commentMapper;

    public void Insert(String title, String content) {
        articleMapper.saveInfo(title, content);
    }

    public List<ArticleEntity> selectAllArticles() {
        return articleMapper.selectAllArticles();
    }

    public ArticleEntity selectArticlesById(int id) {
        return articleMapper.selectArticlesById(id);
    }

    public void deleteArticle(int articleID){
        commentMapper.deleteCommentsByArticle(articleID);
        articleMapper.deleteArticle(articleID);
    }

    public void updateArticle(int id,String title, String content) {
        articleMapper.updateArticle(id,title,content);
    }


//    public List<CommentsEntity> selectAllCommentsByArticleId(int articleId) {
//        return articleMapper.selectAllCommentsByArticleId(articleId);
//    }

}
