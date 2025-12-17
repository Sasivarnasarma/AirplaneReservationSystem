package com.fms.model;

public class Admin extends User {

    public Admin(int userId, String username, String password, String firstName, String lastName, String email) {
        super(userId, username, password, "admin", firstName, lastName, email);
    }

    public Admin(String username, String password, String firstName, String lastName, String email) {
        super(username, password, "admin", firstName, lastName, email);
    }
}
