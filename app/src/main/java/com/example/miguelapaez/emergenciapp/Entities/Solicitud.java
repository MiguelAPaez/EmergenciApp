package com.example.miguelapaez.emergenciapp.Entities;

public class Solicitud {
    String emailSol, emailRem, rolSol, rolRem, status;

    public Solicitud(String emailSol, String emailRem, String rolSol, String rolRem, String status) {
        this.emailSol = emailSol;
        this.emailRem = emailRem;
        this.rolSol = rolSol;
        this.rolRem = rolRem;
        this.status = status;
    }

    public String getEmailSol() {
        return emailSol;
    }

    public void setEmailSol(String emailSol) {
        this.emailSol = emailSol;
    }

    public String getEmailRem() {
        return emailRem;
    }

    public void setEmailRem(String emailRem) {
        this.emailRem = emailRem;
    }

    public String getRolSol() {
        return rolSol;
    }

    public void setRolSol(String rolSol) {
        this.rolSol = rolSol;
    }

    public String getRolRem() {
        return rolRem;
    }

    public void setRolRem(String rolRem) {
        this.rolRem = rolRem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
