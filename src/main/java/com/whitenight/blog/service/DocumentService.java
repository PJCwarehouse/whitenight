package com.whitenight.blog.service;

import com.whitenight.blog.entity.DirectoryTreeNode;
import com.whitenight.blog.entity.DocumentEntity;
import com.whitenight.blog.entity.PathSegment;
import com.whitenight.blog.mapper.DocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DocumentService {
    @Autowired
    DocumentMapper documentMapper;

    @Autowired
    UserService userService;

    public void insert(DocumentEntity documentEntity) {
        documentMapper.insert(documentEntity);
    }

    public void deleteFile(int fileId) {
        documentMapper.deleteFile(fileId);
    }

    public DocumentEntity select(int id) {
        return documentMapper.select(id);
    }

    public List<DocumentEntity> getFileList(int parentId) {
        return documentMapper.getList(parentId, "file");
    }

    public List<DocumentEntity> getDirectoryList(int parentId) {
        return documentMapper.getList(parentId, "directory");
    }

    public void updateUrl(int id, String fileDownloadUrl) {
        documentMapper.updateUrl(id, fileDownloadUrl);
    }

    //    获取路径
    public List<PathSegment> getPathSegment(int parentId){
        String path = "";
        List<PathSegment> pathSegments = new ArrayList<>();//创建一个类包含目录名和id
        while (parentId != 0) {
            //  这里的一对参数是当前目录的id与目录名
            DocumentEntity parentEntity = select(parentId);
            String parent_tableName = parentEntity.getName();
            pathSegments.add(new PathSegment(parent_tableName, parentId));
            parentId = parentEntity.getParentId();
        }
        pathSegments.add(new PathSegment("root",0));
        Collections.reverse(pathSegments);//反转pathSegments以保持正确的顺序
        return pathSegments;
    }

    public double[] fileSize(long fileSize) {
        double size = (double) fileSize;
        long unitIndex = 0;

        while (size >= 1024 && unitIndex < 4) {
            size /= 1024;
            unitIndex++;
        }
        return new double[]{Math.round(size * 100.0) / 100.0, unitIndex}; // 保留两位小数
    }

    public void deleteUsersDocuments(int userId) {
        documentMapper.deleteByUserId(userId);
    }

    public void getChildrenTrees(DirectoryTreeNode root) {
        List<DocumentEntity> directoryEntities = documentMapper.getList(root.getId(), "directory");
        List<DirectoryTreeNode> directoryTreeNodes = new ArrayList<>();
        for (DocumentEntity documentEntity : directoryEntities) {
            if(documentEntity.getVisibleType() != 0 || documentEntity.getUserId() == userService.getId()){
                DirectoryTreeNode directoryTreeNode = new DirectoryTreeNode(documentEntity.getId(), documentEntity.getName(), documentEntity.getType());
                getChildrenTrees(directoryTreeNode);
                directoryTreeNodes.add(directoryTreeNode);
            }
        }
//        目前不需要在树形目录展示文件
//        List<DocumentEntity> fileEntities = documentMapper.getList(root.getId(), "file");
//        List<DirectoryTreeNode> fileTreeNodes = new ArrayList<>();
//        for(DocumentEntity documentEntity : fileEntities){
//            DirectoryTreeNode directoryTreeNode = new DirectoryTreeNode(documentEntity.getId(), documentEntity.getName(), documentEntity.getType());
//            fileTreeNodes.add(directoryTreeNode);
//        }
//        directoryTreeNodes.addAll(fileTreeNodes);
        root.setChildren(directoryTreeNodes);
    }
    public void updateOpenness(int directoryId, int visibleType){
        documentMapper.updateOpenness(directoryId, visibleType);
    }

    public List<DocumentEntity> searchResult(String searchString){
        return documentMapper.searchResult(searchString);
    }
}