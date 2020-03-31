package com.example.miguelapaez.emergenciapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilMedico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilXEPS;
import com.example.miguelapaez.emergenciapp.Entities.PerfilxPrepagada;
import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.example.miguelapaez.emergenciapp.Validaciones.Validaciones;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class HealthRegister extends AppCompatActivity {
    Perfil profile;
    PerfilBasico basicProfile;
    String activity;
    FacadeNegocio bussiness = new ImplementacionNegocio();
    Spinner eBloodType, eDisease, eEnvironmentAllergy, eMedicinesAllergy, eMedicine;
    Spinner eEPS,eRegimenEPS;
    Spinner ePrepagada;
    RadioButton ePC;
    String bloodType, disease, environmentAllergy, medicinesAllergy, medicine;
    String nombreEPS,regimenEPS;
    String nombrePrepagada;
    String redColor = "#40FF0000";
    private LinearLayout editEpsLinear, editAfiliacionLinear, editComplementaryPlanLinear, editPrepaidMedicineLinear, editRhLinear,
            editDiseaseLinear, editambientalAllergyLinear, editMedicineAllergyLinear, editMedicineLinear;
    boolean planC;
    ScrollView scrollView;
    Validaciones objValidar;
    boolean validacionOK;
    ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(this);

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

       //LinearLayout's
        editEpsLinear = (LinearLayout) findViewById ( R.id.linearLayoutEPSRegister );
        editAfiliacionLinear = (LinearLayout) findViewById ( R.id.linearLayoutEPSRegimeRegister );
        editComplementaryPlanLinear = (LinearLayout) findViewById ( R.id.linearLayoutComplementaryPlanRegister );
        editPrepaidMedicineLinear = (LinearLayout) findViewById ( R.id.linearLayoutPrepaidMedicineRegister );
        editRhLinear = (LinearLayout) findViewById ( R.id.linearLayoutBloodTypeRegister );
        editDiseaseLinear = (LinearLayout) findViewById ( R.id.linearLayoutDiseaseRegister );
        editambientalAllergyLinear = (LinearLayout) findViewById ( R.id.linearLayoutEnvironmentAllergyRegister );
        editMedicineAllergyLinear = (LinearLayout) findViewById ( R.id.linearLayoutMedicinesAllergyRegister );
        editMedicineLinear = (LinearLayout) findViewById ( R.id.linearLayoutMedicineRegister );

        //Validaciones
        objValidar = new Validaciones();

        //ScrollView
        scrollView = (ScrollView) findViewById ( R.id.scrollViewHealthRegister );

        // Objetos de negocio
        Button btnRegistrar = (Button) findViewById(R.id.buttonRegister);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HealthRegister.ThreadB b = new HealthRegister.ThreadB ();
                b.start();
                synchronized(b){
                    try{
                        System.out.println("Waiting for b to complete...");
                        b.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    if(validacionOK) {
                        llenarDatos ();
                        registrarUsuario ();
                    }
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        boolean response = false;
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(profile.getEmail(), profile.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            bussiness.guardarPerfil(profile);
                            bussiness.guardarPerfilBasico(basicProfile);
                            bussiness.guardarPerfilMedico(crearPerfilMedico());
                            bussiness.guardarPerfilXEPS(crearPerfilXEPS());
                            bussiness.guardarPerfilXPrepagada(crearPerfilXPrepagada());
                            Toast.makeText(HealthRegister.this, "Usuario creado", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent ( getApplicationContext() , MainActivity.class );
                            startActivityForResult ( intent , 0 );
                        }
                        else {
                            Toast.makeText(HealthRegister.this, "Error al crear Usuario", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
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

    class ThreadB extends Thread{

        @Override
        public void run(){
            synchronized(this){
                validacionOK = validarDatos ();
                notify();
            }
        }

    }

    public boolean validarDatos(){

        //Validación Nombre de EPS
        String eps = eEPS.getSelectedItem().toString().trim();
        if(objValidar.isEquals ( eps, "EPS" )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su EPS" , Toast.LENGTH_SHORT ).show ();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                    editEpsLinear.setBackgroundColor ( Color.parseColor ( redColor ) );
                }
            });
            return false;
        }

        //Validación EPS Régimen
        String epsRegimen = eRegimenEPS.getSelectedItem().toString().trim();
        if(objValidar.isEquals ( epsRegimen, "Régimen de Afiliación EPS" )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su Régimen de Afiliación EPS" , Toast.LENGTH_SHORT ).show ();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                    editAfiliacionLinear.setBackgroundColor ( Color.parseColor ( redColor ) );
                }
            });
            return false;
        }

        //Validación Medicina Prepagada
        String prepaidM = ePrepagada.getSelectedItem().toString().trim();
        if(objValidar.isEquals ( prepaidM, "Medicina Prepagada" )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese Medicina Prepagada" , Toast.LENGTH_SHORT ).show ();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                    editPrepaidMedicineLinear.setBackgroundColor ( Color.parseColor ( redColor ) );
                }
            });
            return false;
        }

        //Validación RH
        String rh = eBloodType.getSelectedItem().toString().trim();
        if(objValidar.isEquals ( rh, "Tipo de Sangre" )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su Tipo de Sangre" , Toast.LENGTH_SHORT ).show ();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                    editRhLinear.setBackgroundColor ( Color.parseColor ( redColor ) );
                }
            });
            return false;
        }

        //Validación Enfermedad
        String disease = eDisease.getSelectedItem().toString().trim();
        if(objValidar.isEquals ( disease, "¿Tienes alguna Enfermedad Crónica?" )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese Enfermedad Crónica" , Toast.LENGTH_SHORT ).show ();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                    editDiseaseLinear.setBackgroundColor ( Color.parseColor ( redColor ) );
                }
            });
            return false;
        }

        //Validación Alergía Ambiental
        String environmentA = eEnvironmentAllergy.getSelectedItem().toString().trim();
        if(objValidar.isEquals ( environmentA, "¿Posees alguna Alergía Ambiental?" )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese Alergía Ambiental" , Toast.LENGTH_SHORT ).show ();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                    editambientalAllergyLinear.setBackgroundColor ( Color.parseColor ( redColor ) );
                }
            });
            return false;
        }

        //Validación Alergía Ambiental
        String medicineA = eMedicinesAllergy.getSelectedItem().toString().trim();
        if(objValidar.isEquals ( medicineA, "¿Posees alguna Alergía a un Medicamento?" )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese Alergía a Medicamento" , Toast.LENGTH_SHORT ).show ();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                    editMedicineAllergyLinear.setBackgroundColor ( Color.parseColor ( redColor ) );
                }
            });
            return false;
        }

        //Validación Alergía Ambiental
        String medicine = eMedicine.getSelectedItem().toString().trim();
        if(objValidar.isEquals ( medicine, "¿Tomas algún Medicamento?" )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese Medicamento" , Toast.LENGTH_SHORT ).show ();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                    editMedicineLinear.setBackgroundColor ( Color.parseColor ( redColor ) );
                }
            });
            return false;
        }

        return true;

    }

}
