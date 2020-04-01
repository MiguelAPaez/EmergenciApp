package com.example.miguelapaez.emergenciapp.Entities;

public class ReferenciaGrupo {
    String email;
    String rol;

    public ReferenciaGrupo(String email, String rol) {
        this.email = email;
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
