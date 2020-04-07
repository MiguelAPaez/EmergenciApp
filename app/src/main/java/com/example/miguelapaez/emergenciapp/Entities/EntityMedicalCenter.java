package com.example.miguelapaez.emergenciapp.Entities;

public class EntityMedicalCenter {

    private String name;
    private String direction;
    private String latitud;
    private String longitud;

    public EntityMedicalCenter(String name , String direction , String latitud , String longitud) {
        this.name = name;
        this.direction = direction;
        this.latitud = latitud;
        this.longitud = longitud;
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

}
