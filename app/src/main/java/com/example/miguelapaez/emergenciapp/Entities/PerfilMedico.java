package com.example.miguelapaez.emergenciapp.Entities;

public class PerfilMedico extends Perfil {
    String rh;
    String enfermedadCronica;
    String alergiaAmbiental;
    String alergiaMedicamento;
    String medicamento;

    public PerfilMedico(String email, String rh, String enfermedadCronica, String alergiaAmbiental, String alergiaMedicamento, String medicamento) {
        super(email);
        this.rh = rh;
        this.enfermedadCronica = enfermedadCronica;
        this.alergiaAmbiental = alergiaAmbiental;
        this.alergiaMedicamento = alergiaMedicamento;
        this.medicamento = medicamento;
    }

    public String getRh() {
        return rh;
    }

    public void setRh(String rh) {
        this.rh = rh;
    }

    public String getEnfermedadCronica() {
        return enfermedadCronica;
    }

    public void setEnfermedadCronica(String enfermedadCronica) {
        this.enfermedadCronica = enfermedadCronica;
    }

    public String getAlergiaAmbiental() {
        return alergiaAmbiental;
    }

    public void setAlergiaAmbiental(String alergiaAmbiental) {
        this.alergiaAmbiental = alergiaAmbiental;
    }

    public String getAlergiaMedicamento() {
        return alergiaMedicamento;
    }

    public void setAlergiaMedicamento(String alergiaMedicamento) {
        this.alergiaMedicamento = alergiaMedicamento;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }
}
