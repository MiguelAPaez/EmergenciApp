package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FacadeNegocio bussiness = new ImplementacionNegocio();
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!bussiness.verificarSesion()) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!bussiness.verificarSesion()) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivityForResult(intent, 0);
        }
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

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
                startActivityForResult(intent, 0);
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

    }
}