package com.example.miguelapaez.emergenciapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HealthQuestionsPelvis extends AppCompatActivity {
    GridLayout mainGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_questions_pelvis);
        getSupportActionBar().hide();

        mainGrid = (GridLayout) findViewById(R.id.gridLayoutQuestion1);

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
                    Toast.makeText ( view.getContext (), "Seleccionaste a: "
                            +finalI, Toast.LENGTH_SHORT).show ();
                    Intent intent = new Intent ( HealthQuestionsPelvis.this, MedicalCenters.class);
                    intent.putExtra("info","This is activity from card item index  "+finalI);
                    startActivity(intent);

                }
            });
        }
    }
}
