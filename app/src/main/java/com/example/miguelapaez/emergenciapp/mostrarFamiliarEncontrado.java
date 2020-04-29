package com.example.miguelapaez.emergenciapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Entities.Solicitud;
import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.ReferenciaGrupoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.SolicitudPersistence;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class mostrarFamiliarEncontrado extends AppCompatActivity {
    PerfilBasicoPersistence basicProfile;
    TextView nombre, eAdd;
    ImageView profilePhoto;
    String parentesco;
    LinearLayout btnAddGroup;
    Spinner eParentesco;
    FacadeNegocio bussiness = new ImplementacionNegocio();
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mostrar_familiar_encontrado);
        //Recepción de datos Activity Register
        basicProfile = (PerfilBasicoPersistence) getIntent().getSerializableExtra("basicProfile");
        email = getIntent().getStringExtra("email");
        //You Know
        nombre = (TextView) findViewById(R.id.nombreFamiliarEncontrado);
        eAdd = (TextView) findViewById(R.id.textCallFamiliarEncontrado);
        profilePhoto = (ImageView) findViewById(R.id.imagePerfilFamiliarEncontrado);
        eParentesco = (Spinner) findViewById(R.id.parentescoFamiliarEncontrado);
        btnAddGroup = (LinearLayout) findViewById(R.id.buttonAddGroup);
        //Cargar Datos
        String name = basicProfile.getName() + " " + basicProfile.getLastName();
        nombre.setText(name);
        if (basicProfile.getGender().equals("Masculino")) {
            profilePhoto.setImageResource(R.drawable.hombre);
        } else if (basicProfile.getGender().equals("Femenino")) {
            profilePhoto.setImageResource(R.drawable.mujer);
        }
        eParentesco.setVisibility(View.GONE);
        btnAddGroup.setEnabled(false);
        eAdd.setText("Comprobando...");
        checkGroup();
        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnAddGroup.isEnabled()) {

                    parentesco = eParentesco.getSelectedItem().toString();
                    if (!parentesco.equals("Parentesco")) {
                        String rol = bussiness.getRol(parentesco);
                        Solicitud solicitud = new Solicitud(email, basicProfile.getEmail(), rol, parentesco, "Pendiente");
                        bussiness.crearSolicitud(solicitud);
                        Toast.makeText(getApplicationContext(), "Solicitud enviada", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Defina un parentesco", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void checkGroup() {
        DatabaseReference mDatabaseBasic = FirebaseDatabase.getInstance().getReference("Perfiles Basicos");
        mDatabaseBasic.orderByChild("email").equalTo(email);
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilBasicoPersistence user;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(email)) {
                            DatabaseReference mDatabaseGroup = FirebaseDatabase.getInstance().getReference("Perfiles Basicos").child(snapshot.getKey()).child("Grupo");
                            mDatabaseGroup.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Boolean status = true;
                                        ReferenciaGrupoPersistence familiar;
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            familiar = snapshot.getValue(ReferenciaGrupoPersistence.class);
                                            if (!familiar.getEmail().isEmpty() && familiar.getEmail().equals(basicProfile.getEmail())) {
                                                eAdd.setText("Ya es familiar");
                                                btnAddGroup.setEnabled(false);
                                                eParentesco.setVisibility(View.GONE);
                                                status = false;
                                                break;
                                            }
                                        }
                                        if (status) {
                                            checkSol();
                                        }
                                    }
                                    else {
                                        checkSol();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkSol() {
        DatabaseReference mDatabaseSol = FirebaseDatabase.getInstance().getReference("Solicitudes");
        mDatabaseSol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Boolean status = true;
                    SolicitudPersistence req;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        req = snapshot.getValue(SolicitudPersistence.class);
                        if (!req.getEmailRem().isEmpty() && !req.getEmailSol().isEmpty()) {
                            if ((req.getEmailSol().equals(email) && req.getEmailRem().equals(basicProfile.getEmail())) || (req.getEmailSol().equals(basicProfile.getEmail()) && req.getEmailRem().equals(email))) {
                                eAdd.setText("Solicitud en curso");
                                btnAddGroup.setEnabled(false);
                                eParentesco.setVisibility(View.GONE);
                                status = false;
                                break;
                            }
                        }
                    }
                    if (status) {
                        eAdd.setText("Agregar");
                        btnAddGroup.setEnabled(true);
                        eParentesco.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    eAdd.setText("Agregar");
                    btnAddGroup.setEnabled(true);
                    eParentesco.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
