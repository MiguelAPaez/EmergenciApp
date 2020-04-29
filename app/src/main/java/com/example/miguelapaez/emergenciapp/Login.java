package com.example.miguelapaez.emergenciapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView message;
    FacadeNegocio bussiness = new ImplementacionNegocio();
    ProgressDialog progressDialog;
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
        progressDialog = new ProgressDialog(Login.this);
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
                iniciarSesionFB(email,password);
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
    private void iniciarSesionFB(String email, String password) {
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Sesión iniciada",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Error al iniciar sesión",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}
