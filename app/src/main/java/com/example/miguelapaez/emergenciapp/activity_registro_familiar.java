package com.example.miguelapaez.emergenciapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class activity_registro_familiar extends AppCompatActivity {

    Button bDate;
    EditText eDate;
    private int ano, mes, dia;
    private int anoaux, mesaux, diaaux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_familiar);
        getSupportActionBar().hide();

        bDate = (Button) findViewById( R.id.buttonDateRegisterFamiliar);
        eDate = (EditText) findViewById(R.id.dateRegisterFamiliar);

        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                ano = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog( activity_registro_familiar.this, new DatePickerDialog.OnDateSetListener() {
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


    }
}
