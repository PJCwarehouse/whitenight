package com.whitenight.blog.mapper;

import com.whitenight.blog.entity.ArticleEntity;
import com.whitenight.blog.entity.CommentsEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Insert("insert into article(title,content)values(#{title},#{content})")
    void saveInfo(@Param("title") String title, @Param("content") String content);

    @Select("select * from article")
    List<ArticleEntity> selectAllArticles();

    @Select("select * from article where id = #{id}")
    ArticleEntity selectArticlesById(@Param("id") int id);

    @Delete("delete from article where id = #{id}")
    void deleteArticle(@Param("id") int articleID);

    @Update("UPDATE article SET title = #{title}, content = #{content} WHERE id = #{id}")
    void updateArticle(@Param("id") int id, @Param("title") String newTitle, @Param("content") String newContent);


}
