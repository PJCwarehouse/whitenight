package com.whitenight.blog.config;

public class HttpService {
    @HttpRequest(url = "https://api.example.com/data", method = "POST", contentType = "application/xml")
    public String sendData() {
        // 发送 HTTP 请求的逻辑
        // 返回响应结果
        return "Response data";
    }
}
