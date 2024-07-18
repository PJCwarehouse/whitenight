package com.whitenight.blog.entity;

public class PathSegment {
    private String name;
    private int parentId;

    // Constructor, getters, and setters
    public PathSegment(String name, int parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
