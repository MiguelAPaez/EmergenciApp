package com.example.miguelapaez.emergenciapp.Persistence;

import java.util.List;

public class EspecialidadPersistence {
    private String especialidad;
    private String dias;
    private int hInicio;
    private int hFin;

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public int gethInicio() {
        return hInicio;
    }

    public void sethInicio(int hInicio) {
        this.hInicio = hInicio;
    }

    public int gethFin() {
        return hFin;
    }

    public void sethFin(int hFin) {
        this.hFin = hFin;
    }
}
