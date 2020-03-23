package com.example.miguelapaez.emergenciapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;

public class mostrarFamiliarEncontrado extends AppCompatActivity {
    PerfilBasicoPersistence basicProfile;
    TextView nombre;
    ImageView profilePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mostrar_familiar_encontrado);
        //Recepción de datos Activity Register
        basicProfile = (PerfilBasicoPersistence) getIntent().getSerializableExtra("basicProfile");
        //You Know
        nombre = (TextView) findViewById(R.id.nombreFamiliarEncontrado);
        profilePhoto = (ImageView) findViewById(R.id.imagePerfilFamiliarBuscado);
        //Cargar Datos
        String name = basicProfile.getName() + " " + basicProfile.getLastName();
        nombre.setText(name);
        if (basicProfile.getGender().equals("Masculino")) {
            profilePhoto.setImageResource(R.drawable.hombre);
        } else if (basicProfile.getGender().equals("Femenino")) {
            profilePhoto.setImageResource(R.drawable.mujer);
        }
    }
}
