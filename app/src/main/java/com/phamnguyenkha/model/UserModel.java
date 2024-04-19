package com.phamnguyenkha.model;

import java.util.Date;

public class UserModel {

    private String name,number,email,address,avatarUrl ;
    public Date dob;
    private int gender;
    public UserModel() {
        // Empty constructor required for Firestore deserialization
    }
    public UserModel(String name, String number, String email, String address, String avatarUrl, Date dob, int gender) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.dob = dob;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
