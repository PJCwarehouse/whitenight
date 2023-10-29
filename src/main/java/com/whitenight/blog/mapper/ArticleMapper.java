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
//删除文章时，由于文章id是评论的外键，所以要先删除文章对应的评论，再删除文章，此处把删除评论的sql语句写在commentMapper里面了
    @Update("UPDATE article SET title = #{title}, content = #{content} WHERE id = #{id}")
    void updateArticle(@Param("id") int id, @Param("title") String newTitle, @Param("content") String newContent);


}
