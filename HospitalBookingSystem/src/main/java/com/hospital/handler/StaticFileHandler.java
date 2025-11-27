package com.hospital.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Static File Handler
 */
public class StaticFileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        
        // If path is API path, should not handle (theoretically won't reach here as API routes are registered)
        if (path.startsWith("/api/")) {
            sendError(exchange, 404, "API path not found");
            return;
        }
        
        // Set CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        
        // Default homepage
        if (path.equals("/") || path.equals("/index.html")) {
            path = "/index.html";
        }
        
        // Read resource file
        InputStream resourceStream = getClass().getResourceAsStream("/web" + path);
        
        if (resourceStream == null) {
            sendError(exchange, 404, "File not found: " + path);
            return;
        }
        
        // Set Content-Type based on file extension
        String contentType = getContentType(path);
        exchange.getResponseHeaders().add("Content-Type", contentType);
        
        // Read file content - using Java 9+ compatible method
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

