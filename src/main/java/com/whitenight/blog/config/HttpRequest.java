package com.whitenight.blog.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HttpRequest {
    String url(); // 请求的URL地址
    String method() default "GET"; // 请求方法，默认为GET
    String contentType() default "application/json"; // 请求的Content-Type，默认为JSON
}
