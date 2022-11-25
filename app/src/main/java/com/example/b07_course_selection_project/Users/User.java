package com.example.b07_course_selection_project.Users;

public abstract class User {
    public String email;
    public String firstname, lastname;
    public boolean admin;
    public User(String firstname, String lastname, String email, boolean admin){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.admin = admin;
    }

}
