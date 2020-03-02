package com.example.miguelapaez.emergenciapp.Negocio;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public interface FacadeNegocio {
    public String registrarUsuario(Perfil user, FirebaseAuth firebaseAuth, DatabaseReference mDatabase);
}
