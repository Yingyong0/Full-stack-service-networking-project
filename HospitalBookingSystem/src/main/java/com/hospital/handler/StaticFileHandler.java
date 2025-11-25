package com.hospital.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 静态文件处理器
 */
public class StaticFileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        
        // 如果路径是API路径，不应该处理（理论上不会到达这里，因为API路由已注册）
        if (path.startsWith("/api/")) {
            sendError(exchange, 404, "API路径未找到");
            return;
        }
        
        // 设置CORS头
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        
        // 默认首页
        if (path.equals("/") || path.equals("/index.html")) {
            path = "/index.html";
        }
        
        // 读取资源文件
        InputStream resourceStream = getClass().getResourceAsStream("/web" + path);
        
        if (resourceStream == null) {
            sendError(exchange, 404, "文件不存在: " + path);
            return;
        }
        
        // 根据文件扩展名设置Content-Type
        String contentType = getContentType(path);
        exchange.getResponseHeaders().add("Content-Type", contentType);
        
        // 读取文件内容 - 使用兼容Java 9+的方法
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while ((nRead = resourceStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] bytes = buffer.toByteArray();
        
        exchange.sendResponseHeaders(200, bytes.length);
        
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
        resourceStream.close();
    }

    private String getContentType(String path) {
        if (path.endsWith(".html")) {
            return "text/html; charset=UTF-8";
        } else if (path.endsWith(".css")) {
            return "text/css";
        } else if (path.endsWith(".js")) {
            return "application/javascript";
        } else if (path.endsWith(".json")) {
            return "application/json";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        } else {
            return "text/plain";
        }
    }

    private void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        String error = "<html><body><h1>" + statusCode + " " + message + "</h1></body></html>";
        byte[] bytes = error.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}

