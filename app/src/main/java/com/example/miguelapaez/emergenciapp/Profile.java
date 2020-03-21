package com.example.miguelapaez.emergenciapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Entities.PerfilMedico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilXEPS;
import com.example.miguelapaez.emergenciapp.Entities.PerfilxPrepagada;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilMedicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilXEPSPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilXPrepagadaPersistence;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private TextView name, lastName, idType, id, age, email, phone, gender, eps, afiliacion, complementaryPlan, prepaidMedicine, rh, disease,
            ambientalAllergy, medicineAllergy, medicine;
    private ImageView profilePhoto;
    DatabaseReference mDatabaseBasic, mDatabaseMedical, mDatabaseEPS, mDatabasePrepagada;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        //TextViews
        name = (TextView) findViewById(R.id.editNameProfile);
        lastName = (TextView) findViewById(R.id.editLastNameProfile);
        idType = (TextView) findViewById(R.id.editIdTypeProfile);
        id = (TextView) findViewById(R.id.editIdProfile);
        age = (TextView) findViewById(R.id.editAgeProfile);
        email = (TextView) findViewById(R.id.editEmailProfile);
        phone = (TextView) findViewById(R.id.editPhoneProfile);
        gender = (TextView) findViewById(R.id.editGenderProfile);
        eps = (TextView) findViewById(R.id.editEpsProfile);
        afiliacion = (TextView) findViewById(R.id.editAffiliationProfile);
        complementaryPlan = (TextView) findViewById(R.id.editComplementaryPlanProfile);
        prepaidMedicine = (TextView) findViewById(R.id.editPrepaidMedicineProfile);
        rh = (TextView) findViewById(R.id.editRhProfile);
        disease = (TextView) findViewById(R.id.editDiseaseProfile);
        ambientalAllergy = (TextView) findViewById(R.id.editEnvironmentAllergyProfile);
        medicineAllergy = (TextView) findViewById(R.id.editMedicineAllergyProfile);
        medicine = (TextView) findViewById(R.id.editMedicineProfile);
        profilePhoto = (ImageView) findViewById(R.id.profilePhoto);
        //Firebase References
        mDatabaseBasic = FirebaseDatabase.getInstance().getReference().child("Perfiles basicos");
        mDatabaseMedical = FirebaseDatabase.getInstance().getReference().child("Perfiles Medicos");
        mDatabaseEPS = FirebaseDatabase.getInstance().getReference().child("PerfilesXEPS");
        mDatabasePrepagada = FirebaseDatabase.getInstance().getReference().child("PerfilesXPrepagada");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabaseBasic.orderByChild("email").equalTo(currentUser.getEmail());
        mDatabaseMedical.orderByChild("email").equalTo(currentUser.getEmail());
        mDatabaseEPS.orderByChild("emailPerfil").equalTo(currentUser.getEmail());
        mDatabasePrepagada.orderByChild("emailPerfil").equalTo(currentUser.getEmail());
        processDialog = new ProgressDialog(this);
        cargarPerfilBasico();
    }

    private void cargarPerfilBasico() {
        processDialog.setMessage("Cargando perfil");
        processDialog.show();
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilBasicoPersistence user = new PerfilBasicoPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                    }
                    llenarDatosBasicos(user);
                    cargarPerfilMedico();
                    cargarPerfilXEPS();
                    cargarPerfilXPrepagada();
                }
                processDialog.dismiss();
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
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        gender.setText(user.getGender());
        if (user.getGender().equals("Masculino")) {
            profilePhoto.setImageResource(R.drawable.hombre);
        } else if (user.getGender().equals("Femenino")) {
            profilePhoto.setImageResource(R.drawable.mujer);
        }
    }

    private void cargarPerfilMedico() {
        mDatabaseMedical.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilMedicoPersistence user = new PerfilMedicoPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilMedicoPersistence.class);
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
        if (user.isPlanComplementario()) {
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
