package com.example.miguelapaez.emergenciapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilMedicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilXEPSPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilXPrepagadaPersistence;
import com.example.miguelapaez.emergenciapp.Validaciones.Validaciones;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private EditText name, lastName, idType, id, age, email, password, validatePassword, phone, gender, eps, afiliacion, complementaryPlan, prepaidMedicine, rh, disease,
            ambientalAllergy, medicineAllergy, medicine;
    private ImageView profilePhoto;
    private TextView update, message, title;
    private String nameAux, lastNameAux, idTypeAux, idAux, ageAux, emailAux, passwordAux, phoneAux, genderAux;
    ScrollView scrollView;
    private LinearLayout passwordLinear, validatePasswordLinear, titleMedical, ageLinear, genderLinear, epsLinear, afiliacionLinear, complmentaryLinear, prepaidLinear, rhLinear, diseaseLinear,
            environmentLinear, medicineAllergyLinear, medicineLinear, buttonNext;
    DatabaseReference mDatabaseBasic, mDatabaseMedical, mDatabaseEPS, mDatabasePrepagada;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressDialog processDialog;
    String redColor = "#40FF0000";
    Validaciones objValidar;
    boolean validacionOK;

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
        password = (EditText) findViewById ( R.id.passwordProfile );
        validatePassword = (EditText) findViewById ( R.id.validatePasswordProfile );
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
        update = (TextView) findViewById ( R.id.meessageSignUpProfile );
        title = (TextView) findViewById ( R.id.titleProfileActivity );

        //Validaciones
        objValidar = new Validaciones();

        //ScrollView
        scrollView = (ScrollView) findViewById ( R.id.scrollViewProfile );

        //LinearLayout's
        passwordLinear = (LinearLayout) findViewById ( R.id.linearPasswordProfile );
        validatePasswordLinear = (LinearLayout) findViewById ( R.id.linearLayoutValidatePasswordProfile );
        titleMedical = (LinearLayout) findViewById ( R.id.linearLayoutTitleMedicalProfile );
        epsLinear = (LinearLayout) findViewById ( R.id.linearEpsProfile );
        afiliacionLinear = (LinearLayout) findViewById ( R.id.linearAffiliationProfile );
        complmentaryLinear = (LinearLayout) findViewById ( R.id.linearComplementaryPlanProfile );
        prepaidLinear = (LinearLayout) findViewById ( R.id.linearPrepaidMedicineProfile );
        rhLinear = (LinearLayout) findViewById ( R.id.linearRhProfile );
        diseaseLinear = (LinearLayout) findViewById ( R.id.linearDiseaseProfile );
        environmentLinear = (LinearLayout) findViewById ( R.id.linearEnvironmentAllergyProfile );
        medicineAllergyLinear = (LinearLayout) findViewById ( R.id.linearMedicineAllergyProfile );
        medicineLinear = (LinearLayout) findViewById ( R.id.linearMedicineProfile );
        genderLinear = (LinearLayout) findViewById ( R.id.linearGenderProfile );
        ageLinear = (LinearLayout) findViewById ( R.id.linearAgeProfile );
        buttonNext = (LinearLayout) findViewById ( R.id.buttonMedicalProfile );

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

        buttonNext.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Profile.ThreadB b = new Profile.ThreadB ();
                b.start();

                synchronized(b){

                    try{
                        System.out.println("Waiting for b to complete...");
                        b.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    if(validacionOK) {
                        Perfil profile = crearPerfil();
                        PerfilBasico basicProfile = crearPerfilBasico();
                        Intent intent = new Intent ( v.getContext () , HealthRegister.class );
                        intent.putExtra ( "activity", "profile" );
                        intent.putExtra ( "profile" , profile);
                        intent.putExtra ( "basicProfile" , basicProfile);
                        startActivity ( intent );
                    }
                }
            }
        } );

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
        idTypeAux = user.getTypeId ();
        id.setText(user.getId());
        idAux = user.getId ();
        age.setText(user.getAge());
        ageAux = user.getAge ();
        email.setText(user.getEmail());
        emailAux = user.getEmail ();
        phone.setText(user.getPhone());
        phoneAux = user.getPhone ();
        gender.setText(user.getGender());
        genderAux = user.getGender ();
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

    private void actualizarPerfil(){
        message.setVisibility ( View.GONE );
        update.setVisibility ( View.GONE );
        validatePasswordLinear.setVisibility ( View.VISIBLE );
        title.setText ( R.string.update_profile );
        titleMedical.setVisibility ( View.GONE );
        epsLinear.setVisibility ( View.GONE );
        afiliacionLinear.setVisibility ( View.GONE );
        complmentaryLinear.setVisibility ( View.GONE );
        prepaidLinear.setVisibility ( View.GONE );
        rhLinear.setVisibility ( View.GONE );
        diseaseLinear.setVisibility ( View.GONE );
        environmentLinear.setVisibility ( View.GONE );
        medicineAllergyLinear.setVisibility ( View.GONE );
        medicineLinear.setVisibility ( View.GONE );
        email.setEnabled ( true );
        // email.setText ( emailAux );
        email.setBackgroundTintList ( ColorStateList.valueOf (getResources ().getColor ( R.color.colorBlack ) ) );
        password.setEnabled ( true );
        password.setText("");
        password.setBackgroundTintList ( ColorStateList.valueOf (getResources ().getColor ( R.color.colorBlack ) ) );
        phone.setEnabled ( true );
        // phone.setText ( phoneAux );
        phone.setBackgroundTintList ( ColorStateList.valueOf (getResources ().getColor ( R.color.colorBlack ) ) );
        genderLinear.setVisibility ( View.GONE );
        ageLinear.setVisibility ( View.GONE );
        buttonNext.setVisibility ( View.VISIBLE );
        scrollView.post ( new Runnable () {
            @Override
            public void run() {
                scrollView.fullScroll ( ScrollView.FOCUS_UP );
            }
        } );
    }

    class ThreadB extends Thread{
        int total;
        @Override
        public void run(){
            synchronized(this){
                validacionOK = validarDatos ();
                notify();
            }
        }
    }

    private Perfil crearPerfil(){
        Perfil user = new Perfil(emailAux);
        user.setPassword(passwordAux);
        return user;
    }
    private PerfilBasico crearPerfilBasico(){
        PerfilBasico user = new PerfilBasico(emailAux,nameAux,lastNameAux,idTypeAux,idAux,ageAux,phoneAux,genderAux);
        return user;
    }

    public boolean validarDatos(){

        //Validación Email
        String emaila = email.getText().toString().trim();
        if(objValidar.Vacio ( email )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su Email" , Toast.LENGTH_SHORT ).show ();
                    email.setError("Campo Requerido");
                    email.requestFocus();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                }
            });
            return false;
        }

        //Email válido
        if(!objValidar.isEmail ( emaila )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    email.setError ( "Campo Requerido" );
                    email.requestFocus ();
                    Toast.makeText ( getApplicationContext () , "Ingrese un Email válido" , Toast.LENGTH_SHORT ).show ();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                }
            });
            return false;
        }else{
            emailAux = emaila;
        }

        //Validación Contraseña
        String passworda = password.getText().toString().trim();
        if(objValidar.Vacio ( password )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese una Contraseña" , Toast.LENGTH_SHORT ).show ();
                    password.setError("Campo Requerido");
                    password.requestFocus();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                }
            });
            return false;
        }

        //Validación Validar Contraseña
        String valPassword = validatePassword.getText().toString().trim();
        if(objValidar.Vacio ( validatePassword )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese Validación de Contraseña" , Toast.LENGTH_SHORT ).show ();
                    validatePassword.setError("Campo Requerido");
                    validatePassword.requestFocus();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                }
            });
            return false;
        }

        //Contraseñas coinciden
        if(!objValidar.isEquals ( passworda, valPassword )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Las contraseñas no son iguales" , Toast.LENGTH_SHORT ).show ();
                    password.getBackground ().setColorFilter ( getResources ().getColor ( R.color.colorRed ) , PorterDuff.Mode.SRC_ATOP );
                    passwordLinear.setBackgroundColor ( Color.parseColor ( redColor ) );
                    validatePassword.getBackground ().setColorFilter ( getResources ().getColor ( R.color.colorRed ) , PorterDuff.Mode.SRC_ATOP );
                    validatePasswordLinear.setBackgroundColor ( Color.parseColor ( redColor ) );
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                }
            });
            return false;
        }else{
            passwordAux = passworda;
        }

        //Validación Celular
        if(objValidar.Vacio ( phone )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su Celular" , Toast.LENGTH_SHORT ).show ();
                    phone.setError("Campo Requerido");
                    phone.requestFocus();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                }
            });
            return false;
        }else{
            phoneAux = phone.getText().toString().trim();
        }
        return true;
    }
}
