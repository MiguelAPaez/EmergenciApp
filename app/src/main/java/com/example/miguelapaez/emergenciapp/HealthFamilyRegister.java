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

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilMedico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilXEPS;
import com.example.miguelapaez.emergenciapp.Entities.PerfilxPrepagada;
import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.example.miguelapaez.emergenciapp.Validaciones.Validaciones;

public class HealthFamilyRegister extends AppCompatActivity {

    PerfilBasico basicProfile;
    String activity;
    FacadeNegocio bussiness = new ImplementacionNegocio ();
    Spinner eBloodType, eDisease, eEnvironmentAllergy, eMedicinesAllergy, eMedicine;
    Spinner eEPS,eRegimenEPS;
    Spinner ePrepagada;
    RadioButton ePC;
    String bloodType, disease, environmentAllergy, medicinesAllergy, medicine;
    String nombreEPS,regimenEPS;
    String nombrePrepagada, emailActual, relationship;
    String redColor = "#40FF0000";
    private LinearLayout editEpsLinear, editAfiliacionLinear, editComplementaryPlanLinear, editPrepaidMedicineLinear, editRhLinear,
            editDiseaseLinear, editambientalAllergyLinear, editMedicineAllergyLinear, editMedicineLinear;
    boolean planC;
    ScrollView scrollView;
    Validaciones objValidar;
    boolean validacionOK;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_health_family_register );
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);

        //EditText de Health Register
        eBloodType = (Spinner) findViewById(R.id.bloodTypeFamilyRegister);
        eDisease = (Spinner) findViewById(R.id.diseaseFamilyRegister);
        eEnvironmentAllergy = (Spinner) findViewById(R.id.environmentAllergyFamilyRegister);
        eMedicinesAllergy = (Spinner) findViewById(R.id.medicinesAllergyFamilyRegister);
        eMedicine = (Spinner) findViewById(R.id.medicineFamilyRegister);

        //EditText de EPS
        eEPS = (Spinner) findViewById(R.id.epsFamilyRegister);
        eRegimenEPS = (Spinner) findViewById(R.id.epsRegimeFamilyRegister);
        ePC = (RadioButton) findViewById(R.id.radio_complementary_plan_family_yes);

        //EditText de Prepagada
        ePrepagada = (Spinner) findViewById(R.id.prepaidMedicineFamilyRegister);

       //Recepción de datos Activity Registro Familiar
        basicProfile = (PerfilBasico) getIntent().getSerializableExtra("basicProfile");
        relationship = getIntent().getStringExtra("relationship");
        emailActual = getIntent().getStringExtra("emailActual");

        //LinearLayout's
        editEpsLinear = (LinearLayout) findViewById ( R.id.linearLayoutEPSFamilyRegister );
        editAfiliacionLinear = (LinearLayout) findViewById ( R.id.linearLayoutEPSRegimeFamilyRegister );
        editComplementaryPlanLinear = (LinearLayout) findViewById ( R.id.linearLayoutComplementaryPlanFamilyRegister );
        editPrepaidMedicineLinear = (LinearLayout) findViewById ( R.id.linearLayoutPrepaidMedicineFamilyRegister );
        editRhLinear = (LinearLayout) findViewById ( R.id.linearLayoutBloodTypeFamilyRegister );
        editDiseaseLinear = (LinearLayout) findViewById ( R.id.linearLayoutDiseaseFamilyRegister );
        editambientalAllergyLinear = (LinearLayout) findViewById ( R.id.linearLayoutEnvironmentAllergyFamilyRegister );
        editMedicineAllergyLinear = (LinearLayout) findViewById ( R.id.linearLayoutMedicinesAllergyFamilyRegister );
        editMedicineLinear = (LinearLayout) findViewById ( R.id.linearLayoutMedicineFamilyRegister );

        //Validaciones
        objValidar = new Validaciones();

        //ScrollView
        scrollView = (ScrollView) findViewById ( R.id.scrollViewHealthFamilyRegister );

        // Objetos de negocio
        Button btnRegistrar = (Button) findViewById( R.id.buttonFamilyRegister);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HealthFamilyRegister.ThreadB b = new HealthFamilyRegister.ThreadB ();
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
        bussiness.guardarPerfilBasico(basicProfile);
        bussiness.guardarPerfilMedico(crearPerfilMedico());
        bussiness.guardarPerfilXEPS(crearPerfilXEPS());
        bussiness.guardarPerfilXPrepagada(crearPerfilXPrepagada());
        bussiness.guardarFamiliar(emailActual,basicProfile.getEmail(),relationship);
        Toast.makeText(HealthFamilyRegister.this, "Usuario creado", Toast.LENGTH_LONG).show();
        Intent intent = new Intent ( getApplicationContext() , MainActivity.class );
        startActivityForResult ( intent , 0 );
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
    private PerfilMedico crearPerfilMedico(){
        PerfilMedico user = new PerfilMedico(basicProfile.getEmail(),bloodType,disease,environmentAllergy,medicinesAllergy,medicine);
        return user;
    }
    private PerfilXEPS crearPerfilXEPS(){
        PerfilXEPS user = new PerfilXEPS(basicProfile.getEmail(),nombreEPS,regimenEPS,planC);
        return user;
    }
    private PerfilxPrepagada crearPerfilXPrepagada(){
        PerfilxPrepagada user = new PerfilxPrepagada(basicProfile.getEmail(),nombrePrepagada);
        return user;
    }
}
