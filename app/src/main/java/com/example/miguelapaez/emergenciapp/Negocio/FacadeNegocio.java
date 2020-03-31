package com.example.miguelapaez.emergenciapp.Negocio;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilMedico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilXEPS;
import com.example.miguelapaez.emergenciapp.Entities.PerfilxPrepagada;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public interface FacadeNegocio {
    public String getAge(int year, int month, int day);
    public boolean verificarSesion();
    public boolean cerrarSesion();
    public void guardarPerfilBasico(PerfilBasico user);
    public void guardarPerfilMedico(PerfilMedico user);
    public void guardarPerfilXEPS(PerfilXEPS user);
    public void guardarPerfilXPrepagada(PerfilxPrepagada user);
    public void guardarPerfil(Perfil user);
}
