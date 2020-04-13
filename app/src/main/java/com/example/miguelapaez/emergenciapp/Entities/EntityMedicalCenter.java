package com.example.miguelapaez.emergenciapp.Entities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class EntityMedicalCenter {

    private String name;
    private String direction;
    private String latitud;
    private String longitud;
    private ArrayList<EPS> Convenios;
    private ArrayList<Especialidad> especialidades;
    private int edadMinimaDeAtencion;
    private int edadMaximaDeAtencion;

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

    public boolean TieneConvenio(String nombreEPS) {

        for (EPS c : Convenios ) {
            if(c.getNombre().equals(nombreEPS))
                return true;
        }
        return false;
    }

    public boolean AtiendeSegunLaEdad(int age) {

        return (edadMinimaDeAtencion >= age && age <=edadMinimaDeAtencion);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int especialidadesDisponibles(ArrayList<Especialidad> especialistasNecesarios) {

        int n = 0;

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        String hora = String.valueOf(hour) + String.valueOf(minute);

        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        for(Especialidad e :especialistasNecesarios){
            for (Especialidad e2 : especialidades){
                if(e.getNombre().equals(e2.getNombre()) && e.atiende(dayOfWeek, hora)){
                    n++;
                }
            }
        }
        return n;
    }
}
