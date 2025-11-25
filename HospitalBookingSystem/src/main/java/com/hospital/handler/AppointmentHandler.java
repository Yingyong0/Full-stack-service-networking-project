package com.hospital.handler;

import com.sun.net.httpserver.HttpExchange;
import com.hospital.model.Appointment;
import com.hospital.util.JsonUtil;
import java.io.IOException;

/**
 * 预约管理处理器
 */
public class AppointmentHandler extends BaseHttpHandler {
    @Override
    protected void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        
        if (parts.length == 4) {
            // GET /api/appointments/{id}
            String appointmentId = parts[3];
            Appointment appointment = dataManager.getAppointment(appointmentId);
            
            if (appointment != null) {
                sendJsonResponse(exchange, 200, appointment);
            } else {
                sendError(exchange, 404, "预约不存在");
            }
        } else {
            // GET /api/appointments
            sendJsonResponse(exchange, 200, dataManager.getAllAppointments());
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange, String path) throws IOException {
        // POST /api/appointments
        String requestBody = readRequestBody(exchange);
        Appointment appointment = JsonUtil.fromJson(requestBody, Appointment.class);
        
        // 验证患者和医生是否存在
        if (dataManager.getPatient(appointment.getPatientId()) == null) {
            sendError(exchange, 400, "患者不存在");
            return;
        }
        
        if (dataManager.getDoctor(appointment.getDoctorId()) == null) {
            sendError(exchange, 400, "医生不存在");
            return;
        }
        
        Appointment createdAppointment = dataManager.createAppointment(appointment);
        sendJsonResponse(exchange, 201, createdAppointment);
    }

    @Override
    protected void handleDelete(HttpExchange exchange, String path) throws IOException {
        // DELETE /api/appointments/{id}
        String[] parts = path.split("/");
        if (parts.length == 4) {
            String appointmentId = parts[3];
            boolean deleted = dataManager.deleteAppointment(appointmentId);
            
            if (deleted) {
                Appointment appointment = dataManager.getAppointment(appointmentId);
                sendJsonResponse(exchange, 200, appointment);
            } else {
                sendError(exchange, 404, "预约不存在");
            }
        } else {
            sendError(exchange, 400, "无效的请求路径");
        }
    }
}

