package com.example.miguelapaez.emergenciapp.Integrador;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IntegradorDB {
    private ArrayList<PerfilBasicoPersistence> PerfilesBasicos = new ArrayList<PerfilBasicoPersistence>();
    PerfilBasicoPersistence user = new PerfilBasicoPersistence();

    public PerfilBasicoPersistence getPerfilBasico(String correo){
        readData(new FirebaseCallback() {
            @Override
            public void onCallBack(ArrayList<PerfilBasicoPersistence> List) {
            }
        });
        return user;
    }


    private void readData (final FirebaseCallback firebaseCallback){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Perfiles basicos");
        mDatabase.orderByChild("email").equalTo(currentUser.getEmail());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //PerfilesBasicos.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PerfilBasicoPersistence user = snapshot.getValue(PerfilBasicoPersistence.class);
                        PerfilesBasicos.add(user);
                    }
                    firebaseCallback.onCallBack(PerfilesBasicos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private interface FirebaseCallback{
        void onCallBack(ArrayList<PerfilBasicoPersistence> List);
    }

}
