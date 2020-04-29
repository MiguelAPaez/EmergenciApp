package com.example.miguelapaez.emergenciapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.example.miguelapaez.emergenciapp.Validaciones.Validaciones;

import java.util.Calendar;
import java.util.Random;

public class activity_registro_familiar extends AppCompatActivity {

    Button bDate;
    EditText eDate;
    private int ano, mes, dia;
    private int anoaux, mesaux, diaaux;
    String eAge;
    EditText eName, eLastName, eID, ePhone;
    ScrollView scrollView;
    LinearLayout nameL, lastNameL, idL, typeIdL, dateL, phoneL, genderL, relationshipL;
    Spinner spinTypeId, spinGender, spinRelationship;
    FacadeNegocio bussiness = new ImplementacionNegocio();
    String name, lastName, typeId, id, age, phone, gender, relationship, emailActual;
    String redColor = "#40FF0000";
    Validaciones objValidar;
    boolean validacionOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_familiar);
        getSupportActionBar().hide();

        //ScrollView Register
        scrollView = (ScrollView) findViewById(R.id.scrollViewFamilyRegister);

        //LinearLayout de Register
        nameL = (LinearLayout) findViewById(R.id.linearLayoutNameFamilyRegister);
        lastNameL = (LinearLayout) findViewById(R.id.linearLayoutLastNameFamilyRegister);
        typeIdL = (LinearLayout) findViewById(R.id.linearLayoutIdTypeFamilyRegister);
        idL = (LinearLayout) findViewById(R.id.linearLayoutIdFamilyRegister);
        dateL = (LinearLayout) findViewById(R.id.linearLayoutDateFamilyRegister);
        phoneL = (LinearLayout) findViewById(R.id.linearLayoutPhoneFamilyRegister);
        genderL = (LinearLayout) findViewById(R.id.linearLayoutGenderFamilyRegister);
        relationshipL = (LinearLayout) findViewById(R.id.linearLayoutRelationshipFamilyRegister);

        //EditText de Register
        eName = (EditText) findViewById(R.id.nameFamilyRegister);
        eLastName = (EditText) findViewById(R.id.lastNameFamilyRegister);
        spinTypeId = (Spinner) findViewById(R.id.idTypeFamilyRegister);
        eID = (EditText) findViewById(R.id.idFamilyRegister);
        bDate = (Button) findViewById(R.id.buttonDateFamilyRegister);
        eDate = (EditText) findViewById(R.id.dateFamilyRegister);
        ePhone = (EditText) findViewById(R.id.phoneFamilyRegister);
        spinGender = (Spinner) findViewById(R.id.genderFamilyRegister);
        spinRelationship = (Spinner) findViewById(R.id.relationshipFamilyRegister);

        //Validaciones
        emailActual = getIntent().getStringExtra("emailActual");
        objValidar = new Validaciones();

        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                ano = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity_registro_familiar.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker var1, int var2, int var3, int var4) {
                        eDate.setText(var4 + "/" + (var3 + 1) + "/" + var2);
                        diaaux = var4;
                        Log.i("DIA", String.valueOf(diaaux));
                        mesaux = var3 + 1;
                        Log.i("MES", String.valueOf(mesaux));
                        anoaux = var2;
                        Log.i("ANO", String.valueOf(anoaux));
                    }
                }
                        , dia, mes, ano);
                datePickerDialog.show();
            }
        });

        LinearLayout btn = (LinearLayout) findViewById(R.id.linearLayoutPerfilMedicoFamiliar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadB b = new ThreadB();
                b.start();

                synchronized (b) {

                    try {
                        System.out.println("Waiting for b to complete...");
                        b.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (validacionOK) {
                        llenarDatos();
                        PerfilBasico basicProfile = crearPerfilBasico();
                        Intent intent = new Intent(v.getContext(), HealthFamilyRegister.class);
                        intent.putExtra("basicProfile", basicProfile);
                        intent.putExtra("emailActual", emailActual);
                        intent.putExtra("relationship", relationship);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void llenarDatos() {
        name = eName.getText().toString().trim();
        lastName = eLastName.getText().toString().trim();
        typeId = spinTypeId.getSelectedItem().toString().trim();
        id = eID.getText().toString().trim();
        eAge = bussiness.getAge(anoaux, mesaux, diaaux);
        age = eAge;
        phone = ePhone.getText().toString().trim();
        gender = spinGender.getSelectedItem().toString().trim();
        relationship = spinRelationship.getSelectedItem().toString().trim();
    }

    class ThreadB extends Thread {
        int total;

        @Override
        public void run() {
            synchronized (this) {
                validacionOK = validarDatos();
                notify();
            }
        }
    }

    public boolean validarDatos() {

        //Validación Nombres
        if (objValidar.Vacio(eName)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su Nombre", Toast.LENGTH_SHORT).show();
                    eName.setError("Campo Requerido");
                    eName.requestFocus();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        }

        //Validación Apellidos
        if (objValidar.Vacio(eLastName)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese sus Apellidos", Toast.LENGTH_SHORT).show();
                    eLastName.setError("Campo Requerido");
                    eLastName.requestFocus();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        }

        //Validación Tipo de Identificación
        String idType = spinTypeId.getSelectedItem().toString().trim();
        if (objValidar.isEquals(idType, "Tipo de Identificación")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su Tipo de Identificación", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    typeIdL.setBackgroundColor(Color.parseColor(redColor));
                }
            });
            return false;
        }

        //Validación Número de Identificación
        if (objValidar.Vacio(eID)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su Número de Identificación", Toast.LENGTH_SHORT).show();
                    eID.setError("Campo Requerido");
                    eID.requestFocus();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        }

        //Validación Fecha de Nacimiento
        if (objValidar.Vacio(eDate)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su Fecha de Nacimiento", Toast.LENGTH_SHORT).show();
                    eDate.setError("Campo Requerido");
                    eDate.requestFocus();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        }

        //Validación Celular
        if (objValidar.Vacio(ePhone)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su Celular", Toast.LENGTH_SHORT).show();
                    ePhone.setError("Campo Requerido");
                    ePhone.requestFocus();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        }

        //Validación Género
        String gender = spinGender.getSelectedItem().toString().trim();
        if (objValidar.isEquals(gender, "Género")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su Género", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    genderL.setBackgroundColor(Color.parseColor(redColor));
                }
            });
            return false;
        }

        //Validación Parentesco
        String relationship = spinRelationship.getSelectedItem().toString().trim();
        if (objValidar.isEquals(relationship, "Parentesco")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese el Parentesco con su Familiar", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    relationshipL.setBackgroundColor(Color.parseColor(redColor));
                }
            });
            return false;
        }
        return true;
    }

    private PerfilBasico crearPerfilBasico() {
        int random = new Random().nextInt(9999);
        String email = "tmp" + random + "@gmail.com";
        PerfilBasico user = new PerfilBasico(email, name, lastName, typeId, id, age, phone, gender);
        return user;
    }
}
