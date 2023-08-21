package com.whitenight.blog.mapper;

import com.whitenight.blog.entity.ArticleEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ArticleMapper {
    @Insert("insert into article(title,content)values(#{title},#{content})")
    void saveInfo(@Param("title") String title, @Param("content") String content);

    @Select("select * from article")
    List<ArticleEntity> selectAllArticles();

    @Select("select * from article where id = #{id}")
    ArticleEntity selectArticlesById(@Param("id") int id);
}
