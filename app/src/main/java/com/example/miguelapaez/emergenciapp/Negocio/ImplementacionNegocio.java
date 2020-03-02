package com.example.miguelapaez.emergenciapp.Negocio;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                            }
                            else {
                            }
                        }
                    }
                });
        if (firebaseAuth.getCurrentUser()!=null){
            response = "Se ha creado el usuario";
        }
        return response;
    }
    private void crearUsuarioBD(Perfil user, DatabaseReference mDatabase){
        mDatabase.child("Perfiles").child(user.getId()).setValue(user);
        //Toast.makeText(getApplicationContext(),"Registro Exitoso",Toast.LENGTH_LONG).show();
    }
}
