package com.example.miguelapaez.emergenciapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Adapters.AdapterFamilyGroup;
import com.example.miguelapaez.emergenciapp.Entities.EntityFamilyGroup;
import com.example.miguelapaez.emergenciapp.Entities.ReferenciaGrupo;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.SolicitudPersistence;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notificaciones extends AppCompatActivity {
    String emailActual;
    DatabaseReference mDatabaseBasic;
    ListView listItemsNotificaciones;
    private AdapterFamilyGroup adaptador;
    ArrayList<EntityFamilyGroup> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        listItemsNotificaciones = findViewById(R.id.listViewNotificaciones);
        adaptador = new AdapterFamilyGroup(this, listItems);
        listItemsNotificaciones.setAdapter(adaptador);
        emailActual = getIntent().getStringExtra("emailActual");
        mDatabaseBasic = FirebaseDatabase.getInstance().getReference().child("Perfiles Basicos");
        cargarSolicitudes();

        listItemsNotificaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder message = new AlertDialog.Builder(Notificaciones.this);
                message.setTitle(listItems.get(i).getName());
                message.setMessage("Desea agregarte a su Grupo Familiar");
                final EntityFamilyGroup buzz = listItems.get(i);
                message.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String idP = mDatabaseBasic.push().getKey();
                        mDatabaseBasic.orderByChild("email").equalTo(emailActual);
                        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    PerfilBasicoPersistence user;
                                    ReferenciaGrupo familiar = new ReferenciaGrupo(buzz.getEmail(), buzz.getParent());
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                                        if (!user.getEmail().isEmpty() && user.getEmail().equals(emailActual)) {
                                            String idChild = snapshot.getKey();
                                            mDatabaseBasic.child(idChild).child("Grupo").child(idP).setValue(familiar);
                                            cargarSolicitud(emailActual, buzz.getEmail(),idP);
                                            break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                message.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Notificaciones.this, "Solicitud eliminada", Toast.LENGTH_SHORT).show();
                        eliminarSolicitudNeg(emailActual, buzz.getEmail());
                    }
                });

                AlertDialog dialog = message.create();
                dialog.show();
            }
        });
    }

    private void cargarSolicitudes() {
        DatabaseReference mDatabaseSolicitud = FirebaseDatabase.getInstance().getReference().child("Solicitudes");
        mDatabaseSolicitud.orderByChild("emailRem").equalTo(emailActual);
        mDatabaseSolicitud.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SolicitudPersistence solPer;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        solPer = snapshot.getValue(SolicitudPersistence.class);
                        if (!solPer.getEmailRem().isEmpty() && solPer.getEmailRem().equals(emailActual)) {
                            String id = snapshot.getKey();
                            cargarSolicitante(solPer);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cargarSolicitante(final SolicitudPersistence solicitud) {
        mDatabaseBasic.orderByChild("email").equalTo(solicitud.getEmailSol());
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilBasicoPersistence user = new PerfilBasicoPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(solicitud.getEmailSol())) {
                            llenarSolicitante(user, solicitud.getRolSol());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void llenarSolicitante(PerfilBasicoPersistence user, String rol) {
        String name = user.getName() + " " + user.getLastName();
        int img = R.drawable.incognito;
        if (user.getGender().equals("Masculino")) {
            img = R.drawable.hombre;
        } else if (user.getGender().equals("Femenino")) {
            img = R.drawable.mujer;
        }
        EntityFamilyGroup fam = new EntityFamilyGroup(img, name, rol, user.getEmail());
        listItems.add(fam);
        adaptador.notifyDataSetChanged();
    }

    private void cargarSolicitud(final String emailActual, final String emailSol, final String idP) {
        DatabaseReference mDatabaseSolicitud = FirebaseDatabase.getInstance().getReference().child("Solicitudes");
        mDatabaseSolicitud.orderByChild("emailRem").equalTo(emailActual);
        mDatabaseSolicitud.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SolicitudPersistence solPer;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        solPer = snapshot.getValue(SolicitudPersistence.class);
                        if ((!solPer.getEmailRem().isEmpty() && solPer.getEmailRem().equals(emailActual)) && (!solPer.getEmailSol().isEmpty() && solPer.getEmailSol().equals(emailSol))) {
                            String id = snapshot.getKey();
                            actualizarSolicitante(solPer, id, idP);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void actualizarSolicitante(final SolicitudPersistence solicitud, final String id, final String idP) {
        mDatabaseBasic.orderByChild("email").equalTo(solicitud.getEmailSol());
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilBasicoPersistence user;
                    ReferenciaGrupo familiar = new ReferenciaGrupo(solicitud.getEmailRem(), solicitud.getRolRem());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(solicitud.getEmailSol())) {
                            String idChild = snapshot.getKey();
                            mDatabaseBasic.child(idChild).child("Grupo").child(idP).setValue(familiar);
                            eliminarSolicitud(id);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void eliminarSolicitud(String idSolicitud) {
        DatabaseReference mDatabaseSolicitud = FirebaseDatabase.getInstance().getReference().child("Solicitudes").child(idSolicitud);
        mDatabaseSolicitud.removeValue();
        Intent intent = new Intent(getApplicationContext(), FamilyGroup.class);
        intent.putExtra("emailActual", emailActual);
        startActivityForResult(intent, 0);
    }

    private void eliminarSolicitudNeg(final String emailActual, final String emailSol) {
        DatabaseReference mDatabaseSolicitud = FirebaseDatabase.getInstance().getReference().child("Solicitudes");
        mDatabaseSolicitud.orderByChild("emailRem").equalTo(emailActual);
        mDatabaseSolicitud.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SolicitudPersistence solPer = new SolicitudPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        solPer = snapshot.getValue(SolicitudPersistence.class);
                        if ((!solPer.getEmailRem().isEmpty() && solPer.getEmailRem().equals(emailActual)) && (!solPer.getEmailSol().isEmpty() && solPer.getEmailSol().equals(emailSol))) {
                            String id = snapshot.getKey();
                            DatabaseReference mDatabaseSolicitud = FirebaseDatabase.getInstance().getReference().child("Solicitudes").child(id);
                            mDatabaseSolicitud.removeValue();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
