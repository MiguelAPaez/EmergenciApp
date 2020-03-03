package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        //Comprobar Sección
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(MainActivity.this,Login.class);
            startActivityForResult(intent,0);
        }
        else {
            Log.e("Usuario","Encontrado"+ currentUser.getEmail());
        }
        setContentView ( R.layout.activity_main );
        getSupportActionBar().hide();

        Button btn = (Button) findViewById( R.id.buttonLogin);
        Button btnLogOut = (Button) findViewById(R.id.buttonLogOut);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( v.getContext(), Login.class);
                startActivityForResult(intent, 0);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this,"Sección cerrada",Toast.LENGTH_LONG).show();
                startActivity(new Intent(v.getContext(),Login.class));
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
