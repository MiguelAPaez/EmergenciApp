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

public class HealthQuestion extends AppCompatActivity {
    private String latUser;
    private String lonUser;
    GridLayout mainGrid;
    String email;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_health_question );
        getSupportActionBar ().hide ();
        email = getIntent ().getStringExtra ( "email" );
        latUser = getIntent().getStringExtra("latitud");
        lonUser = getIntent().getStringExtra("longitud");
        mainGrid = (GridLayout) findViewById ( R.id.gridLayoutQuestion1 );

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
                        Intent intent = new Intent ( HealthQuestion.this, HealthQuestionsFace.class);
                        //intent.putExtra("info","This is activity from card item index  "+finalI);
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 1){
                        Intent intent = new Intent ( HealthQuestion.this, MedicalCenters.class);
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("cardiología");
                        especialidades.add("neumología");
                        especialidades.add("gastroenterología");
                        intent.putStringArrayListExtra("especialidades",especialidades);
                        //intent.putExtra("info","This is activity from card item index  "+finalI);
                       // intent.putExtra("answer1","Cardiologo/Neumologo/Gastroenterologo");
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 2){
                        Intent intent = new Intent ( HealthQuestion.this, HealthQuestionsPelvis.class);
                        intent.putExtra("info","This is activity from card item index  "+finalI);
                        intent.putExtra("answer1","Nefrologo/Gastroenterologo/Urologo");
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    if(finalI == 3){
                        Intent intent = new Intent ( HealthQuestion.this, MedicalCenters.class);
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("neurología");
                        especialidades.add("cardiología");
                        intent.putStringArrayListExtra("especialidades",especialidades);
                        //intent.putExtra("info","This is activity from card item index  "+finalI);
                       //intent.putExtra("answer1","Ortopedista/Neurologo/Cardiologo");
                        intent.putExtra("latitud",latUser);
                        intent.putExtra("longitud",lonUser);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }

                    if(finalI == 4){
                        Intent intent = new Intent ( HealthQuestion.this, MedicalCenters.class);
                        ArrayList<String> especialidades = new ArrayList<>();
                        especialidades.add("ortopedia");
                        especialidades.add("neurología");
                        intent.putStringArrayListExtra("especialidades",especialidades);
                        //intent.putExtra("info","This is activity from card item index  "+finalI);
                        //intent.putExtra("answer1","Ortopedista/Neurologo/Cardiologo");
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
