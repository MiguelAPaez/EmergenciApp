package com.example.miguelapaez.emergenciapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmergencyOptions extends AppCompatActivity {
    String emailActual;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_emergency_options );
        getSupportActionBar().hide();
        emailActual = getIntent().getStringExtra("emailActual");

        LinearLayout health = (LinearLayout) findViewById ( R.id.linearLayoutHealthEmergency );
        health.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                Intent intent = new Intent ( v.getContext () , FamilyGroupHealth.class );
                intent.putExtra("emailActual", emailActual);
                startActivityForResult ( intent , 0 );
            }
        } );

        countDownTimer = new CountDownTimer( 20000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(Integer.valueOf ( (int) (millisUntilFinished / 1000L) ) == 10) {
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
                Toast.makeText( EmergencyOptions.this, "Informar Familiares", Toast.LENGTH_SHORT).show();
                // Enviar ubicación: https://maps.google.com/?q=4.190984,-74.4871482 (latitud, longitud)
                Intent intent = new Intent(EmergencyOptions.this, MainActivity.class);
                startActivity(intent);
            }
        }.start();
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
