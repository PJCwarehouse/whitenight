package com.whitenight.blog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleEntity {
        private int id;

        private String title;

        private String content;

        private Date time;

        private String author;

        String thumbnail;

        int userId;

        public ArticleEntity() {
        }
        public ArticleEntity(String title, String content) {
            this.title = title;
            this.content = content;
        }

    @Override
    public String toString() {
        return "ArticleEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", author='" + author + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", userId=" + userId +
                '}';
    }
}

