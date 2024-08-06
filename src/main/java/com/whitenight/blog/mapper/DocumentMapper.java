package com.whitenight.blog.mapper;

import com.whitenight.blog.entity.DocumentEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DocumentMapper {

//    插入文件和目录
    @Insert("insert into document(type,userId,parentId,name,time,size,sizeUnit,visibleType)values(#{type}, #{userId}, #{parentId}, #{name}, #{time}, #{size}, #{sizeUnit}, #{visibleType})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")//想一想怎么能把自增的id取出来
    void insert(DocumentEntity DocumentEntity);

//    设置文件Url
    @Update("UPDATE document SET url = #{url} WHERE id = #{id}")
    void updateUrl(@Param("id") int id, @Param("url") String fileDownloadUrl);

//    通过id查询文件具体信息
    @Select("SELECT * FROM document WHERE id = #{id}")
    DocumentEntity select(@Param("id") int id);

//    查询父目录的子文件或者子目录
    @Select("SELECT * FROM document WHERE parentId = #{parentId} AND type = #{type}")
    List<DocumentEntity> getList(@Param("parentId") int directoryId, @Param("type") String type);

//    删除文件
    @Delete("DELETE from document WHERE id = #{fileId}")
    void deleteFile(@Param("fileId") int fileId);

//    删除用户id对应的文件和目录
    @Delete("DELETE from document WHERE userId = #{userId}")
    void deleteByUserId(@Param("userId") int userId);

//    更改目录私密性
    @Update("UPDATE document SET visibleType = #{visibleType} where id = #{directoryId}")
    void updateOpenness(@Param("directoryId") int directoryId, @Param("visibleType") int visibleType);

    @Select("SELECT * FROM document WHERE name LIKE CONCAT('%', #{searchString}, '%') AND type = 'directory'")
    List<DocumentEntity> searchResult(@Param("searchString") String searchString);


}
