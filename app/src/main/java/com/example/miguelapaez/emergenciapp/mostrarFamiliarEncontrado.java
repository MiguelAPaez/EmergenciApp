package com.example.miguelapaez.emergenciapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Entities.Solicitud;
import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;

public class mostrarFamiliarEncontrado extends AppCompatActivity {
    PerfilBasicoPersistence basicProfile;
    TextView nombre;
    ImageView profilePhoto;
    String parentesco;
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
        final String email = getIntent().getStringExtra("email");
        //You Know
        nombre = (TextView) findViewById(R.id.nombreFamiliarEncontrado);
        profilePhoto = (ImageView) findViewById(R.id.imagePerfilFamiliarEncontrado);
        eParentesco = (Spinner) findViewById(R.id.parentescoFamiliarEncontrado);
        //Cargar Datos
        String name = basicProfile.getName() + " " + basicProfile.getLastName();
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
                parentesco = eParentesco.getSelectedItem().toString();
                if (!parentesco.equals("Parentesco")) {
                    String rol = bussiness.getRol(parentesco);
                    Solicitud solicitud = new Solicitud(email,basicProfile.getEmail(),rol,parentesco,"Pendiente");
                    bussiness.crearSolicitud(solicitud);
                    Toast.makeText(getApplicationContext(),"Solicitud enviada",Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Defina un parentesco", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
