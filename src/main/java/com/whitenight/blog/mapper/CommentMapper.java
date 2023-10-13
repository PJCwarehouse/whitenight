package com.whitenight.blog.mapper;

import com.whitenight.blog.entity.ArticleEntity;
import com.whitenight.blog.entity.CommentsEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface CommentMapper {

    @Select("select * from comments where articleId = #{articleId}")
    List<CommentsEntity> selectAllCommentsByArticleId(@Param("articleId") int articleId);

    @Insert("insert into comments(userId, username, articleId, content) values (#{userId}, #{username}, #{articleId}, #{content})")
    void insert(@Param("userId") int userId, @Param("username") String username, @Param("articleId") int articleId, @Param("content") String content);


}

