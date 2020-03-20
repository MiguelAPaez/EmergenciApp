package com.example.miguelapaez.emergenciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    private TextView name, lastName, idType, id, date, email, phone;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        //TextViews
        name = (TextView) findViewById(R.id.nameProfile);
        lastName = (TextView) findViewById(R.id.lastNameProfile);
        idType = (TextView) findViewById(R.id.idTypeProfile);
        id = (TextView) findViewById(R.id.idProfile);
        date = (TextView) findViewById(R.id.dateProfile);
        email = (TextView) findViewById(R.id.emailProfile);
        phone = (TextView) findViewById(R.id.phoneProfile);
        //Firebase References
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Perfiles basicos");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase.orderByChild("email").equalTo(currentUser.getEmail());
        processDialog = new ProgressDialog(this);
        cargarPerfil();
    }

    private void cargarPerfil() {
        processDialog.setMessage("Cargando perfil");
        processDialog.show();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilBasicoPersistence user = new PerfilBasicoPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                    }
                    llenarDatos(user);
                }
                processDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void llenarDatos(PerfilBasicoPersistence user) {
        name.setText(user.getName());
        lastName.setText(user.getLastName());
        idType.setText(user.getTypeId());
        id.setText(user.getId());
        date.setText(user.getAge());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
    }
}
