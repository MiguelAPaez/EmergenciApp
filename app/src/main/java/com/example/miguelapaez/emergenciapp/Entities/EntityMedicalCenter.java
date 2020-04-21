package com.example.miguelapaez.emergenciapp.Entities;

import com.example.miguelapaez.emergenciapp.Persistence.EspecialidadPersistence;

import java.util.List;

public class EntityMedicalCenter {

    private String name;
    private String direction;
    private String latitud;
    private String longitud;
    private int edadMin;
    private int edadMax;
    private double calificacion;
    private List<EspecialidadPersistence> especialidades;
    private int duration;
    private String id;
    private boolean qualificated = false;

    public EntityMedicalCenter(String name, String direction, String latitud, String longitud, int edadMin, int edadMax, double calificacion) {
        this.name = name;
        this.direction = direction;
        this.latitud = latitud;
        this.longitud = longitud;
        this.edadMin = edadMin;
        this.edadMax = edadMax;
        this.calificacion = calificacion;
    }

    public String getName() {
        return name;
    }

    public String getDirection() {
        return direction;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public int getEdadMin() {
        return edadMin;
    }

    public int getEdadMax() {
        return edadMax;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public List<EspecialidadPersistence> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<EspecialidadPersistence> especialidades) {
        this.especialidades = especialidades;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isQualificated() {
        return qualificated;
    }

    public void setQualificated(boolean qualificated) {
        this.qualificated = qualificated;
    }
}
