package com.example.miguelapaez.emergenciapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        Button btn = (Button) findViewById(R.id.buttonLogOut);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( v.getContext(), activity_registro_familiar.class);
                startActivity(intent);
            }
        });

        message = (TextView) findViewById ( R.id.messageBuscar );
        String font_path = "font/Arvo-Regular.ttf";
        Typeface TF = Typeface.createFromAsset ( getAssets (), font_path );
        message.setTypeface ( TF );


    }
}
