package com.example.miguelapaez.emergenciapp.Negocio;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public interface FacadeNegocio {
    public String registrarUsuario(Perfil user, FirebaseAuth firebaseAuth, DatabaseReference mDatabase);
    public String getAge(int year, int month, int day);
    public boolean verificarSesion();
    public void iniciarSesion(String email, String password);
    public boolean cerrarSesion();
}
