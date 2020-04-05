package com.example.miguelapaez.emergenciapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilMedico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilXEPS;
import com.example.miguelapaez.emergenciapp.Entities.PerfilxPrepagada;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilMedicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilXEPSPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilXPrepagadaPersistence;
import com.example.miguelapaez.emergenciapp.Validaciones.Validaciones;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private EditText name, lastName, idType, id, age, email, password, actualPassword, validatePassword, phone, gender, eps, afiliacion, complementaryPlan, prepaidMedicine, rh, disease,
            ambientalAllergy, medicineAllergy, medicine;
    private ImageView profilePhoto;
    private TextView update, message, title;
    private String nameAux, lastNameAux, idTypeAux, idAux, ageAux, emailAux, passwordAux, phoneAux, genderAux, epsAux, afiliacionAux, prepaidMedicineAux, rhAux, diseaseAux, ambientalAllergyAux, medicineAllergyAux, medicineAux, actualPasswordAux;
    private boolean complementaryPlanAux = false;
    ScrollView scrollView;
    private LinearLayout passwordLinear, actualPasswordLinear, validatePasswordLinear, titleMedical, ageLinear, genderLinear, epsLinear, afiliacionLinear, complmentaryLinear, prepaidLinear, rhLinear, diseaseLinear,
            environmentLinear, medicineAllergyLinear, medicineLinear, editEpsLinear, editAfiliacionLinear, editComplementaryPlanLinear, editPrepaidMedicineLinear, editRhLinear,
            editDiseaseLinear, editambientalAllergyLinear, editMedicineAllergyLinear, editMedicineLinear;
    private Button updateButton;
    Spinner eBloodType, eDisease, eEnvironmentAllergy, eMedicinesAllergy, eMedicine;
    Spinner eEPS, eRegimenEPS;
    Spinner ePrepagada;
    RadioButton ePC;
    DatabaseReference mDatabase, mDatabaseBasic, mDatabaseMedical, mDatabaseEPS, mDatabasePrepagada;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressDialog processDialog;
    String redColor = "#40FF0000";
    Validaciones objValidar;
    boolean validacionOK;
    String emailActual;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        message = (TextView) findViewById(R.id.textMessageProfile);
        String font_path = "font/Arvo-Regular.ttf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        message.setTypeface(TF);

        final TextView txtSub = (TextView) findViewById(R.id.meessageSignUpProfile);
        txtSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPerfil();
            }
        });

        //TextViews
        name = (EditText) findViewById(R.id.nameProfile);
        lastName = (EditText) findViewById(R.id.lastNameProfile);
        idType = (EditText) findViewById(R.id.idTypeProfile);
        id = (EditText) findViewById(R.id.idProfile);
        age = (EditText) findViewById(R.id.ageProfile);
        email = (EditText) findViewById(R.id.emailProfile);
        password = (EditText) findViewById(R.id.passwordProfile);
        actualPassword = (EditText) findViewById(R.id.actualPasswordProfile);
        validatePassword = (EditText) findViewById(R.id.validatePasswordProfile);
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
        update = (TextView) findViewById(R.id.meessageSignUpProfile);
        title = (TextView) findViewById(R.id.titleProfileActivity);
        updateButton = (Button) findViewById(R.id.buttonUpdateProfile);

        //Spinner's
        //EditText de Health Register
        eBloodType = (Spinner) findViewById(R.id.editBloodTypeProfile);
        eDisease = (Spinner) findViewById(R.id.editdiseaseProfile);
        eEnvironmentAllergy = (Spinner) findViewById(R.id.editEnvironmentAllergyProfile);
        eMedicinesAllergy = (Spinner) findViewById(R.id.editMedicinesAllergyProfile);
        eMedicine = (Spinner) findViewById(R.id.editMedicineProfile);
        //EditText de EPS
        eEPS = (Spinner) findViewById(R.id.editEpsProfile);
        eRegimenEPS = (Spinner) findViewById(R.id.editEpsRegimeProfile);
        ePC = (RadioButton) findViewById(R.id.radio_complementary_plan_yes_profile);
        //EditText de Prepagada
        ePrepagada = (Spinner) findViewById(R.id.editprepaidMedicineProfile);

        //Validaciones
        objValidar = new Validaciones();

        //ScrollView
        scrollView = (ScrollView) findViewById(R.id.scrollViewProfile);

        //LinearLayout's
        passwordLinear = (LinearLayout) findViewById(R.id.linearPasswordProfile);
        validatePasswordLinear = (LinearLayout) findViewById(R.id.linearLayoutValidatePasswordProfile);
        actualPasswordLinear = (LinearLayout) findViewById(R.id.linearLayoutActualPasswordProfile);
        titleMedical = (LinearLayout) findViewById(R.id.linearLayoutTitleMedicalProfile);
        epsLinear = (LinearLayout) findViewById(R.id.linearEpsProfile);
        afiliacionLinear = (LinearLayout) findViewById(R.id.linearAffiliationProfile);
        complmentaryLinear = (LinearLayout) findViewById(R.id.linearComplementaryPlanProfile);
        prepaidLinear = (LinearLayout) findViewById(R.id.linearPrepaidMedicineProfile);
        rhLinear = (LinearLayout) findViewById(R.id.linearRhProfile);
        diseaseLinear = (LinearLayout) findViewById(R.id.linearDiseaseProfile);
        environmentLinear = (LinearLayout) findViewById(R.id.linearEnvironmentAllergyProfile);
        medicineAllergyLinear = (LinearLayout) findViewById(R.id.linearMedicineAllergyProfile);
        medicineLinear = (LinearLayout) findViewById(R.id.linearMedicineProfile);
        genderLinear = (LinearLayout) findViewById(R.id.linearGenderProfile);
        ageLinear = (LinearLayout) findViewById(R.id.linearAgeProfile);

        editEpsLinear = (LinearLayout) findViewById(R.id.linearLayouteditEPSProfile);
        editAfiliacionLinear = (LinearLayout) findViewById(R.id.linearLayouteditEPSRegimeProfile);
        editComplementaryPlanLinear = (LinearLayout) findViewById(R.id.linearLayouteditComplementaryPlanProfile);
        editPrepaidMedicineLinear = (LinearLayout) findViewById(R.id.linearLayouteditPrepaidMedicineProfile);
        editRhLinear = (LinearLayout) findViewById(R.id.linearLayouteditBloodTypeProfile);
        editDiseaseLinear = (LinearLayout) findViewById(R.id.linearLayouteditDiseaseProfile);
        editambientalAllergyLinear = (LinearLayout) findViewById(R.id.linearLayouteditEnvironmentAllergyProfile);
        editMedicineAllergyLinear = (LinearLayout) findViewById(R.id.linearLayouteditMedicinesAllergyProfile);
        editMedicineLinear = (LinearLayout) findViewById(R.id.linearLayouteditMedicineProfile);

        //Firebase References
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Perfiles");
        mDatabaseBasic = FirebaseDatabase.getInstance().getReference().child("Perfiles Basicos");
        mDatabaseMedical = FirebaseDatabase.getInstance().getReference().child("Perfiles Medicos");
        mDatabaseEPS = FirebaseDatabase.getInstance().getReference().child("PerfilesXEPS");
        mDatabasePrepagada = FirebaseDatabase.getInstance().getReference().child("PerfilesXPrepagada");
        mDatabase.orderByChild("email").equalTo(currentUser.getEmail());
        mDatabaseBasic.orderByChild("email").equalTo(currentUser.getEmail());
        mDatabaseMedical.orderByChild("email").equalTo(currentUser.getEmail());
        mDatabaseEPS.orderByChild("emailPerfil").equalTo(currentUser.getEmail());
        mDatabasePrepagada.orderByChild("emailPerfil").equalTo(currentUser.getEmail());
        processDialog = new ProgressDialog(this);
        emailActual = currentUser.getEmail();
        cargarPerfil();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile.ThreadB b = new Profile.ThreadB();
                b.start();

                synchronized (b) {

                    try {
                        System.out.println("Waiting for b to complete...");
                        b.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (validacionOK) {
                        actualizarPerfilFB();
                    }
                }
            }
        });

    }

    private void cargarPerfil() {
        processDialog.setMessage("Cargando perfil");
        processDialog.show();
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                PerfilBasicoPersistence user = new PerfilBasicoPersistence();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    user = snapshot.getValue(PerfilBasicoPersistence.class);
                                    if (!user.getEmail().isEmpty() && user.getEmail().equals(currentUser.getEmail())) {
                                        break;
                                    }
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
        idTypeAux = user.getTypeId();
        id.setText(user.getId());
        idAux = user.getId();
        age.setText(user.getAge());
        ageAux = user.getAge();
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
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(currentUser.getEmail())) {
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
                        if (!user.getEmailPerfil().isEmpty() && user.getEmailPerfil().equals(currentUser.getEmail())) {
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
                        if (!user.getEmailPerfil().isEmpty() && user.getEmailPerfil().equals(currentUser.getEmail())) {
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

    private void actualizarPerfil() {
        message.setVisibility(View.GONE);
        update.setVisibility(View.GONE);
        actualPasswordLinear.setVisibility(View.VISIBLE);
        validatePasswordLinear.setVisibility(View.VISIBLE);
        title.setText(R.string.update_profile);
        epsLinear.setVisibility(View.GONE);
        afiliacionLinear.setVisibility(View.GONE);
        complmentaryLinear.setVisibility(View.GONE);
        prepaidLinear.setVisibility(View.GONE);
        rhLinear.setVisibility(View.GONE);
        diseaseLinear.setVisibility(View.GONE);
        environmentLinear.setVisibility(View.GONE);
        medicineAllergyLinear.setVisibility(View.GONE);
        medicineLinear.setVisibility(View.GONE);
        email.setEnabled(true);
        email.setText(emailAux);
        email.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
        password.setEnabled(true);
        password.setText("");
        password.setHint(R.string.new_password);
        password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
        phone.setEnabled(true);
        // phone.setText ( phoneAux );
        phone.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
        genderLinear.setVisibility(View.GONE);
        ageLinear.setVisibility(View.GONE);
        editEpsLinear.setVisibility(View.VISIBLE);
        editAfiliacionLinear.setVisibility(View.VISIBLE);
        editComplementaryPlanLinear.setVisibility(View.VISIBLE);
        editPrepaidMedicineLinear.setVisibility(View.VISIBLE);
        editRhLinear.setVisibility(View.VISIBLE);
        editDiseaseLinear.setVisibility(View.VISIBLE);
        editambientalAllergyLinear.setVisibility(View.VISIBLE);
        editMedicineAllergyLinear.setVisibility(View.VISIBLE);
        editMedicineLinear.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.VISIBLE);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    class ThreadB extends Thread {
        int total;

        @Override
        public void run() {
            synchronized (this) {
                validacionOK = validarDatos();
                notify();
            }
        }
    }

    public boolean validarDatos() {


        //Validación Email
        String emaila = email.getText().toString().trim();
        if (objValidar.Vacio(email)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su Email", Toast.LENGTH_SHORT).show();
                    email.setError("Campo Requerido");
                    email.requestFocus();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        }

        //Email válido
        if (!objValidar.isEmail(emaila)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    email.setError("Campo Requerido");
                    email.requestFocus();
                    Toast.makeText(getApplicationContext(), "Ingrese un Email válido", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        } else {
            emailAux = emaila;
        }


        //Contraseña Actual
        String passwordActual = actualPassword.getText().toString().trim();
        if (objValidar.Vacio(actualPassword)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su Contraseña Actual", Toast.LENGTH_SHORT).show();
                    actualPassword.setError("Campo Requerido");
                    actualPassword.requestFocus();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        } else {
            actualPasswordAux = passwordActual;
        }

        //Validación Contraseña
        String passworda = password.getText().toString().trim();
        if (objValidar.Vacio(password)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese una Contraseña", Toast.LENGTH_SHORT).show();
                    password.setError("Campo Requerido");
                    password.requestFocus();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        }

        //Validación Validar Contraseña
        String valPassword = validatePassword.getText().toString().trim();
        if (objValidar.Vacio(validatePassword)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese Validación de Contraseña", Toast.LENGTH_SHORT).show();
                    validatePassword.setError("Campo Requerido");
                    validatePassword.requestFocus();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        }

        //Contraseñas coinciden
        if (!objValidar.isEquals(passworda, valPassword)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                    password.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
                    passwordLinear.setBackgroundColor(Color.parseColor(redColor));
                    validatePassword.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
                    validatePasswordLinear.setBackgroundColor(Color.parseColor(redColor));
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        } else {
            passwordAux = passworda;
        }

        //Validación Celular
        if (objValidar.Vacio(phone)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su Celular", Toast.LENGTH_SHORT).show();
                    phone.setError("Campo Requerido");
                    phone.requestFocus();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            });
            return false;
        } else {
            phoneAux = phone.getText().toString().trim();
        }

        //Validación Nombre de EPS
        String eps = eEPS.getSelectedItem().toString().trim();
        if (objValidar.isEquals(eps, "EPS")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su EPS", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    editEpsLinear.setBackgroundColor(Color.parseColor(redColor));
                }
            });
            return false;
        } else {
            epsAux = eps;
        }

        //Validación EPS Régimen
        String epsRegimen = eRegimenEPS.getSelectedItem().toString().trim();
        if (objValidar.isEquals(epsRegimen, "Régimen de Afiliación EPS")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su Régimen de Afiliación EPS", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    editAfiliacionLinear.setBackgroundColor(Color.parseColor(redColor));
                }
            });
            return false;
        } else {
            afiliacionAux = epsRegimen;
        }

        //Validación Medicina Prepagada
        String prepaidM = ePrepagada.getSelectedItem().toString().trim();
        if (objValidar.isEquals(prepaidM, "Medicina Prepagada")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese Medicina Prepagada", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    editPrepaidMedicineLinear.setBackgroundColor(Color.parseColor(redColor));
                }
            });
            return false;
        } else {
            prepaidMedicineAux = prepaidM;
        }

        //Validación RH
        String rh = eBloodType.getSelectedItem().toString().trim();
        if (objValidar.isEquals(rh, "Tipo de Sangre")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese su Tipo de Sangre", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    editRhLinear.setBackgroundColor(Color.parseColor(redColor));
                }
            });
            return false;
        } else {
            rhAux = rh;
        }

        //Validación Enfermedad
        String disease = eDisease.getSelectedItem().toString().trim();
        if (objValidar.isEquals(disease, "¿Tienes alguna Enfermedad Crónica?")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese Enfermedad Crónica", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    editDiseaseLinear.setBackgroundColor(Color.parseColor(redColor));
                }
            });
            return false;
        } else {
            diseaseAux = disease;
        }

        //Validación Alergía Ambiental
        String environmentA = eEnvironmentAllergy.getSelectedItem().toString().trim();
        if (objValidar.isEquals(environmentA, "¿Posees alguna Alergía Ambiental?")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese Alergía Ambiental", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    editambientalAllergyLinear.setBackgroundColor(Color.parseColor(redColor));
                }
            });
            return false;
        } else {
            ambientalAllergyAux = environmentA;
        }

        //Validación Alergía Medicina
        String medicineA = eMedicinesAllergy.getSelectedItem().toString().trim();
        if (objValidar.isEquals(medicineA, "¿Posees alguna Alergía a un Medicamento?")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese Alergía a Medicamento", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    editMedicineAllergyLinear.setBackgroundColor(Color.parseColor(redColor));
                }
            });
            return false;
        } else {
            medicineAllergyAux = medicineA;
        }

        //Validación Alergía Ambiental
        String medicine = eMedicine.getSelectedItem().toString().trim();
        if (objValidar.isEquals(medicine, "¿Tomas algún Medicamento?")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Ingrese Medicamento", Toast.LENGTH_SHORT).show();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    editMedicineLinear.setBackgroundColor(Color.parseColor(redColor));
                }
            });
            return false;
        } else {
            medicineAux = medicine;
        }
        if (ePC.isChecked()) {
            complementaryPlanAux = true;
        }
        return true;
    }

    private void actualizarPerfilFB() {
        processDialog.setMessage("Actualizando perfil");
        processDialog.show();
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), actualPasswordAux);
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            currentUser.updatePassword(passwordAux).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        currentUser.updateEmail(emailAux).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mDatabase.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                String id;
                                                                Perfil userUpdated = new Perfil(emailAux);
                                                                userUpdated.setPassword(passwordAux);
                                                                PerfilPersistence user = new PerfilPersistence();
                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                    user = snapshot.getValue(PerfilPersistence.class);
                                                                    if (!user.getEmail().isEmpty() && user.getEmail().equals(emailActual)) {
                                                                        id = snapshot.getKey();
                                                                        mDatabase.child(id).setValue(userUpdated);
                                                                        actualizarPerfilBasico();
                                                                        Toast.makeText(getApplicationContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show();
                                                                        break;
                                                                    }
                                                                }

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Error al actualizar correo", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error al actualizar contraseña", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "La contraseña actual es incorrecta", Toast.LENGTH_SHORT).show();
                        }
                        processDialog.dismiss();
                    }
                });
    }

    private void actualizarPerfilBasico() {
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String id;
                    PerfilBasicoPersistence user = new PerfilBasicoPersistence();
                    PerfilBasico userUpdated = new PerfilBasico(emailAux, nameAux, lastNameAux, idTypeAux, idAux, ageAux, phoneAux, genderAux);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(emailActual)) {
                            id = snapshot.getKey();
                            mDatabaseBasic.child(id).setValue(userUpdated);
                            actualizarPerfilMedico();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void actualizarPerfilMedico() {
        mDatabaseMedical.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String id;
                    PerfilMedicoPersistence user = new PerfilMedicoPersistence();
                    PerfilMedico userUpdated = new PerfilMedico(emailAux, rhAux, diseaseAux, ambientalAllergyAux, medicineAllergyAux, medicineAux);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilMedicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(emailActual)) {
                            id = snapshot.getKey();
                            mDatabaseMedical.child(id).setValue(userUpdated);
                            actualizarPerfilXEPS();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void actualizarPerfilXEPS() {
        mDatabaseEPS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String id;
                    PerfilXEPS userUpdated = new PerfilXEPS(emailAux, epsAux, afiliacionAux, complementaryPlanAux);
                    PerfilXEPSPersistence user = new PerfilXEPSPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilXEPSPersistence.class);
                        if (!user.getEmailPerfil().isEmpty() && user.getEmailPerfil().equals(emailActual)) {
                            id = snapshot.getKey();
                            mDatabaseEPS.child(id).setValue(userUpdated);
                            actualizarPerfilXPrepagada();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void actualizarPerfilXPrepagada() {
        mDatabasePrepagada.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String id;
                    PerfilxPrepagada userUpdated = new PerfilxPrepagada(emailAux, prepaidMedicineAux);
                    PerfilXPrepagadaPersistence user = new PerfilXPrepagadaPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilXPrepagadaPersistence.class);
                        if (!user.getEmailPerfil().isEmpty() && user.getEmailPerfil().equals(emailActual)) {
                            id = snapshot.getKey();
                            mDatabasePrepagada.child(id).setValue(userUpdated);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}