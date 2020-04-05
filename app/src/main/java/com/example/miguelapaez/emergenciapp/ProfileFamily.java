package com.example.miguelapaez.emergenciapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileFamily extends AppCompatActivity {

    private EditText name, lastName, idType, id, age, phone, gender, eps, afiliacion, complementaryPlan, prepaidMedicine, rh, disease,
            ambientalAllergy, medicineAllergy, medicine;
    private ImageView profilePhoto;
    private TextView title;
    ScrollView scrollView;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_profile_family );
        getSupportActionBar().hide();

        //TextViews
        name = (EditText) findViewById( R.id.nameProfileFamily);
        lastName = (EditText) findViewById(R.id.lastNameProfileFamily);
        idType = (EditText) findViewById(R.id.idTypeProfileFamily);
        id = (EditText) findViewById(R.id.idProfileFamily);
        age = (EditText) findViewById(R.id.ageProfileFamily);
        phone = (EditText) findViewById(R.id.phoneProfileFamily);
        gender = (EditText) findViewById(R.id.genderProfileFamily);
        eps = (EditText) findViewById(R.id.epsProfileFamily);
        afiliacion = (EditText) findViewById(R.id.affiliationProfileFamily);
        complementaryPlan = (EditText) findViewById(R.id.complementaryPlanProfileFamily);
        prepaidMedicine = (EditText) findViewById(R.id.prepaidMedicineProfileFamily);
        rh = (EditText) findViewById(R.id.rhProfileFamily);
        disease = (EditText) findViewById(R.id.diseaseProfileFamily);
        ambientalAllergy = (EditText) findViewById(R.id.environmentAllergyProfileFamily);
        medicineAllergy = (EditText) findViewById(R.id.medicineAllergyProfileFamily);
        medicine = (EditText) findViewById(R.id.medicineProfileFamily);
        profilePhoto = (ImageView) findViewById( R.id.profileFamilyPhoto);
        title = (TextView) findViewById( R.id.titleProfileFamilyActivity);

        //ScrollView
        scrollView = (ScrollView) findViewById( R.id.scrollViewProfile);

        // email = getIntent().getStringExtra("email");

    }



}
