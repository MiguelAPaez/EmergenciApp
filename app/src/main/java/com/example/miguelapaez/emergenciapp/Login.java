package com.example.miguelapaez.emergenciapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;

public class Login extends AppCompatActivity {

    TextView message;
    FacadeNegocio bussiness = new ImplementacionNegocio();
    @Override
    public void onResume() {
        super.onResume();
        if (bussiness.verificarSesion()) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivityForResult(intent, 0);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (bussiness.verificarSesion()){
            startActivity(new Intent(Login.this, MainActivity.class));
        }
        setContentView ( R.layout.activity_login );
        getSupportActionBar().hide();
        //Negocio
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
                String email = eEmail.getText().toString().trim();
                String password = ePassword.getText().toString().trim();
                bussiness.iniciarSesion(email,password);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
                if (!bussiness.verificarSesion()){
                    Toast.makeText(v.getContext(),"Error al iniciar sesión",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(v.getContext(),"Sesión iniciada",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(v.getContext(),MainActivity.class));
                }
            }
        });

        txtSub.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent ( v.getContext(), Register.class);
                intent.putExtra ( "activity" , "login" );
                startActivityForResult(intent, 0);
            }
        });

    }
}
