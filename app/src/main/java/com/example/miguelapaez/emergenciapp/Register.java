package com.example.miguelapaez.emergenciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

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
                eAge= getAge(ano,mes,dia);
            }
        });

        getSupportActionBar().hide();

        Button btn = (Button) findViewById(R.id.buttonNextRegister);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
                ;
                /*Intent intent = new Intent ( v.getContext(), HealthRegister.class);
                startActivityForResult(intent, 0);*/
            }
        });

    }

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private void registrarUsuario() {
        String email = eEmail.getText().toString().trim();
        String password = ePassword.getText().toString().trim();
        progressDialog.setMessage("Realizando registro...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            crearUsuarioBD();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(Register.this, "El usuario ya existe", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(Register.this, "No se pudo registrar al usuario correctamente", Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }
    private void crearUsuarioBD(){
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
        mDatabase.child("Perfiles").child(id).setValue(user);
        Toast.makeText(Register.this,"Registro Exitoso",Toast.LENGTH_LONG).show();
    }
}
