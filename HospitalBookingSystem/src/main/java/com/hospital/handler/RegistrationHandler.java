package com.hospital.handler;

import com.sun.net.httpserver.HttpExchange;
import com.hospital.model.Registration;
import com.hospital.util.JsonUtil;
import java.io.IOException;

/**
 * 挂号管理处理器
 */
public class RegistrationHandler extends BaseHttpHandler {
    @Override
    protected void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        
        if (parts.length == 4) {
            // GET /api/registrations/{id}
            String registrationId = parts[3];
            Registration registration = dataManager.getRegistration(registrationId);
            
            if (registration != null) {
                sendJsonResponse(exchange, 200, registration);
            } else {
                sendError(exchange, 404, "挂号记录不存在");
            }
        } else {
            // GET /api/registrations
            sendJsonResponse(exchange, 200, dataManager.getAllRegistrations());
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange, String path) throws IOException {
        // POST /api/registrations
        String requestBody = readRequestBody(exchange);
        Registration registration = JsonUtil.fromJson(requestBody, Registration.class);
        
        // 验证患者和医生是否存在
        if (dataManager.getPatient(registration.getPatientId()) == null) {
            sendError(exchange, 400, "患者不存在");
            return;
        }
        
        if (dataManager.getDoctor(registration.getDoctorId()) == null) {
            sendError(exchange, 400, "医生不存在");
            return;
        }
        
        Registration createdRegistration = dataManager.createRegistration(registration);
        sendJsonResponse(exchange, 201, createdRegistration);
    }
}

