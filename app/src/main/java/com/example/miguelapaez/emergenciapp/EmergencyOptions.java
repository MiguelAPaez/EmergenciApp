package com.example.miguelapaez.emergenciapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.ReferenciaGrupoPersistence;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmergencyOptions extends AppCompatActivity {
    String emailActual;
    CountDownTimer countDownTimer;
    DatabaseReference mDatabaseBasic;
    private FusedLocationProviderClient mFusedLocationClient;
    private double latitudUser = 0;
    private double longitudUser = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_emergency_options );
        getSupportActionBar ().hide ();
        emailActual = getIntent ().getStringExtra ( "emailActual" );
        mDatabaseBasic = FirebaseDatabase.getInstance ().getReference ( "Perfiles Basicos" );
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient( this);

        LinearLayout health = (LinearLayout) findViewById ( R.id.linearLayoutHealthEmergency );
        health.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel ();
                Intent intent = new Intent ( v.getContext () , FamilyGroupHealth.class );
                intent.putExtra ( "emailActual" , emailActual );
                startActivityForResult ( intent , 0 );
            }
        } );

        countDownTimer = new CountDownTimer ( 20000 , 1000 ) {
            public void onTick(long millisUntilFinished) {
                if (Integer.valueOf ( (int) (millisUntilFinished / 1000L) ) == 10) {
                    AlertDialog.Builder message = new AlertDialog.Builder ( EmergencyOptions.this );
                    message.setTitle ( "Atención" );

                    message.setMessage ( "Si en 10 segundos no se detecta actividad se enviará una alerta a su grupo familiar" );
                    message.setPositiveButton ( "Ok" , new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialogInterface , int i) {
                            Toast.makeText ( EmergencyOptions.this , "Ok!" , Toast.LENGTH_SHORT ).show ();
                        }
                    } );

                    AlertDialog dialog = message.create ();
                    dialog.show ();
                }
            }

            public void onFinish() {
                Toast.makeText ( EmergencyOptions.this , "Informar Familiares" , Toast.LENGTH_SHORT ).show ();
                notificarFamiliar ();
                Intent intent = new Intent ( EmergencyOptions.this , MainActivity.class );
                startActivity ( intent );
            }
        }.start ();

        if (checkSelfPermission ( Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission ( Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation ().addOnSuccessListener (
                this , new OnSuccessListener <Location> () {
                    @Override
                    public void onSuccess(Location location) {
                        Log.i ( "LOCATION" , "onSuccess location" );
                        if (location != null) {
                            longitudUser = location.getLongitude ();
                            latitudUser = location.getLatitude ();
                        }
                    }
                } );

    }
    private void notificarFamiliar(){
        mDatabaseBasic.orderByChild("email").equalTo(emailActual);
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    PerfilBasicoPersistence user;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.exists()){
                            user = snapshot.getValue(PerfilBasicoPersistence.class);
                            if(user.getEmail().equals(emailActual)){
                                String id = snapshot.getKey();
                                cargarFamiliar(id);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cargarFamiliar(String id){
        mDatabaseBasic.child(id).child("Grupo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ReferenciaGrupoPersistence fam;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.exists()){
                            fam = snapshot.getValue(ReferenciaGrupoPersistence.class);
                            obtenerNumero(fam.getEmail());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void obtenerNumero(final String email){
        mDatabaseBasic.orderByChild("email").equalTo(email);
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    PerfilBasicoPersistence user;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.exists()){
                            user = snapshot.getValue(PerfilBasicoPersistence.class);
                            if(user.getEmail().equals(email)){
                                System.out.println("Num: " + user.getPhone());
                                String mensaje = "Tengo una emergencia, por favor comunícate conmigo. Esta es mi ubicacación: " + "https://maps.google.com/?q="+latitudUser+","+longitudUser;
                                enviarMensaje(user.getPhone(),mensaje);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void enviarMensaje(String numero , String mensaje) {
        try {
            SmsManager sms = SmsManager.getDefault ();
            sms.sendTextMessage ( numero , null , mensaje , null , null );
            Toast.makeText ( getApplicationContext () , "Mensaje Enviado." , Toast.LENGTH_LONG ).show ();
        } catch (Exception e) {
            Toast.makeText ( getApplicationContext () , "Mensaje no enviado, datos incorrectos", Toast.LENGTH_LONG).show();
                        e.printStackTrace ();
        }
    }


    @Override
    protected void onPause() {
        super.onPause ();
        countDownTimer.cancel();
    }


}
