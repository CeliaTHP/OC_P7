package com.openclassrooms.oc_p7.model;

public class User {

    private String name;
    private String surname;
    private String email;
    private String lunch;
    private String pic;

    public User(String name, String surname, String email, String lunch, String pic) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.lunch = lunch;
        this.pic = pic;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
