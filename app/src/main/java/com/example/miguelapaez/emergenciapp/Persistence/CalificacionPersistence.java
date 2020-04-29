package com.example.miguelapaez.emergenciapp.Persistence;

public class CalificacionPersistence {
    String email;
    boolean calificacion;
    String idIPS;

    public String getIdIPS() {
        return idIPS;
    }

    public void setIdIPS(String idIPS) {
        this.idIPS = idIPS;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCalificacion() {
        return calificacion;
    }

    public void setCalificacion(boolean calificacion) {
        this.calificacion = calificacion;
    }
}
