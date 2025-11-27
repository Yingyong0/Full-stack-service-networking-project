package com.hospital.model;

/**
 * Doctor Model
 */
public class Doctor {
    private String id;
    private String name;
    private String department;
    private String title;
    private String phone;
    private String schedule; // Work schedule

    public Doctor() {
    }

    public Doctor(String id, String name, String department, String title, String phone, String schedule) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.title = title;
        this.phone = phone;
        this.schedule = schedule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}


