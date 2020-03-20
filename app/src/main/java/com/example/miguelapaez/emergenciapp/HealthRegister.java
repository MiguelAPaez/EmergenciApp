package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilMedico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilXEPS;
import com.example.miguelapaez.emergenciapp.Entities.PerfilxPrepagada;
import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;


public class HealthRegister extends AppCompatActivity {
    Perfil profile;
    PerfilBasico basicProfile;
    FacadeNegocio bussiness = new ImplementacionNegocio();
    Spinner eBloodType, eDisease, eEnvironmentAllergy, eMedicinesAllergy, eMedicine;
    Spinner eEPS,eRegimenEPS;
    Spinner ePrepagada;
    RadioButton ePC;
    String bloodType, disease, environmentAllergy, medicinesAllergy, medicine;
    String nombreEPS,regimenEPS;
    String nombrePrepagada;
    boolean planC;

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
        super.onCreate(savedInstanceState);
        if (bussiness.verificarSesion()) {
            startActivity(new Intent(HealthRegister.this, MainActivity.class));
        }
        setContentView(R.layout.activity_health_register);
        getSupportActionBar().hide();
        //EditText de Health Register
        eBloodType = (Spinner) findViewById(R.id.bloodTypeRegister);
        eDisease = (Spinner) findViewById(R.id.diseaseRegister);
        eEnvironmentAllergy = (Spinner) findViewById(R.id.environmentAllergyRegister);
        eMedicinesAllergy = (Spinner) findViewById(R.id.medicinesAllergyRegister);
        eMedicine = (Spinner) findViewById(R.id.medicineRegister);
        //EditText de EPS
        eEPS = (Spinner) findViewById(R.id.epsRegister);
        eRegimenEPS = (Spinner) findViewById(R.id.epsRegimeRegister);
        ePC = (RadioButton) findViewById(R.id.radio_complementary_plan_yes);
        //EditText de Prepagada
        ePrepagada = (Spinner) findViewById(R.id.prepaidMedicineRegister);

        //Recepción de datos Activity Register
       profile = (Perfil) getIntent().getSerializableExtra("profile");
       basicProfile = (PerfilBasico) getIntent().getSerializableExtra("basicProfile");


        // Objetos de negocio
        Button btnRegistrar = (Button) findViewById(R.id.buttonRegister);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenarDatos();
                registrarUsuario();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (bussiness.verificarSesion()) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

    }
    private void llenarDatos(){
        //Datos Medicos
        bloodType = eBloodType.getSelectedItem().toString().trim();
        disease = eDisease.getSelectedItem().toString().trim();
        environmentAllergy = eEnvironmentAllergy.getSelectedItem().toString().trim();
        medicinesAllergy = eMedicinesAllergy.getSelectedItem().toString().trim();
        medicine = eMedicine.getSelectedItem().toString().trim();
        //EPS
        nombreEPS = eEPS.getSelectedItem().toString().trim();
        regimenEPS = eRegimenEPS.getSelectedItem().toString().trim();
        if (ePC.isChecked()){
            planC = true;
        }
        else {
            planC = false;
        }
        //Prepagada
        nombrePrepagada = ePrepagada.getSelectedItem().toString().trim();
    }
    private void registrarUsuario() {
        if (bussiness.registrarUsuario(profile)) {
            Toast.makeText(HealthRegister.this, "Usuario creado", Toast.LENGTH_LONG).show();
            bussiness.guardarPerfilBasico(basicProfile);
            bussiness.guardarPerfilMedico(crearPerfilMedico());
            bussiness.guardarPerfilXEPS(crearPerfilXEPS());
            bussiness.guardarPerfilXPrepagada(crearPerfilXPrepagada());
        } else {
            Toast.makeText(HealthRegister.this, "Error al crear Usuario", Toast.LENGTH_LONG).show();
        }
    }
    private PerfilMedico crearPerfilMedico(){
        PerfilMedico user = new PerfilMedico(profile.getEmail(),bloodType,disease,environmentAllergy,medicinesAllergy,medicine);
        return user;
    }
    private PerfilXEPS crearPerfilXEPS(){
        PerfilXEPS user = new PerfilXEPS(profile.getEmail(),nombreEPS,regimenEPS,planC);
        return user;
    }
    private PerfilxPrepagada crearPerfilXPrepagada(){
        PerfilxPrepagada user = new PerfilxPrepagada(profile.getEmail(),nombrePrepagada);
        return user;
    }
}
