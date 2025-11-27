package com.hospital.server;

import com.hospital.handler.*;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Hospital Appointment and Registration System HTTP Server
 * Implemented using Java native HTTP/1.1
 */
public class HospitalServer {
    private static final int PORT = 8080;
    private HttpServer server;

    public HospitalServer() throws IOException {
        // Create HTTP server, listen on specified port
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Register API routes
        registerRoutes();
        
        // Set thread pool (use default thread pool)
        server.setExecutor(null);
    }

    private void registerRoutes() {
        // Register API routes first (more specific paths)
        server.createContext("/api/patients", new PatientHandler());
        server.createContext("/api/doctors", new DoctorHandler());
        server.createContext("/api/appointments", new AppointmentHandler());
        server.createContext("/api/registrations", new RegistrationHandler());
        
        // Register static file service last (as fallback)
        server.createContext("/", new StaticFileHandler());
    }

    public void start() {
        server.start();
        System.out.println("========================================");
        System.out.println("Hospital Appointment and Registration System Server Started");
        System.out.println("Server Address: http://localhost:" + PORT);
        System.out.println("Frontend Page: http://localhost:" + PORT + "/index.html");
        System.out.println("API Documentation:");
        System.out.println("  GET    /api/patients - Get all patients");
        System.out.println("  GET    /api/patients/{id} - Get patient information");
        System.out.println("  POST   /api/patients - Register new patient");
        System.out.println("  GET    /api/doctors - Get all doctors");
        System.out.println("  GET    /api/doctors/{id} - Get doctor information");
        System.out.println("  GET    /api/appointments - Get all appointments");
        System.out.println("  GET    /api/appointments/{id} - Get appointment information");
        System.out.println("  POST   /api/appointments - Create appointment");
        System.out.println("  DELETE /api/appointments/{id} - Cancel appointment");
        System.out.println("  GET    /api/registrations - Get all registration records");
        System.out.println("  GET    /api/registrations/{id} - Get registration information");
        System.out.println("  POST   /api/registrations - Create registration");
        System.out.println("========================================");
    }

    public void stop(int delay) {
        server.stop(delay);
        System.out.println("Server stopped");
    }

    public static void main(String[] args) {
        try {
            HospitalServer server = new HospitalServer();
            server.start();
            
            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down server...");
                server.stop(0);
            }));
            
            // Keep server running
            System.out.println("\nPress Ctrl+C to stop the server");
            
        } catch (IOException e) {
            System.err.println("Server startup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

