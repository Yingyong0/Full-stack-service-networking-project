package com.hospital.handler;

import com.sun.net.httpserver.HttpExchange;
import com.hospital.model.Registration;
import com.hospital.util.JsonUtil;
import java.io.IOException;

/**
 * Registration Management Handler
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
                sendError(exchange, 404, "Registration record does not exist");
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
        
        // Verify patient and doctor exist
        if (dataManager.getPatient(registration.getPatientId()) == null) {
            sendError(exchange, 400, "Patient does not exist");
            return;
        }
        
        if (dataManager.getDoctor(registration.getDoctorId()) == null) {
            sendError(exchange, 400, "Doctor does not exist");
            return;
        }
        
        Registration createdRegistration = dataManager.createRegistration(registration);
        sendJsonResponse(exchange, 201, createdRegistration);
    }
}

