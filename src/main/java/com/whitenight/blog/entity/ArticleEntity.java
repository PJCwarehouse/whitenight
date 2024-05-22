package com.whitenight.blog.entity;

import java.util.Date;

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
        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Date getTime() {
        return time;
        }

        public void setTime(Date time) {
        this.time = time;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
}

