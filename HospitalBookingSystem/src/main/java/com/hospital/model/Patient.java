package com.hospital.model;

/**
 * Patient Model
 */
public class Patient {
    private String id;
    private String name;
    private String phone;
    private String idCard;
    private String gender;
    private int age;

    public Patient() {
    }

    public Patient(String id, String name, String phone, String idCard, String gender, int age) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.idCard = idCard;
        this.gender = gender;
        this.age = age;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}


