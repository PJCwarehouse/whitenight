package com.whitenight.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whitenight.blog.entity.ArticleEntity;
import com.whitenight.blog.mapper.ArticleMapper;
import com.whitenight.blog.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ArticleService {
    @Resource
    ArticleMapper articleMapper;
    @Resource
    CommentMapper commentMapper;

    public void Insert(String title, String content, Date time,String author,int userId) {
        articleMapper.saveInfo(title, content,time,author,userId);
    }

    //根据id升序返回文章列表
    public List<ArticleEntity> selectAllArticles() {
        return articleMapper.selectAllArticles();
    }

    //根据id降序返回文章列表，注意返回的数据类型不同
    public PageInfo<ArticleEntity> selectArticleWithPage(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<ArticleEntity> articleList = articleMapper.selectArticleWithPage();
        // 封装文章统计热度数据
//        for (int i = 0; i < articleList.size(); i++) {
//            ArticleEntity article = articleList.get(i);
//            Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
//            article.setHits(statistic.getHits());
//            article.setCommentsNum(statistic.getCommentsNum());
//        }
        PageInfo<ArticleEntity> pageInfo = new PageInfo<>(articleList);
        return pageInfo;
    }

    public ArticleEntity selectArticlesByArticleId(int id) {
        return articleMapper.selectArticlesByArticleId(id);
    }

    public List<ArticleEntity> selectArticlesByUserId(int userId){
        return articleMapper.selectArticlesByUserId(userId);
    }
    public void deleteArticle(int articleID){
        commentMapper.deleteCommentsByArticle(articleID);
        articleMapper.deleteArticle(articleID);
    }

    public void updateArticle(int id,String title, String content) {
        articleMapper.updateArticle(id,title,content);
    }




}
