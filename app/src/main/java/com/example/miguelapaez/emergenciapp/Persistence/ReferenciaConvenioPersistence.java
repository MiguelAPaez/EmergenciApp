package com.example.miguelapaez.emergenciapp.Persistence;

public class ReferenciaConvenioPersistence {
    private String idIPS;
    private boolean planC;
    private String regimen;

    public String getIdIPS() {
        return idIPS;
    }

    public void setIdIPS(String idIPS) {
        this.idIPS = idIPS;
    }

    public boolean isPlanC() {
        return planC;
    }

    public void setPlanC(boolean planC) {
        this.planC = planC;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }
}
