package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );
        getSupportActionBar().hide();
        //Negocio
        final FacadeNegocio bussiness = new ImplementacionNegocio();

        final TextView eEmail = (TextView) findViewById(R.id.emailLogin);
        final TextView ePassword = (TextView) findViewById(R.id.passwordLogin);
        Button btnLogin = (Button) findViewById(R.id.button);

        message = (TextView) findViewById ( R.id.textMessageIntro );
        String font_path = "font/Arvo-Regular.ttf";
        Typeface TF = Typeface.createFromAsset ( getAssets (),font_path );
        message.setTypeface ( TF );

        final TextView txtSub = (TextView)findViewById(R.id.meesageSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = eEmail.toString().trim();
                String password = ePassword.toString().trim();
                bussiness.iniciarSesion(email,password);
                if (!bussiness.verificarSesion()){
                    Toast.makeText(v.getContext(),"Error al iniciar sección",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(v.getContext(),"Sección iniciada",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(v.getContext(),MainActivity.class));
                }
            }
        });

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
