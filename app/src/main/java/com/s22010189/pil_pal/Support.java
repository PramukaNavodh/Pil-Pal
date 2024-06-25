package com.s22010189.pil_pal;
//helper class for UserSignUp with firebase
public class Support {
    String firstname, lastname, email, mobilenumber,password;

    public Support(String firstname, String lastname, String email, String mobilenumber, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.mobilenumber = mobilenumber;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Support() {
    }
}
