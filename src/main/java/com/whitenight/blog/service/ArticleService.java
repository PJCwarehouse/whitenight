package com.whitenight.blog.service;

import com.whitenight.blog.entity.ArticleEntity;
import com.whitenight.blog.mapper.ArticleMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ArticleService {
    @Resource
    ArticleMapper articleMapper;

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
        articleMapper.deleteArticle(articleID);
    }

    public void updateArticle(int id,String title, String content) {
        articleMapper.updateArticle(id,title,content);
    }
}
