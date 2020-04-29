package com.example.miguelapaez.emergenciapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class HealthQuestion extends AppCompatActivity {
    private double latitudUser = 0;
    private double longitudUser = 0;
    GridLayout mainGrid;
    private FusedLocationProviderClient mFusedLocationClient;
    String email;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_health_question );
        getSupportActionBar ().hide ();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient ( this );
        email = getIntent ().getStringExtra ( "email" );
        mainGrid = (GridLayout) findViewById ( R.id.gridLayoutQuestion1 );
        if (checkSelfPermission ( Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission ( Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation ().addOnSuccessListener (
                this , new OnSuccessListener <Location> () {
                    @Override
                    public void onSuccess(Location location) {
                        Log.i ( "LOCATION" , "onSuccess location" );
                        if (location != null) {
                            longitudUser = location.getLongitude ();
                            latitudUser = location.getLatitude ();
                        }
                    }
                } );
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
                    String latUser = String.valueOf(latitudUser);
                    String lonUser = String.valueOf(longitudUser);
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
