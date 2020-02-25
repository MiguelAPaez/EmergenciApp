package com.example.miguelapaez.emergenciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    Button bDate;
    EditText eDate;
    private int ano, mes, dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_register );
        getSupportActionBar().hide();

        bDate = (Button) findViewById ( R.id.buttonDateRegister );
        eDate = (EditText) findViewById ( R.id.dateRegister );

        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance ();
                ano = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker var1, int var2, int var3, int var4) {
                        eDate.setText(var4 + "/" + (var3+1) + "/" + var2);
                    }
                }
                        ,dia,mes,ano);
                datePickerDialog.show();
            }
        });

    }
}
