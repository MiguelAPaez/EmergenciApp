package com.example.miguelapaez.emergenciapp.Entities;

import java.util.ArrayList;

public class PerfilMedico extends Perfil {
    String rh;
    ArrayList<String> enfermedadesCronicas;
    ArrayList<String> alergiasAmbientales;
    ArrayList<String> alergiasMedicamentos;
    ArrayList<String> medicamentos;

    public PerfilMedico(String email, String rh, ArrayList<String> enfermedadesCronicas, ArrayList<String> alergiasAmbientales, ArrayList<String> alergiasMedicamentos, ArrayList<String> medicamentos) {
        super(email);
        this.rh = rh;
        this.enfermedadesCronicas = enfermedadesCronicas;
        this.alergiasAmbientales = alergiasAmbientales;
        this.alergiasMedicamentos = alergiasMedicamentos;
        this.medicamentos = medicamentos;
    }

    public String getRh() {
        return rh;
    }

    public void setRh(String rh) {
        this.rh = rh;
    }

    public ArrayList<String> getEnfermedadesCronicas() {
        return enfermedadesCronicas;
    }

    public void setEnfermedadesCronicas(ArrayList<String> enfermedadesCronicas) {
        this.enfermedadesCronicas = enfermedadesCronicas;
    }

    public ArrayList<String> getAlergiasAmbientales() {
        return alergiasAmbientales;
    }

    public void setAlergiasAmbientales(ArrayList<String> alergiasAmbientales) {
        this.alergiasAmbientales = alergiasAmbientales;
    }

    public ArrayList<String> getAlergiasMedicamentos() {
        return alergiasMedicamentos;
    }

    public void setAlergiasMedicamentos(ArrayList<String> alergiasMedicamentos) {
        this.alergiasMedicamentos = alergiasMedicamentos;
    }

    public ArrayList<String> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(ArrayList<String> medicamentos) {
        this.medicamentos = medicamentos;
    }
}
