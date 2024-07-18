package com.whitenight.blog.mapper;

import com.whitenight.blog.entity.ArticleEntity;
import com.whitenight.blog.entity.CommentsEntity;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface ArticleMapper {
    @Insert("insert into article(title,content,time,author,userId)values(#{title},#{content},#{time},#{author},#{userId})")
    void saveInfo(@Param("title") String title, @Param("content") String content, @Param("time") Date time, @Param("author") String author, @Param("userId") int userId);

    @Select("select * from article")
    List<ArticleEntity> selectAllArticles();

    @Select("select * from article where id = #{id}")
    ArticleEntity selectArticlesByArticleId(@Param("id") int id);

    /**
     * 用户文章管理
     * @param userId
     * @return
     */
    @Select("select * from article where userId = #{userId}")
    List<ArticleEntity> selectArticlesByUserId(@Param("userId") int userId);


    //删除文章时，由于文章id是评论的外键，所以要先删除文章对应的评论，再删除文章，此处把删除评论的sql语句写在commentMapper里面了
    @Delete("delete from article where id = #{id}")
    void deleteArticle(@Param("id") int articleID);

    //更新文章内容
    @Update("UPDATE article SET title = #{title}, content = #{content} WHERE id = #{id}")
    void updateArticle(@Param("id") int id, @Param("title") String newTitle, @Param("content") String newContent);

    // 文章发分页查询
    @Select("SELECT * FROM article ORDER BY id DESC")
    List<ArticleEntity> selectArticleWithPage();
}
