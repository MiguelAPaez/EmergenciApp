package com.example.miguelapaez.emergenciapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;

public class MainActivity extends AppCompatActivity {
    FacadeNegocio bussiness = new ImplementacionNegocio ();
    String dial = "tel:321";

    public void onStart() {
        super.onStart ();
    }

    @Override
    public void onResume() {
        super.onResume ();
        if (!bussiness.verificarSesion ()) {
            Intent intent = new Intent ( MainActivity.this , Login.class );
            startActivityForResult ( intent , 0 );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (!bussiness.verificarSesion ()) {
            Intent intent = new Intent ( MainActivity.this , Login.class );
            startActivityForResult ( intent , 0 );
        }
        setContentView ( R.layout.activity_main );
        getSupportActionBar ().hide ();

        Button btnLogOut = (Button) findViewById ( R.id.buttonLogOut );
        btnLogOut.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (bussiness.cerrarSesion ()) {
                    Toast.makeText ( MainActivity.this , "Sesión cerrada" , Toast.LENGTH_LONG ).show ();
                    startActivity ( new Intent ( v.getContext () , Login.class ) );
                    finish ();
                }
            }
        } );

        LinearLayout profile = (LinearLayout) findViewById ( R.id.linearLayoutProfileMain );
        profile.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( v.getContext () , Profile.class );
                startActivityForResult ( intent , 0 );
            }
        } );

        LinearLayout emergency = (LinearLayout) findViewById ( R.id.linearLayoutEmergencyMain );
        emergency.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( v.getContext () , EmergencyOptions.class );
                startActivityForResult ( intent , 0 );
            }
        } );

        /*LinearLayout lineEmergency = (LinearLayout) findViewById ( R.id.linearLayoutLineEmergencyMain );
        emergency.setOnClickListener ( new View.OnClickListener () {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission ( Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
                    startActivity ( new Intent ( Intent.ACTION_CALL , Uri.parse ( dial ) ) );
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }
        });*/

        LinearLayout grupoFamiliar = (LinearLayout) findViewById(R.id.linearLayoutFamilyGroupMain);
        grupoFamiliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), activity_buscarFamiliar.class);
                startActivityForResult(intent, 0);
            }
        });

    }
}