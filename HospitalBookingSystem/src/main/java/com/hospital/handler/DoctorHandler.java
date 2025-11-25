package com.hospital.handler;

import com.sun.net.httpserver.HttpExchange;
import com.hospital.model.Doctor;
import java.io.IOException;

/**
 * 医生管理处理器
 */
public class DoctorHandler extends BaseHttpHandler {
    @Override
    protected void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        
        if (parts.length == 4) {
            // GET /api/doctors/{id}
            String doctorId = parts[3];
            Doctor doctor = dataManager.getDoctor(doctorId);
            
            if (doctor != null) {
                sendJsonResponse(exchange, 200, doctor);
            } else {
                sendError(exchange, 404, "医生不存在");
            }
        } else {
            // GET /api/doctors
            sendJsonResponse(exchange, 200, dataManager.getAllDoctors());
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange, String path) throws IOException {
        sendError(exchange, 405, "方法不允许");
    }
}

