package com.example.miguelapaez.emergenciapp.Entities;


import java.util.ArrayList;

public class Especialidad {

    private String nombre;
    private ArrayList<Dia> diasAtencion;
    private String horarioDisponibilidadInicio;
    private String horarioDisponibilidadFin;

    public Especialidad(String nombre, ArrayList<Dia> diasAtencion, String horarioDisponibilidadInicio, String horarioDisponibilidadFin) {
        this.nombre = nombre;
        this.diasAtencion = diasAtencion;
        this.horarioDisponibilidadInicio = horarioDisponibilidadInicio;
        this.horarioDisponibilidadFin = horarioDisponibilidadFin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Dia> getDiasAtencion() {
        return diasAtencion;
    }

    public void setDiasAtencion(ArrayList<Dia> diasAtencion) {
        this.diasAtencion = diasAtencion;
    }

    public String getHorarioDisponibilidadInicio() {
        return horarioDisponibilidadInicio;
    }

    public void setHorarioDisponibilidadInicio(String horarioDisponibilidadInicio) {
        this.horarioDisponibilidadInicio = horarioDisponibilidadInicio;
    }

    public String getHorarioDisponibilidadFin() {
        return horarioDisponibilidadFin;
    }

    public void setHorarioDisponibilidadFin(String horarioDisponibilidadFin) {
        this.horarioDisponibilidadFin = horarioDisponibilidadFin;
    }

    public boolean atiende(int dia, String horaActual){

        for(Dia dias : diasAtencion){

            if(dias.getDia() == dia && dias.getHoraInicio() =< horaActual && horaActual <= dias.getHoraFin()){
                return true;
            }
         }
        return false;

    }
}
