package com.example.miguelapaez.emergenciapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HealthQuestionsFace extends AppCompatActivity {

    private String latUser;
    private String lonUser;
    private String email;
    //private String answer1;

    GridLayout mainGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_questions_face);
        getSupportActionBar().hide();

        latUser = getIntent().getStringExtra("latitud");
        lonUser = getIntent().getStringExtra("longitud");
        email = getIntent().getStringExtra("email");

        mainGrid = (GridLayout) findViewById(R.id.gridLayoutQuestionFace);

        setSingleEvent(mainGrid);
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            LinearLayout cardView = (LinearLayout) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalI == 0){
                        ArrayList<String> especialidades = new ArrayList<>();

                        especialidades.add("neurología");
                        especialidades.add("oftalmología");
                        especialidades.add("otorrinolaringología");
                        Intent intent = new Intent ( HealthQuestionsFace.this, MedicalCenters.class);
                        //intent.putExtra("info","This is activity from card item index  "+finalI);
                        //intent.putExtra("answer1","");
                        intent.putStringArrayListExtra("especialidades",especialidades);
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 1){
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("neurología");
                        especialidades.add("otorrinolaringología");
                        Intent intent = new Intent ( HealthQuestionsFace.this, MedicalCenters.class);
                        intent.putStringArrayListExtra("especialidades",especialidades);
                        intent.putExtra("info","This is activity from card item index  "+finalI);
                        //intent.putExtra("answer1",answer1+"/Nariz");
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 2){

                        Intent intent = new Intent ( HealthQuestionsFace.this, MedicalCenters.class);
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("neurología");
                        especialidades.add("otorrinolaringología");
                        intent.putStringArrayListExtra("especialidades",especialidades);
                        intent.putExtra("info","This is activity from card item index  "+finalI);
                        //intent.putExtra("answer1",answer1+"/Oidos");
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 3){
                        Intent intent = new Intent ( HealthQuestionsFace.this, MedicalCenters.class);
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("neurología");
                        especialidades.add("otorrinolaringología");
                        intent.putStringArrayListExtra("especialidades",especialidades);
                        intent.putExtra("info","This is activity from card item index  "+finalI);
                        //intent.putExtra("answer1",answer1+"/Boca");
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 4){
                        Intent intent = new Intent ( HealthQuestionsFace.this, MedicalCenters.class);
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("neurología");
                        especialidades.add("oftalmología");
                        intent.putStringArrayListExtra("especialidades",especialidades);
                        intent.putExtra("info","This is activity from card item index  "+finalI);
                        //intent.putExtra("answer1",answer1+"/Ojos");
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 5){
                        Intent intent = new Intent ( HealthQuestionsFace.this, MedicalCenters.class);
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("neumología");
                        especialidades.add("otorrinolaringología");
                        especialidades.add("cardiología");
                        especialidades.add("gastroenterología");
                        intent.putStringArrayListExtra("especialidades",especialidades);
                        intent.putExtra("info","This is activity from card item index  "+finalI);
                        //intent.putExtra("answer1",answer1+"/Garganta");
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }

                }
            });
        }
    }

}
