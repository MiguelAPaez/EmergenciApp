package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class FamilyGroupHealth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_family_group_health );
        getSupportActionBar().hide();

        LinearLayout health = (LinearLayout) findViewById ( R.id.linearLayoutFamilyGroupHealth1 );
        health.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( v.getContext () , HealthQuestion.class );
                startActivityForResult ( intent , 0 );
            }
        } );

    }
}
