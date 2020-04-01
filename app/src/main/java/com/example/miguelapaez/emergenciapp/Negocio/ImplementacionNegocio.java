package com.example.miguelapaez.emergenciapp.Negocio;


import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilMedico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilXEPS;
import com.example.miguelapaez.emergenciapp.Entities.PerfilxPrepagada;
import com.example.miguelapaez.emergenciapp.Entities.Solicitud;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ImplementacionNegocio extends AppCompatActivity implements FacadeNegocio {
    @Override
    public String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    @Override
    public String getRol(String rol) {
        String parent = "Padre/Madre";
        String son = "Hijo(a)";
        String other = "Familiar";
        String rolResult;
        if (rol.equals(parent)) {
            rolResult = son;
        } else if (rol.equals(son)) {
            rolResult = parent;
        } else {
            rolResult = other;
        }
        return rolResult;
    }

    @Override
    public boolean verificarSesion() {

        boolean response = false;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            response = true;
        }
        return response;
    }


    @Override
    public boolean cerrarSesion() {
        boolean response = false;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        if (!verificarSesion()) {
            response = true;
        }
        return response;
    }

    @Override
    public void guardarPerfilBasico(PerfilBasico user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String id = mDatabase.push().getKey();
        mDatabase.child("Perfiles Basicos").child(id).setValue(user);
    }

    @Override
    public void guardarPerfilMedico(PerfilMedico user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String id = mDatabase.push().getKey();
        mDatabase.child("Perfiles Medicos").child(id).setValue(user);
    }

    @Override
    public void guardarPerfilXEPS(PerfilXEPS user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String id = mDatabase.push().getKey();
        mDatabase.child("PerfilesXEPS").child(id).setValue(user);
    }

    @Override
    public void guardarPerfilXPrepagada(PerfilxPrepagada user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String id = mDatabase.push().getKey();
        mDatabase.child("PerfilesXPrepagada").child(id).setValue(user);
    }

    public void guardarPerfil(Perfil user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String id = mDatabase.push().getKey();
        mDatabase.child("Perfiles").child(id).setValue(user);
    }

    @Override
    public void crearSolicitud(Solicitud solicitud) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String id = mDatabase.push().getKey();
        mDatabase.child("Solicitudes").child(id).setValue(solicitud);
    }
}
