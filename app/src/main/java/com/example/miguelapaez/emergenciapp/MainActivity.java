package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        getSupportActionBar().hide();

        Button btn = (Button) findViewById( R.id.buttonLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( v.getContext(), Login.class);
                startActivityForResult(intent, 0);
            }
        });

        LinearLayout profile = (LinearLayout) findViewById( R.id.linearLayoutProfileMain);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( v.getContext(), Profile.class);
                startActivityForResult(intent, 0);
            }
        });

    }
}
