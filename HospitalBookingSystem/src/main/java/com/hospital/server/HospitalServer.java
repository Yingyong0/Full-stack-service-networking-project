package com.hospital.server;

import com.hospital.handler.*;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 医院预约和挂号系统HTTP服务器
 * 基于Java原生HTTP/1.1实现
 */
public class HospitalServer {
    private static final int PORT = 8080;
    private HttpServer server;

    public HospitalServer() throws IOException {
        // 创建HTTP服务器，监听指定端口
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // 注册API路由
        registerRoutes();
        
        // 设置线程池（使用默认线程池）
        server.setExecutor(null);
    }

    private void registerRoutes() {
        // 先注册API路由（更具体的路径）
        server.createContext("/api/patients", new PatientHandler());
        server.createContext("/api/doctors", new DoctorHandler());
        server.createContext("/api/appointments", new AppointmentHandler());
        server.createContext("/api/registrations", new RegistrationHandler());
        
        // 最后注册静态文件服务（作为fallback）
        server.createContext("/", new StaticFileHandler());
    }

    public void start() {
        server.start();
        System.out.println("========================================");
        System.out.println("医院预约和挂号系统服务器已启动");
        System.out.println("服务器地址: http://localhost:" + PORT);
        System.out.println("前端页面: http://localhost:" + PORT + "/index.html");
        System.out.println("API文档:");
        System.out.println("  GET    /api/patients - 获取所有患者");
        System.out.println("  GET    /api/patients/{id} - 获取患者信息");
        System.out.println("  POST   /api/patients - 注册新患者");
        System.out.println("  GET    /api/doctors - 获取所有医生");
        System.out.println("  GET    /api/doctors/{id} - 获取医生信息");
        System.out.println("  GET    /api/appointments - 获取所有预约");
        System.out.println("  GET    /api/appointments/{id} - 获取预约信息");
        System.out.println("  POST   /api/appointments - 创建预约");
        System.out.println("  DELETE /api/appointments/{id} - 取消预约");
        System.out.println("  GET    /api/registrations - 获取所有挂号记录");
        System.out.println("  GET    /api/registrations/{id} - 获取挂号信息");
        System.out.println("  POST   /api/registrations - 创建挂号");
        System.out.println("========================================");
    }

    public void stop(int delay) {
        server.stop(delay);
        System.out.println("服务器已关闭");
    }

    public static void main(String[] args) {
        try {
            HospitalServer server = new HospitalServer();
            server.start();
            
            // 添加关闭钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n正在关闭服务器...");
                server.stop(0);
            }));
            
            // 保持服务器运行
            System.out.println("\n按 Ctrl+C 停止服务器");
            
        } catch (IOException e) {
            System.err.println("服务器启动失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

