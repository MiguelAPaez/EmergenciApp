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
    TextView eName;
    ImageView ePic;
    DatabaseReference mDatabaseSolicitud, mDatabaseBasic;
    SolicitudPersistence solicitud;
    Button eBtnAccept,eBtnNeg;
    String idSolicitud;
    ProgressDialog progressDialog;

    ListView listItemsNotificaciones;
    private AdapterFamilyGroup adaptador;
    ArrayList <EntityFamilyGroup> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        /*eName = (TextView) findViewById(R.id.textViewNameUserNotification);
        ePic = (ImageView) findViewById(R.id.imageViewUserNotification);
        eBtnAccept = (Button) findViewById(R.id.buttonConfirmarPeticion);
        eBtnNeg = (Button) findViewById(R.id.buttonNegarPeticion);*/

        listItemsNotificaciones = findViewById ( R.id.listViewNotificaciones );
        adaptador = new AdapterFamilyGroup ( this, listItems);
        listItemsNotificaciones.setAdapter(adaptador);

        solicitud = (SolicitudPersistence) getIntent().getSerializableExtra("solicitud");
        idSolicitud = getIntent().getStringExtra("id");
        mDatabaseSolicitud = FirebaseDatabase.getInstance().getReference().child("Solicitudes").child(idSolicitud);
        mDatabaseBasic = FirebaseDatabase.getInstance().getReference().child("Perfiles Basicos");
        cargarSolicitante();

        listItemsNotificaciones.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView <?> adapterView , View view , int i , long l) {
                AlertDialog.Builder message = new AlertDialog.Builder ( Notificaciones.this );
                message.setTitle ( listItems.get(i).getName () );
                message.setMessage ( "Desea agregarte a su Grupo Familiar" );
                message.setPositiveButton ( "Confirmar" , new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialogInterface , int i) {
                    /*
                        progressDialog = new ProgressDialog(v.getContext());
                        progressDialog.setMessage("Aceptando solicitud...");
                        progressDialog.show();
                        mDatabaseBasic.orderByChild("email").equalTo(solicitud.getEmailRem());
                        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    PerfilBasicoPersistence user = new PerfilBasicoPersistence();
                                    ReferenciaGrupo familiar = new ReferenciaGrupo(solicitud.getEmailSol(),solicitud.getRolSol());
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                                        if (!user.getEmail().isEmpty() && user.getEmail().equals(solicitud.getEmailRem())) {
                                            String idChild = snapshot.getKey();
                                            mDatabaseBasic.child(idChild).child("Grupo").child(idSolicitud).setValue(familiar);
                                            actualizarSolicitante();
                                            break;
                                        }
                                    }
                                }
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                     */
                    }
                } );
                message.setNegativeButton ( "Eliminar" , new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialogInterface , int i) {
                        Toast.makeText( Notificaciones.this, "Solicitud eliminada", Toast.LENGTH_SHORT).show();
                        eliminarSolicitud();
                    }
                } );

                AlertDialog dialog = message.create ();
                dialog.show();
            }
        } );


        /*eBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setMessage("Aceptando solicitud...");
                progressDialog.show();
                mDatabaseBasic.orderByChild("email").equalTo(solicitud.getEmailRem());
                mDatabaseBasic.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            PerfilBasicoPersistence user = new PerfilBasicoPersistence();
                            ReferenciaGrupo familiar = new ReferenciaGrupo(solicitud.getEmailSol(),solicitud.getRolSol());
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                user = snapshot.getValue(PerfilBasicoPersistence.class);
                                if (!user.getEmail().isEmpty() && user.getEmail().equals(solicitud.getEmailRem())) {
                                    String idChild = snapshot.getKey();
                                    mDatabaseBasic.child(idChild).child("Grupo").child(idSolicitud).setValue(familiar);
                                    actualizarSolicitante();
                                    break;
                                }
                            }
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        eBtnNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Solicitud eliminada",Toast.LENGTH_SHORT).show();
                eliminarSolicitud();
            }
        });*/
    }
    private void cargarSolicitante(){
        mDatabaseBasic.orderByChild("email").equalTo(solicitud.getEmailSol());
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    PerfilBasicoPersistence user = new PerfilBasicoPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(solicitud.getEmailSol())) {
                            break;
                        }
                    }
                    llenarSolicitante(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void llenarSolicitante(PerfilBasicoPersistence user){
        String name = user.getName() + " " + user.getLastName();
        eName.setText(name);
        if (user.getGender().equals("Masculino")) {
            ePic.setImageResource(R.drawable.hombre);
        } else if (user.getGender().equals("Femenino")) {
            ePic.setImageResource(R.drawable.mujer);
        }
    }
    private void actualizarSolicitante(){
        mDatabaseBasic.orderByChild("email").equalTo(solicitud.getEmailSol());
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    PerfilBasicoPersistence user = new PerfilBasicoPersistence();
                    ReferenciaGrupo familiar = new ReferenciaGrupo(solicitud.getEmailRem(),solicitud.getRolRem());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(solicitud.getEmailSol())) {
                            String idChild = snapshot.getKey();
                            mDatabaseBasic.child(idChild).child("Grupo").child(idSolicitud).setValue(familiar);
                            eliminarSolicitud();
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
    private void eliminarSolicitud(){
        mDatabaseSolicitud.removeValue();
        Intent intent = new Intent(getApplicationContext(), FamilyGroup.class);
        intent.putExtra("emailActual",solicitud.getEmailRem());
        startActivityForResult(intent, 0);
    }
}
