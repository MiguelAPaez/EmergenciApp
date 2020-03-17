package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class HealthQuestion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_health_question );
        getSupportActionBar().hide();

        LinearLayout pecho = (LinearLayout) findViewById ( R.id.linearLayoutPecho );
        pecho.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( v.getContext () , MedicalCenters.class );
                startActivityForResult ( intent , 0 );
            }
        } );

    }
}
