package com.example.miguelapaez.emergenciapp.Negocio;


import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilMedico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilXEPS;
import com.example.miguelapaez.emergenciapp.Entities.PerfilxPrepagada;
import com.example.miguelapaez.emergenciapp.Entities.Solicitud;

public interface FacadeNegocio {
    public String getAge(int year, int month, int day);
    public String getRol(String rol);
    public boolean verificarSesion();
    public boolean cerrarSesion();
    public void guardarPerfilBasico(PerfilBasico user);
    public void guardarPerfilMedico(PerfilMedico user);
    public void guardarPerfilXEPS(PerfilXEPS user);
    public void guardarPerfilXPrepagada(PerfilxPrepagada user);
    public void guardarPerfil(Perfil user);
    public void crearSolicitud(Solicitud solicitud);
}
