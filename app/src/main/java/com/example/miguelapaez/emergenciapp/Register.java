package com.example.miguelapaez.emergenciapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;

import java.util.Calendar;


public class Register extends AppCompatActivity {
    String eAge;
    Button bDate;
    EditText eDate, eName, eLastName, eID, eEmail, ePassword, ePhone;
    Spinner spinTypeId, spinGender;
    private int ano, mes, dia;
    private int anoaux, mesaux, diaaux;
    FacadeNegocio bussiness = new ImplementacionNegocio();
    String name, lastName, typeId, id, age, email, password,phone, gender;

    @Override
    public void onResume() {
        super.onResume();
        if (bussiness.verificarSesion()) {
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (bussiness.verificarSesion()){
            startActivity(new Intent(Register.this, MainActivity.class));
        }
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

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
                        diaaux = var4;
                        Log.i( "DIA", String.valueOf ( diaaux ) );
                        mesaux = var3+1;
                        Log.i( "MES", String.valueOf ( mesaux ) );
                        anoaux = var2;
                        Log.i( "ANO", String.valueOf ( anoaux ) );
                    }
                }
                        , dia, mes, ano);
                datePickerDialog.show();
            }
        });

        getSupportActionBar().hide();

        LinearLayout btn = (LinearLayout) findViewById(R.id.buttonNextRegister);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenarDatos();
                Intent intent = new Intent ( v.getContext(), HealthRegister.class);
                intent.putExtra ( "name", name );
                intent.putExtra ( "lastName", lastName );
                intent.putExtra ( "typeId", typeId );
                intent.putExtra ( "id", id );
                intent.putExtra ( "age", age );
                intent.putExtra ( "email", email );
                intent.putExtra ( "password", password );
                intent.putExtra ( "phone", phone );
                intent.putExtra ( "gender", gender );
                Toast.makeText(Register.this, "POPO!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        /*
        Button btn = (Button) findViewById(R.id.buttonNextRegister);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenarDatos();
                Intent intent = new Intent ( v.getContext(), HealthRegister.class);
                intent.putExtra ( "name", name );
                intent.putExtra ( "lastName", lastName );
                intent.putExtra ( "typeId", typeId );
                intent.putExtra ( "id", id );
                intent.putExtra ( "age", age );
                intent.putExtra ( "email", email );
                intent.putExtra ( "password", password );
                intent.putExtra ( "phone", phone );
                intent.putExtra ( "gender", gender );
                startActivity(intent);
            }
        });
        */


    }


    private void llenarDatos(){

        name = eName.getText().toString().trim();
        lastName = eLastName.getText().toString().trim();
        typeId = spinTypeId.getSelectedItem().toString().trim();
        id = eID.getText().toString().trim();
        eAge= bussiness.getAge(anoaux,mesaux,diaaux);
        age = eAge;
        email = eEmail.getText().toString().trim();
        password = ePassword.getText().toString().trim();
        phone = ePhone.getText().toString().trim();
        Log.i( "CEL", String.valueOf ( phone ) );
        gender = spinGender.getSelectedItem().toString().trim();
    }
}
