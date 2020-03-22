package com.example.miguelapaez.emergenciapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class mostrarFamiliarEncontrado extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mostrar_familiar_encontrado);
    }
}
