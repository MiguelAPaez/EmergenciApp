package com.example.miguelapaez.emergenciapp.Entities;

public class PerfilxPrepagada {
    String emailPerfil;
    String nombrePrepada;

    public PerfilxPrepagada(String emailPerfil, String nombrePrepada) {
        this.emailPerfil = emailPerfil;
        this.nombrePrepada = nombrePrepada;
    }

    public String getEmailPerfil() {
        return emailPerfil;
    }

    public void setEmailPerfil(String emailPerfil) {
        this.emailPerfil = emailPerfil;
    }

    public String getNombrePrepada() {
        return nombrePrepada;
    }

    public void setNombrePrepada(String nombrePrepada) {
        this.nombrePrepada = nombrePrepada;
    }
}
