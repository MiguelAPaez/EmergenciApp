package com.example.miguelapaez.emergenciapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class activity_buscarFamiliar extends AppCompatActivity {

    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_familiar);
        getSupportActionBar().hide();

        message = (TextView) findViewById ( R.id.messageBuscar );
        String font_path = "font/Arvo-Regular.ttf";
        Typeface TF = Typeface.createFromAsset ( getAssets (), font_path );
        message.setTypeface ( TF );

    }
}
