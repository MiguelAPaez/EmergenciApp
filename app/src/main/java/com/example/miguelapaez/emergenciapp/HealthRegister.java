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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HealthRegister extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private FacadeNegocio bussiness;
    String name, lastName, typeId, id, age, email, password,phone, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
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
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog ( this);
        bussiness = new ImplementacionNegocio ();

        Button btnRegistrar = (Button) findViewById(R.id.buttonRegister);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario ();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    private void registrarUsuario() {
        progressDialog.setMessage("Realizando registro en linea...");
        progressDialog.show();
        Perfil user = crearUsuario();
        String response = bussiness.registrarUsuario(user,firebaseAuth,mDatabase);
        progressDialog.dismiss();
        Toast.makeText( HealthRegister.this, response, Toast.LENGTH_LONG).show();
    }

    private Perfil crearUsuario(){
        Perfil user = new Perfil(name,lastName,typeId,id,age,email,password,phone,gender);
        return user;
    }

}
