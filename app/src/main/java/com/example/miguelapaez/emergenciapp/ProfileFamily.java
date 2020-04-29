package com.example.miguelapaez.emergenciapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilMedicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilXEPSPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilXPrepagadaPersistence;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFamily extends AppCompatActivity {

    private EditText name, lastName, idType, id, age, phone, gender, eps, afiliacion, complementaryPlan, prepaidMedicine, rh, disease,
            ambientalAllergy, medicineAllergy, medicine;
    private ImageView profilePhoto;
    private TextView title;
    ScrollView scrollView;
    String email,rol;
    DatabaseReference mDatabaseBasic, mDatabaseMedical, mDatabaseEPS, mDatabasePrepagada;

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

        //Recibe datos
        email = getIntent().getStringExtra("email");
        rol = getIntent().getStringExtra("rol");

        //Firebase
        mDatabaseBasic = FirebaseDatabase.getInstance().getReference().child("Perfiles Basicos");
        mDatabaseMedical = FirebaseDatabase.getInstance().getReference().child("Perfiles Medicos");
        mDatabaseEPS = FirebaseDatabase.getInstance().getReference().child("PerfilesXEPS");
        mDatabasePrepagada = FirebaseDatabase.getInstance().getReference().child("PerfilesXPrepagada");
        mDatabaseBasic.orderByChild("email").equalTo(email);
        mDatabaseMedical.orderByChild("email").equalTo(email);
        mDatabaseEPS.orderByChild("emailPerfil").equalTo(email);
        mDatabasePrepagada.orderByChild("emailPerfil").equalTo(email);

        cargarPerfil();

    }

    private void cargarPerfil() {
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilBasicoPersistence user = new PerfilBasicoPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(email)) {
                            break;
                        }
                    }
                    llenarDatosBasicos(user);
                    cargarPerfilMedico();
                    cargarPerfilXEPS();
                    cargarPerfilXPrepagada();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void llenarDatosBasicos(PerfilBasicoPersistence user) {
        name.setText(user.getName());
        lastName.setText(user.getLastName());
        idType.setText(user.getTypeId());
        id.setText(user.getId());
        age.setText(user.getAge());
        phone.setText(user.getPhone());
        gender.setText(user.getGender());
        if (user.getGender().equals("Masculino")) {
            profilePhoto.setImageResource(R.drawable.hombre);
        } else if (user.getGender().equals("Femenino")) {
            profilePhoto.setImageResource(R.drawable.mujer);
        }
        title.setText(rol);
    }

    private void cargarPerfilMedico() {
        mDatabaseMedical.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilMedicoPersistence user = new PerfilMedicoPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilMedicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(email)) {
                            break;
                        }
                    }
                    llenarDatosMedicos(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void llenarDatosMedicos(PerfilMedicoPersistence user) {
        rh.setText(user.getRh());
        disease.setText(user.getEnfermedadCronica());
        ambientalAllergy.setText(user.getAlergiaAmbiental());
        medicineAllergy.setText(user.getAlergiaMedicamento());
        medicine.setText(user.getMedicamento());
    }

    private void cargarPerfilXEPS() {
        mDatabaseEPS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilXEPSPersistence user = new PerfilXEPSPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilXEPSPersistence.class);
                        if (!user.getEmailPerfil().isEmpty() && user.getEmailPerfil().equals(email)) {
                            break;
                        }
                    }
                    llenarDatosEPS(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void llenarDatosEPS(PerfilXEPSPersistence user) {
        eps.setText(user.getNombreEPS());
        afiliacion.setText(user.getRegimen());
        if (!user.isPlanComplementario()) {
            complementaryPlan.setText("No aplica");
        } else {
            complementaryPlan.setText("Si aplica");
        }
    }

    private void cargarPerfilXPrepagada() {
        mDatabasePrepagada.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilXPrepagadaPersistence user = new PerfilXPrepagadaPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilXPrepagadaPersistence.class);
                        if (!user.getEmailPerfil().isEmpty() && user.getEmailPerfil().equals(email)) {
                            break;
                        }
                    }
                    llenarDatosPrepagada(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void llenarDatosPrepagada(PerfilXPrepagadaPersistence user) {
        prepaidMedicine.setText(user.getNombrePrepada());
    }



}
