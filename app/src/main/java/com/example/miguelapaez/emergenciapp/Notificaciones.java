package com.example.miguelapaez.emergenciapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Notificaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
    }
}
