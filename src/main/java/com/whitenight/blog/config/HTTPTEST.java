package com.whitenight.blog.config;

import java.lang.reflect.Method;

public class HTTPTEST {
    public static void main(String[] args) throws Exception {
        HttpService handler = new HttpService();
        Method[] methods = handler.getClass().getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(HttpRequest.class)) {
                HttpRequest httpRequest = method.getAnnotation(HttpRequest.class);
                String url = httpRequest.url();
                String methodType = httpRequest.method();

                // 根据注解中的 URL 和方法发送 HTTP 请求
                System.out.println("Sending " + methodType + " request to: " + url);

                // 执行方法并获取响应结果
                Object response = method.invoke(handler);
                System.out.println("Response: " + response);
            }
        }
    }
}
