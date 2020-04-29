package com.example.miguelapaez.emergenciapp.Entities;

public class PerfilXEPS {
    String emailPerfil;
    String nombreEPS;
    String regimen;
    boolean planComplementario;

    public PerfilXEPS(String emailPerfil, String nombreEPS, String regimen, boolean planComplementario) {
        this.emailPerfil = emailPerfil;
        this.nombreEPS = nombreEPS;
        this.regimen = regimen;
        this.planComplementario = planComplementario;
    }

    public String getEmailPerfil() {
        return emailPerfil;
    }

    public void setEmailPerfil(String emailPerfil) {
        this.emailPerfil = emailPerfil;
    }

    public String getNombreEPS() {
        return nombreEPS;
    }

    public void setNombreEPS(String nombreEPS) {
        this.nombreEPS = nombreEPS;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public boolean isPlanComplementario() {
        return planComplementario;
    }

    public void setPlanComplementario(boolean planComplementario) {
        this.planComplementario = planComplementario;
    }
}
