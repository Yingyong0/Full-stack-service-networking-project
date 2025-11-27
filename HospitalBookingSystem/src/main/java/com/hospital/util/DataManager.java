package com.hospital.util;

import com.hospital.model.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data Manager - In-memory storage (should use database in production)
 */
public class DataManager {
    private static DataManager instance = new DataManager();
    
    // Data storage
    private Map<String, Patient> patients = new ConcurrentHashMap<>();
    private Map<String, Doctor> doctors = new ConcurrentHashMap<>();
    private Map<String, Appointment> appointments = new ConcurrentHashMap<>();
    private Map<String, Registration> registrations = new ConcurrentHashMap<>();
    
    // ID counters
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
     * Initialize sample data
     */
    private void initSampleData() {
        // Initialize doctor data
        doctors.put("D001", new Doctor("D001", "Dr. Smith", "Internal Medicine", "Chief Physician", "13800138001", "Monday to Friday 9:00-17:00"));
        doctors.put("D002", new Doctor("D002", "Dr. Johnson", "Surgery", "Associate Chief Physician", "13800138002", "Monday to Friday 8:00-16:00"));
        doctors.put("D003", new Doctor("D003", "Dr. Williams", "Pediatrics", "Chief Physician", "13800138003", "Monday to Sunday 9:00-18:00"));
        doctors.put("D004", new Doctor("D004", "Dr. Brown", "Gynecology", "Attending Physician", "13800138004", "Monday to Friday 10:00-18:00"));
    }

    // ============ Patient Management ============
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

    // ============ Doctor Management ============
    public Doctor getDoctor(String id) {
        return doctors.get(id);
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors.values());
    }

    // ============ Appointment Management ============
    public Appointment getAppointment(String id) {
        return appointments.get(id);
    }

    public Appointment createAppointment(Appointment appointment) {
        String id = "A" + String.format("%03d", appointmentIdCounter++);
        appointment.setId(id);
        appointment.setStatus("Pending");
        appointments.put(id, appointment);
        return appointment;
    }

    public boolean deleteAppointment(String id) {
        Appointment appointment = appointments.get(id);
        if (appointment != null) {
            appointment.setStatus("Cancelled");
            return true;
        }
        return false;
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }

    // ============ Registration Management ============
    public Registration getRegistration(String id) {
        return registrations.get(id);
    }

    public Registration createRegistration(Registration registration) {
        String id = "R" + String.format("%03d", registrationIdCounter++);
        registration.setId(id);
        registration.setStatus("Pending");
        registrations.put(id, registration);
        return registration;
    }

    public List<Registration> getAllRegistrations() {
        return new ArrayList<>(registrations.values());
    }
}



