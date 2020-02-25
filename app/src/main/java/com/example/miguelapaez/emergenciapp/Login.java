package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );
        getSupportActionBar().hide();
        message = (TextView) findViewById ( R.id.textMessageIntro );
        String font_path = "font/Arvo-Regular.ttf";
        Typeface TF = Typeface.createFromAsset ( getAssets (),font_path );
        message.setTypeface ( TF );

        final TextView txtSub = (TextView)findViewById(R.id.meesageSignUp);

        txtSub.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent ( v.getContext(), Register.class);
                startActivityForResult(intent, 0);
            }
        });

    }
}
