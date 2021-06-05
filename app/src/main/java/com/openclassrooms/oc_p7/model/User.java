package com.openclassrooms.oc_p7.model;

import java.util.List;

public class User {

    private String name;
    private String surname;
    private String email;
    private String lunch;
    private String picUrl;
    private List<Workmate> workmates;

    public User(String name, String email, String lunch, String picUrl, List<Workmate> workmates) {
        this.name = name;
        this.email = email;
        this.lunch = lunch;
        this.picUrl = picUrl;
        this.workmates = workmates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public List<Workmate> getWorkmates() {
        return workmates;
    }

    public void setWorkmates(List<Workmate> workmates) {
        this.workmates = workmates;
    }
}
