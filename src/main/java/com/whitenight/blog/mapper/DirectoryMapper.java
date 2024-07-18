package com.whitenight.blog.mapper;

import com.whitenight.blog.entity.DirectoryDataEntity;
import com.whitenight.blog.entity.DirectoryEntity;
import com.whitenight.blog.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;

@Mapper
public interface DirectoryMapper {


//    获取当前用户拥有的目录
    @Select("select * from directory WHERE userId = #{userId}")
    List<DirectoryEntity> getDirectoryListByUserId(@Param("userId") int userId);

//    创建目录
    @Insert("insert into directory(userId, parent_tableId, directoryName, openness)values(#{userId}, #{parent_tableId}, #{directoryName}, #{openness})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")//想一想怎么能把自增的id取出来
    void createDirectory(DirectoryEntity directoryEntity);

//    根据父目录id获取子目录
    @Select("SELECT * FROM directory WHERE parent_tableId = #{parent_tableId}")
    List<DirectoryEntity> getDirectoryList(@Param("parent_tableId") int parent_tableId);

//    根据用户id获取用户根目录
    @Select("SELECT id FROM directory WHERE userId = #{userId} AND parent_tableId = #{parent_tableId}")
    int getDirectoryIdByUserId(@Param("userId") int userId, @Param("parent_tableId") int parent_tableId);


//    根据目录id获取目录名
    @Select("select * from directory where id = #{id}")
    DirectoryEntity getDirectoryById(@Param("id") int directoryId);

    @Select("select parent_tableId from directory where id = #{id}")
    int getParent_tableIdById(@Param("id") int directoryId);

//    创建目录时生成权限
    @Insert("insert into directory_authority(userId, directoryId, authority)values(#{userId}, #{directoryId}, #{authority})")
    void insertAuthority(@Param("userId") int userId, @Param("directoryId") int directoryId, @Param("authority") int authority);

//    根据目录id获取目录数据
    @Select("select * from directory_data where directoryId = #{directoryId}")
    List<DirectoryDataEntity> getDirectoryDataById(@Param("directoryId") int directoryId);

//    插入文件
    @Insert("insert into directory_data(directoryId,name,url,time)values(#{directoryId}, #{name}, #{url}, #{time})")
    void insert(@Param("directoryId") int directoryId, @Param("name") String name, @Param("url") String url, @Param("time") Date time);

//    查询已拥有目录的userID
    @Select("SELECT DISTINCT userId FROM directory")
    List<DirectoryEntity> selectUserDirectory();

//    更改目录私密性
    @Update("update directory SET openness = #{openness} where id = #{directoryId}")
    void updateOpenness(@Param("directoryId") int directoryId, @Param("openness") int openOrClose);

}
