package com.whitenight.blog.breakpointTransfer;

import org.springframework.web.multipart.MultipartFile;

public class FileChunk {
    private String filename;
    private long offset;
    private long total;
    private MultipartFile file;


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileChunk{" +
                "filename='" + filename + '\'' +
                ", offset=" + offset +
                ", total=" + total +
                ", file=" + file +
                '}';
    }
// 构造函数、getter和setter省略
}