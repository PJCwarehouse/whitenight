package com.whitenight.blog.entity;

import lombok.Data;

import java.util.List;

/**
 * 树形目录节点结构
 */
@Data
public class DirectoryTreeNode {

    private int id;

    private String name;

    private String type; // "directory" or "file"

    private List<DirectoryTreeNode> children;

    public DirectoryTreeNode() {}

    public DirectoryTreeNode(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

}
