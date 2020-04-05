package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class EmergencyOptions extends AppCompatActivity {
    String emailActual;

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
                Intent intent = new Intent ( v.getContext () , FamilyGroupHealth.class );
                intent.putExtra("emailActual", emailActual);
                startActivityForResult ( intent , 0 );
            }
        } );

    }
}
