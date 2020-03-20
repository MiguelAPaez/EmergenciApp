package com.example.miguelapaez.emergenciapp.Persistence;

import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Integrador.IntegradorDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Persistence {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    IntegradorDB integrador = new IntegradorDB();

    //Perfil Básico
    PerfilBasicoPersistence basicProfile = integrador.getPerfilBasico(currentUser.getEmail());

    public PerfilBasicoPersistence getBasicProfile() {
        return basicProfile;
    }

    public void setBasicProfile(PerfilBasicoPersistence basicProfile) {
        this.basicProfile = basicProfile;
    }
}
