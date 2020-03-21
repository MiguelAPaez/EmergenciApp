package com.example.miguelapaez.emergenciapp.Persistence;

public class PerfilXEPSPersistence {
    String emailPerfil;
    String nombreEPS;
    String regimen;
    boolean planComplementario;

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
