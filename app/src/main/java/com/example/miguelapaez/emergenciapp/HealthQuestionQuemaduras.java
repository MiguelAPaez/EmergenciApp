package com.example.miguelapaez.emergenciapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HealthQuestionQuemaduras extends AppCompatActivity {

    private String latUser;
    private String lonUser;
    GridLayout mainGrid;
    String email;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_question_quemaduras);

        getSupportActionBar ().hide ();
        email = getIntent ().getStringExtra ( "email" );
        latUser = getIntent().getStringExtra("latitud");
        lonUser = getIntent().getStringExtra("longitud");
        mainGrid = (GridLayout) findViewById ( R.id.gridLayoutQuestionQuemaduras);
        setSingleEvent(mainGrid);
    }



    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            LinearLayout cardView = (LinearLayout) mainGrid.getChildAt( i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalI == 0){
                        Intent intent = new Intent ( HealthQuestionQuemaduras.this, MedicalCenters.class);
                        //intent.putExtra("info","This is activity from card item index  "+finalI);
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("cirugía plástica");
                        especialidades.add("oftalmología");
                        intent.putStringArrayListExtra("especialidades",especialidades);
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 1){
                        Intent intent = new Intent ( HealthQuestionQuemaduras.this, MedicalCenters.class);
                        //intent.putExtra("info","This is activity from card item index  "+finalI);
                        // intent.putExtra("answer1","Cardiologo/Neumologo/Gastroenterologo");
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("cirugía plástica");

                        intent.putStringArrayListExtra("especialidades",especialidades);
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 2){
                        Intent intent = new Intent ( HealthQuestionQuemaduras.this, MedicalCenters.class);
                        //intent.putExtra("info","This is activity from card item index  "+finalI);
                        // intent.putExtra("answer1","Cardiologo/Neumologo/Gastroenterologo");
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("cirugía plástica");

                        intent.putStringArrayListExtra("especialidades",especialidades);
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 3){
                        Intent intent = new Intent ( HealthQuestionQuemaduras.this, MedicalCenters.class);
                        //intent.putExtra("info","This is activity from card item index  "+finalI);
                        // intent.putExtra("answer1","Cardiologo/Neumologo/Gastroenterologo");
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("cirugía plástica");

                        intent.putStringArrayListExtra("especialidades",especialidades);
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 4){
                        Intent intent = new Intent ( HealthQuestionQuemaduras.this, MedicalCenters.class);
                        //intent.putExtra("info","This is activity from card item index  "+finalI);
                        // intent.putExtra("answer1","Cardiologo/Neumologo/Gastroenterologo");
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("cirugía plástica");

                        intent.putStringArrayListExtra("especialidades",especialidades);
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
