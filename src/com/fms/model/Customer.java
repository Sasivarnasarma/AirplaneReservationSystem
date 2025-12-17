package com.fms.model;

public class Customer extends User {
    private String nic;
    private String passportNumber;

    public Customer(int userId, String username, String password, String firstName, String lastName, String email,
            String nic, String passportNumber) {
        super(userId, username, password, "customer", firstName, lastName, email);
        this.nic = nic;
        this.passportNumber = passportNumber;
    }

    public Customer(String username, String password, String firstName, String lastName, String email, String nic,
            String passportNumber) {
        super(username, password, "customer", firstName, lastName, email);
        this.nic = nic;
        this.passportNumber = passportNumber;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }
}
