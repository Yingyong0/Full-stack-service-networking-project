package com.hospital.handler;

import com.sun.net.httpserver.HttpExchange;
import com.hospital.model.Patient;
import com.hospital.util.JsonUtil;
import java.io.IOException;

/**
 * Patient Management Handler
 */
public class PatientHandler extends BaseHttpHandler {
    @Override
    protected void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        
        if (parts.length == 4) {
            // GET /api/patients/{id}
            String patientId = parts[3];
            Patient patient = dataManager.getPatient(patientId);
            
            if (patient != null) {
                sendJsonResponse(exchange, 200, patient);
            } else {
                sendError(exchange, 404, "Patient does not exist");
            }
        } else {
            // GET /api/patients
            sendJsonResponse(exchange, 200, dataManager.getAllPatients());
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange, String path) throws IOException {
        // POST /api/patients
        String requestBody = readRequestBody(exchange);
        Patient patient = JsonUtil.fromJson(requestBody, Patient.class);
        
        Patient createdPatient = dataManager.createPatient(patient);
        sendJsonResponse(exchange, 201, createdPatient);
    }
}

