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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;

public class MainActivity extends AppCompatActivity {
    FacadeNegocio bussiness = new ImplementacionNegocio ();
    private static final int CODIGO_PERMISOS_CALL = 1;
    private boolean tienePermisoCall = false;
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

        LinearLayout lineEmergency = (LinearLayout) findViewById ( R.id.linearLayoutLineEmergencyMain );
        lineEmergency.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                verificarYPedirPermisosCall ();
            }
        } );

        LinearLayout grupoFamiliar = (LinearLayout) findViewById ( R.id.linearLayoutFamilyGroupMain );
        grupoFamiliar.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( v.getContext () , FamilyGroup.class );
                startActivityForResult ( intent , 0 );
            }
        } );
    }

    private void verificarYPedirPermisosCall() {
        int estadoDePermiso = ContextCompat.checkSelfPermission ( MainActivity.this , Manifest.permission.CALL_PHONE );
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoDeLlamadaConcedido ();
        } else {
            // Si no, entonces pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions (
                    MainActivity.this ,
                    new String[]{Manifest.permission.CALL_PHONE} ,
                    CODIGO_PERMISOS_CALL
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisoDeLlamadaConcedido ();
                } else {
                    permisoDeLlamadaDenegado ();
                }
                break;
        }
    }

    private void permisoDeLlamadaConcedido() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity ( new Intent ( Intent.ACTION_CALL , Uri.parse ( dial ) ) );
        }
        tienePermisoCall = true;
    }

    private void permisoDeLlamadaDenegado() {
        Toast.makeText(MainActivity.this, "El permiso para la llamada está denegado", Toast.LENGTH_SHORT).show();
    }

}