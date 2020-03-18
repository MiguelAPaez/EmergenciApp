package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MedicalCenters extends AppCompatActivity {

    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_medical_centers );
        getSupportActionBar().hide();

        message = (TextView) findViewById ( R.id.textMessageMedicalCenters );
        String font_path = "font/Arvo-Regular.ttf";
        Typeface TF = Typeface.createFromAsset ( getAssets (), font_path );
        message.setTypeface ( TF );

        LinearLayout hospital = (LinearLayout) findViewById ( R.id.linearLayoutMedicalCenter1 );
        hospital.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( v.getContext () , MapsActivity.class );
                intent.putExtra ( "latitud" , 4.628551);
                intent.putExtra ( "longitud" , -74.064039);
                intent.putExtra ( "name" , "Hospital San Ignacio");
                startActivityForResult ( intent , 0 );
            }
        } );

    }
}
