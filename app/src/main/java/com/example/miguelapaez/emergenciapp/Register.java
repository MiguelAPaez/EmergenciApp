package com.example.miguelapaez.emergenciapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class Register extends AppCompatActivity {
    String eAge;
    Button bDate;
    EditText eDate, eName, eLastName, eID, eEmail, ePassword, ePhone;
    Spinner spinTypeId, spinGender;
    private int ano, mes, dia;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private FacadeNegocio bussiness;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        // Objetos de negocio
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        bussiness = new ImplementacionNegocio();

        eName = (EditText) findViewById(R.id.nameRegister);
        eLastName = (EditText) findViewById(R.id.lastNameRegister);
        spinTypeId = (Spinner) findViewById(R.id.idTypeRegister);
        eID = (EditText) findViewById(R.id.idRegister);
        bDate = (Button) findViewById(R.id.buttonDateRegister);
        eDate = (EditText) findViewById(R.id.dateRegister);
        eEmail = (EditText) findViewById(R.id.emailRegister);
        ePassword = (EditText) findViewById(R.id.passwordRegister);
        ePhone = (EditText) findViewById(R.id.phoneRegister);
        spinGender = (Spinner) findViewById(R.id.genderRegister);

        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                ano = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker var1, int var2, int var3, int var4) {
                        eDate.setText(var4 + "/" + (var3 + 1) + "/" + var2);
                    }
                }
                        , dia, mes, ano);
                datePickerDialog.show();
                eAge= bussiness.getAge(ano,mes,dia);
            }
        });

        getSupportActionBar().hide();

        Button btn = (Button) findViewById(R.id.buttonNextRegister);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
                /*

                Intent intent = new Intent ( v.getContext(), HealthRegister.class);
                startActivityForResult(intent, 0);*/
            }
        });

    }

    private void registrarUsuario() {
        progressDialog.setMessage("Realizando registro en linea...");
        progressDialog.show();
        Perfil user = crearUsuario();
        String response = bussiness.registrarUsuario(user,firebaseAuth,mDatabase);
        progressDialog.dismiss();
        Toast.makeText(Register.this,response,Toast.LENGTH_LONG).show();
    }

    private Perfil crearUsuario(){
        String name = eName.getText().toString().trim();
        String lastName = eLastName.getText().toString().trim();
        String typeId = spinTypeId.getSelectedItem().toString().trim();
        String id = eID.getText().toString().trim();
        String age = eAge;
        String email = eEmail.getText().toString().trim();
        String password = ePassword.getText().toString().trim();
        String phone = ePhone.getText().toString().trim();
        String gender = spinGender.getSelectedItem().toString().trim();
        Perfil user = new Perfil(name,lastName,typeId,id,age,email,password,phone,gender);
        return user;
    }
}
