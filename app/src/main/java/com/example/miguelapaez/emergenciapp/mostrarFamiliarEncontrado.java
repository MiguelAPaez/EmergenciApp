package com.example.miguelapaez.emergenciapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;

public class mostrarFamiliarEncontrado extends AppCompatActivity {
    PerfilBasicoPersistence basicProfile;
    TextView nombre;
    ImageView profilePhoto;
    String id, parentesco;
    LinearLayout btnAddGroup;
    Spinner eParentesco;
    FacadeNegocio bussiness = new ImplementacionNegocio();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mostrar_familiar_encontrado);
        //Recepción de datos Activity Register
        basicProfile = (PerfilBasicoPersistence) getIntent().getSerializableExtra("basicProfile");
        id = getIntent().getStringExtra("id");
        //You Know
        nombre = (TextView) findViewById(R.id.nombreFamiliarEncontrado);
        profilePhoto = (ImageView) findViewById(R.id.imagePerfilFamiliarEncontrado);
        eParentesco = (Spinner) findViewById(R.id.parentescoFamiliarEncontrado);
        //Cargar Datos
        String name = basicProfile.getName() + " " + basicProfile.getLastName();
        parentesco = eParentesco.getSelectedItem().toString();
        nombre.setText(name);
        if (basicProfile.getGender().equals("Masculino")) {
            profilePhoto.setImageResource(R.drawable.hombre);
        } else if (basicProfile.getGender().equals("Femenino")) {
            profilePhoto.setImageResource(R.drawable.mujer);
        }

        btnAddGroup = (LinearLayout) findViewById(R.id.buttonAddGroup);
        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
