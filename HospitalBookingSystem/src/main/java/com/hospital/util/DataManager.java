package com.hospital.util;

import com.hospital.model.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据管理器 - 内存存储（实际项目应使用数据库）
 */
public class DataManager {
    private static DataManager instance = new DataManager();
    
    // 数据存储
    private Map<String, Patient> patients = new ConcurrentHashMap<>();
    private Map<String, Doctor> doctors = new ConcurrentHashMap<>();
    private Map<String, Appointment> appointments = new ConcurrentHashMap<>();
    private Map<String, Registration> registrations = new ConcurrentHashMap<>();
    
    // ID计数器
    private int patientIdCounter = 1;
    private int appointmentIdCounter = 1;
    private int registrationIdCounter = 1;

    private DataManager() {
        initSampleData();
    }

    public static DataManager getInstance() {
        return instance;
    }

    /**
     * 初始化示例数据
     */
    private void initSampleData() {
        // 初始化医生数据
        doctors.put("D001", new Doctor("D001", "张医生", "内科", "主任医师", "13800138001", "周一至周五 9:00-17:00"));
        doctors.put("D002", new Doctor("D002", "李医生", "外科", "副主任医师", "13800138002", "周一至周五 8:00-16:00"));
        doctors.put("D003", new Doctor("D003", "王医生", "儿科", "主任医师", "13800138003", "周一至周日 9:00-18:00"));
        doctors.put("D004", new Doctor("D004", "赵医生", "妇科", "主治医师", "13800138004", "周一至周五 10:00-18:00"));
    }

    // ============ 患者管理 ============
    public Patient getPatient(String id) {
        return patients.get(id);
    }

    public Patient createPatient(Patient patient) {
        String id = "P" + String.format("%03d", patientIdCounter++);
        patient.setId(id);
        patients.put(id, patient);
        return patient;
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    // ============ 医生管理 ============
    public Doctor getDoctor(String id) {
        return doctors.get(id);
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors.values());
    }

    // ============ 预约管理 ============
    public Appointment getAppointment(String id) {
        return appointments.get(id);
    }

    public Appointment createAppointment(Appointment appointment) {
        String id = "A" + String.format("%03d", appointmentIdCounter++);
        appointment.setId(id);
        appointment.setStatus("待就诊");
        appointments.put(id, appointment);
        return appointment;
    }

    public boolean deleteAppointment(String id) {
        Appointment appointment = appointments.get(id);
        if (appointment != null) {
            appointment.setStatus("已取消");
            return true;
        }
        return false;
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }

    // ============ 挂号管理 ============
    public Registration getRegistration(String id) {
        return registrations.get(id);
    }

    public Registration createRegistration(Registration registration) {
        String id = "R" + String.format("%03d", registrationIdCounter++);
        registration.setId(id);
        registration.setStatus("待就诊");
        registrations.put(id, registration);
        return registration;
    }

    public List<Registration> getAllRegistrations() {
        return new ArrayList<>(registrations.values());
    }
}



