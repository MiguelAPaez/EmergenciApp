package com.example.miguelapaez.emergenciapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    TextView message;
    private EditText name, lastName, idType, id, age, email, phone, gender, eps, afiliacion, complementaryPlan, prepaidMedicine, rh, disease,
            ambientalAllergy, medicineAllergy, medicine;
    private String nameAux, lastNameAux, typeIdAux, idAux, emailAux, phoneAux, genderAux;
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

        message = (TextView) findViewById ( R.id.textMessageProfile );
        String font_path = "font/Arvo-Regular.ttf";
        Typeface TF = Typeface.createFromAsset ( getAssets (), font_path );
        message.setTypeface ( TF );

        final TextView txtSub = (TextView)findViewById(R.id.meessageSignUpProfile);
        txtSub.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent ( v.getContext(), Register.class);
                intent.putExtra ( "activity" , "profile" );
                intent.putExtra ( "name" , nameAux );
                intent.putExtra ( "lastName" , lastNameAux );
                intent.putExtra ( "id" , idAux );
                intent.putExtra ( "email" , emailAux );
                intent.putExtra ( "phone" , phoneAux );
                intent.putExtra ( "gender" , genderAux );
                startActivityForResult(intent, 0);
            }
        });

        //TextViews
        name = (EditText) findViewById(R.id.nameProfile);
        lastName = (EditText) findViewById(R.id.lastNameProfile);
        idType = (EditText) findViewById(R.id.idTypeProfile);
        id = (EditText) findViewById(R.id.idProfile);
        age = (EditText) findViewById(R.id.ageProfile);
        email = (EditText) findViewById(R.id.emailProfile);
        phone = (EditText) findViewById(R.id.phoneProfile);
        gender = (EditText) findViewById(R.id.genderProfile);
        eps = (EditText) findViewById(R.id.epsProfile);
        afiliacion = (EditText) findViewById(R.id.affiliationProfile);
        complementaryPlan = (EditText) findViewById(R.id.complementaryPlanProfile);
        prepaidMedicine = (EditText) findViewById(R.id.prepaidMedicineProfile);
        rh = (EditText) findViewById(R.id.rhProfile);
        disease = (EditText) findViewById(R.id.diseaseProfile);
        ambientalAllergy = (EditText) findViewById(R.id.environmentAllergyProfile);
        medicineAllergy = (EditText) findViewById(R.id.medicineAllergyProfile);
        medicine = (EditText) findViewById(R.id.medicineProfile);
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
        nameAux = user.getName();
        lastName.setText(user.getLastName());
        lastNameAux = user.getLastName();
        idType.setText(user.getTypeId());
        id.setText(user.getId());
        idAux = user.getId();
        age.setText(user.getAge());
        email.setText(user.getEmail());
        emailAux = user.getEmail();
        phone.setText(user.getPhone());
        phoneAux = user.getPhone();
        gender.setText(user.getGender());
        genderAux = user.getGender();
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
