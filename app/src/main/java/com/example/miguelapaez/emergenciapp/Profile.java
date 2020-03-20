package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.Persistence;

public class Profile extends AppCompatActivity {
private TextView nameProfile;
Persistence persistence = new Persistence();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_profile );
        getSupportActionBar().hide();
        nameProfile = (TextView) findViewById(R.id.nameProfile);
        PerfilBasicoPersistence basicProfile = persistence.getBasicProfile();
        nameProfile.setText(basicProfile.getName());
    }
}
