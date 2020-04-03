package com.example.miguelapaez.emergenciapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.example.miguelapaez.emergenciapp.Persistence.SolicitudPersistence;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String emailActual;
    FacadeNegocio bussiness = new ImplementacionNegocio();
    private static final int CODIGO_PERMISOS_CALL = 1;
    private static final int CODIGO_PERMISOS_LOCATION = 2;
    private boolean tienePermisoCall = false;
    private boolean tienePermisoLocation = false;
    String dial = "tel:321";
    ProgressDialog progressDialog;
    ImageView btnNotificaciones;

    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (!bussiness.verificarSesion()) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivityForResult(intent, 0);
        }
        checkNotify();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (!bussiness.verificarSesion()) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivityForResult(intent, 0);
        }
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        verificarYPedirPermisos();
        Button btnLogOut = (Button) findViewById(R.id.buttonLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bussiness.cerrarSesion()) {
                    Toast.makeText(MainActivity.this, "Sesión cerrada", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(v.getContext(), Login.class));
                    finish();
                }
            }
        });

        LinearLayout profile = (LinearLayout) findViewById(R.id.linearLayoutProfileMain);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Profile.class);
                startActivity(intent);
            }
        });

        LinearLayout emergency = (LinearLayout) findViewById(R.id.linearLayoutEmergencyMain);
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EmergencyOptions.class);
                startActivityForResult(intent, 0);
            }
        });

        LinearLayout lineEmergency = (LinearLayout) findViewById(R.id.linearLayoutLineEmergencyMain);
        lineEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerLlamada(dial);
            }
        });

        LinearLayout grupoFamiliar = (LinearLayout) findViewById(R.id.linearLayoutFamilyGroupMain);
        grupoFamiliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FamilyGroup.class);
                intent.putExtra("emailActual", currentUser.getEmail());
                startActivityForResult(intent, 0);
            }
        });


        btnNotificaciones = (ImageView) findViewById( R.id.buttonNotification);
        btnNotificaciones.setEnabled(false);
        checkNotify();
        btnNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnNotificaciones.isEnabled()){
                    progressDialog = new ProgressDialog(v.getContext());
                    progressDialog.setMessage("Cargando notificaciones...");
                    progressDialog.show();
                    DatabaseReference mDatabaseSolicitud= FirebaseDatabase.getInstance().getReference().child("Solicitudes");
                    mDatabaseSolicitud.orderByChild("emailRem").equalTo(currentUser.getEmail());
                    mDatabaseSolicitud.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                SolicitudPersistence solPer = new SolicitudPersistence();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    solPer = snapshot.getValue(SolicitudPersistence.class);
                                    if (!solPer.getEmailRem().isEmpty() && solPer.getEmailRem().equals(currentUser.getEmail())) {
                                        String id = snapshot.getKey();
                                        Intent intent = new Intent(getApplicationContext(), Notificaciones.class);
                                        intent.putExtra("id",id);
                                        intent.putExtra("solicitud", solPer);
                                        startActivityForResult(intent, 0);
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
            }
        });
    }


    private void verificarYPedirPermisos() {
        if (!tienePermisoCall) {
            permisoCall();
        }
        if (!tienePermisoLocation) {
            permisoLocation();
        }
    }

    private void permisoCall() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {

        } else {
            // Si no, entonces pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CODIGO_PERMISOS_CALL
            );
        }
    }

    private void permisoLocation() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {

        } else {
            // Si no, entonces pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PERMISOS_LOCATION
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "El permiso para la llamada está habilitado", Toast.LENGTH_SHORT).show();
                    tienePermisoCall = true;
                    verificarYPedirPermisos();
                } else {
                    Toast.makeText(MainActivity.this, "El permiso para la llamada está denegado", Toast.LENGTH_SHORT).show();
                    if (!tienePermisoLocation) {
                        permisoLocation();
                    }
                }
                break;
            case CODIGO_PERMISOS_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "El permiso para ubicacón está habilitado", Toast.LENGTH_SHORT).show();
                    tienePermisoLocation = true;
                } else {
                    Toast.makeText(MainActivity.this, "El permiso para ubicación está denegado", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void hacerLlamada(String dial) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        } else {
            verificarYPedirPermisos();
        }
    }
    private void checkNotify() {
        btnNotificaciones = (ImageView) findViewById( R.id.buttonNotification);
        DatabaseReference mDatabaseSolicitud = FirebaseDatabase.getInstance().getReference().child("Solicitudes");
        mDatabaseSolicitud.orderByChild("emailRem").equalTo(currentUser.getEmail());
        mDatabaseSolicitud.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean status = false;
                    SolicitudPersistence solPer = new SolicitudPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        solPer = snapshot.getValue(SolicitudPersistence.class);
                        if (!solPer.getEmailRem().isEmpty() && solPer.getEmailRem().equals(currentUser.getEmail())) {
                            btnNotificaciones.setImageResource(R.drawable.notification_enabled);
                            btnNotificaciones.setEnabled(true);
                            status = true;
                            break;
                        }
                    }
                    if (!status){
                        btnNotificaciones.setEnabled(false);
                    }
                }
                else{
                    btnNotificaciones.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
