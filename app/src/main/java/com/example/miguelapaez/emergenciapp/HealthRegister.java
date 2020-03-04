package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;


public class HealthRegister extends AppCompatActivity {
    String name, lastName, typeId, id, age, email, password,phone, gender;
    FacadeNegocio bussiness = new ImplementacionNegocio();
    @Override
    public void onResume() {
        super.onResume();
        if (bussiness.verificarSesion()) {
            Intent intent = new Intent(HealthRegister.this, MainActivity.class);
            startActivityForResult(intent, 0);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (bussiness.verificarSesion()){
            startActivity(new Intent(HealthRegister.this, MainActivity.class));
        }
        setContentView ( R.layout.activity_health_register );
        getSupportActionBar().hide();

        //Recepción de datos Activity Register
        name = getIntent().getExtras().getString("name");
        Log.i( "NAME", String.valueOf ( name ) );
        lastName = getIntent().getExtras().getString("lastName");
        Log.i( "LASTNAME", String.valueOf ( lastName ) );
        typeId = getIntent().getExtras().getString("typeId");
        Log.i( "TYPEID", String.valueOf ( typeId ) );
        id = getIntent().getExtras().getString("id");
        Log.i( "ID", String.valueOf ( id ) );
        age = getIntent().getExtras().getString("age");
        Log.i( "AGE", String.valueOf ( age ) );
        email = getIntent().getExtras().getString("email");
        Log.i( "EMAIL", String.valueOf ( email ) );
        password = getIntent().getExtras().getString("password");
        Log.i( "PASSWORD", String.valueOf ( password ) );
        phone = getIntent().getExtras().getString("phone");
        Log.i( "CEL", String.valueOf ( phone ) );
        gender = getIntent().getExtras().getString("gender");
        Log.i( "GENDER", String.valueOf ( gender ) );

        // Objetos de negocio


        Button btnRegistrar = (Button) findViewById(R.id.buttonRegister);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario ();
                if (bussiness.verificarSesion()) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

    }

    private void registrarUsuario() {
        Perfil user = crearUsuario();
        String response = bussiness.registrarUsuario(user);
        Toast.makeText( HealthRegister.this, response, Toast.LENGTH_LONG).show();
    }

    private Perfil crearUsuario(){
        Perfil user = new Perfil(name,lastName,typeId,id,age,email,password,phone,gender);
        return user;
    }

}
