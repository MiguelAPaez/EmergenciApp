package com.example.miguelapaez.emergenciapp.Negocio;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.example.miguelapaez.emergenciapp.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ImplementacionNegocio extends AppCompatActivity implements FacadeNegocio {

    @Override
    public String registrarUsuario(final Perfil user, FirebaseAuth firebaseAuth, final DatabaseReference mDatabase) {
        String email = user.getEmail();
        String password = user.getEmail();
        String response = "Error al crear usuario";
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            crearUsuarioBD(user,mDatabase);

                        } else {
                        }
                    }
                });
        if (firebaseAuth.getCurrentUser()!=null){
            response = "Se ha creado el usuario";
        }
        return response;
    }
    public String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    @Override
    public boolean verificarSeccion() {
        boolean response = false;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            response = true;
        }
        return response;
    }

    @Override
    public void iniciarSeccion(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Log.e("Entro"," En función");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.e("User"," Success");
                        }
                    }
                });
    }

    private void crearUsuarioBD(Perfil user, DatabaseReference mDatabase){
        mDatabase.child("Perfiles").child(user.getId()).setValue(user);
        //Toast.makeText(getApplicationContext(),"Registro Exitoso",Toast.LENGTH_LONG).show();
    }
}
