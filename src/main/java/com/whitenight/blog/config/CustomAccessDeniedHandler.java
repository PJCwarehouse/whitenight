package com.whitenight.blog.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 设置响应状态码为403 Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 设置提示信息
        String message = "您没有访问此页面的权限，请联系管理员。";
        request.setAttribute("accessDeniedMessage", message);

        // 重定向到错误页面或其他页面
        response.sendRedirect("page/1?NoPermission=权限不足！"); // 根据您的实际需求设置重定向的页面
    }
}

