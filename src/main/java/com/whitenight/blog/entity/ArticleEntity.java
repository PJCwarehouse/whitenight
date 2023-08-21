package com.whitenight.blog.entity;

public class ArticleEntity {
        private int id;
        private String title;
        private String content;
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
}

