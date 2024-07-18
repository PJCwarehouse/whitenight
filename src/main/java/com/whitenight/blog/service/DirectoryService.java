package com.whitenight.blog.service;

import com.whitenight.blog.entity.DirectoryDataEntity;
import com.whitenight.blog.entity.DirectoryEntity;
import com.whitenight.blog.mapper.DirectoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DirectoryService {
    @Autowired
    DirectoryMapper directoryMapper;

    @Autowired
    UserService userService;

    public void createDirectory(int userId, int parent_tableId, String directoryName) {
        DirectoryEntity directoryEntity = new DirectoryEntity();
        directoryEntity.setUserId(userId);
        directoryEntity.setParent_tableId(parent_tableId);
        directoryEntity.setDirectoryName(directoryName);
        directoryEntity.setOpenness(1);

        directoryMapper.createDirectory(directoryEntity);
        directoryMapper.insertAuthority(userId, directoryEntity.getId(), 0);
        System.out.println("创建目录：" + directoryName);
    }

    public List<DirectoryEntity> getDirectoryList(int parent_tableId) {
        return directoryMapper.getDirectoryList(parent_tableId);
    }

    public int getDirectoryIdByUserId(int userId) {
        return directoryMapper.getDirectoryIdByUserId(userId, 0);
    }

    public List<DirectoryDataEntity> getDirectoryDataList(int parent_tableId){
        return directoryMapper.getDirectoryDataById(parent_tableId);
    }

    public DirectoryEntity getDirectoryById(int directoryId) {
        return directoryMapper.getDirectoryById(directoryId);
    }

    public int getParent_tableIdById(int id){
        return directoryMapper.getParent_tableIdById(id);
    }
    public void insert(int directoryId, String name, String url){
        Date time = new Date();
        directoryMapper.insert(directoryId, name, url, time);
    }

    public void updateOpenness(int directoryId, int openOrClose){
        directoryMapper.updateOpenness(directoryId, openOrClose);
    }

}
