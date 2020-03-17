package com.example.miguelapaez.emergenciapp.Entities;

import java.io.Serializable;

public class Perfil implements Serializable {
    String email;
    String password;

    public Perfil(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
