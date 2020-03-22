package com.example.miguelapaez.emergenciapp;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
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

        final TextView txtSub = (TextView)findViewById(R.id.messageSignUpBuscar);
        final TextView txtSub2 = (TextView)findViewById(R.id.buttonBuscarFamiliar);

        txtSub.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent ( v.getContext(), activity_registro_familiar.class);
                startActivityForResult(intent, 0);
            }
        });

        txtSub2.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent ( v.getContext(), mostrarFamiliarEncontrado.class);
                startActivityForResult(intent, 0);
            }
        });

    }
}
