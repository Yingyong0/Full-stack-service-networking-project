package com.hospital.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.hospital.util.JsonUtil;
import com.hospital.util.DataManager;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Base HTTP Request Handler
 */
public abstract class BaseHttpHandler implements HttpHandler {
    protected DataManager dataManager = DataManager.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        // Set CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");

        // Handle OPTIONS request (CORS preflight)
        if ("OPTIONS".equals(method)) {
            exchange.sendResponseHeaders(200, -1);
            exchange.close();
            return;
        }

        try {
            switch (method) {
                case "GET":
                    handleGet(exchange, path);
                    break;
                case "POST":
                    handlePost(exchange, path);
                    break;
                case "PUT":
                    handlePut(exchange, path);
                    break;
                case "DELETE":
                    handleDelete(exchange, path);
                    break;
                default:
                    sendError(exchange, 405, "Method not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    protected abstract void handleGet(HttpExchange exchange, String path) throws IOException;

    protected abstract void handlePost(HttpExchange exchange, String path) throws IOException;

    protected void handlePut(HttpExchange exchange, String path) throws IOException {
        sendError(exchange, 405, "Method not allowed");
    }

    protected void handleDelete(HttpExchange exchange, String path) throws IOException {
        sendError(exchange, 405, "Method not allowed");
    }

    protected void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    protected void sendJsonResponse(HttpExchange exchange, int statusCode, Object obj) throws IOException {
        String json = JsonUtil.toJson(obj);
        sendResponse(exchange, statusCode, json);
    }

    protected void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        String error = "{\"error\": \"" + message + "\"}";
        sendResponse(exchange, statusCode, error);
    }

    protected String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        return JsonUtil.readRequestBody(reader);
    }
}

