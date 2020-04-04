package com.example.miguelapaez.emergenciapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Entities.Perfil;
import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.example.miguelapaez.emergenciapp.Validaciones.Validaciones;

import java.util.Calendar;


public class Register extends AppCompatActivity {
    String eAge;
    Button bDate;
    EditText eDate, eName, eLastName, eID, eEmail, ePassword, eValidatePassword, ePhone;
    ScrollView scrollView;
    LinearLayout nameL, lastNameL, idL, typeIdL, dateL, emailL, passwordL, validatePasswordL, phoneL, genderL;
    Spinner spinTypeId, spinGender;
    private int ano, mes, dia;
    private int anoaux, mesaux, diaaux;
    FacadeNegocio bussiness = new ImplementacionNegocio();
    String name, lastName, typeId, id, age, email, password,phone, gender;
    String redColor = "#40FF0000";
    Validaciones objValidar;
    boolean validacionOK;

    @Override
    public void onResume() {
        super.onResume();
        if (bussiness.verificarSesion()) {
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (bussiness.verificarSesion ()) {
            startActivity ( new Intent ( Register.this , MainActivity.class ) );
        }
        setContentView ( R.layout.activity_register );
        getSupportActionBar ().hide ();

        //ScrollView Register
        scrollView = (ScrollView) findViewById ( R.id.scrollViewRegister );

        //LinearLayout de Register
        nameL = (LinearLayout) findViewById ( R.id.linearLayoutNameRegister );
        lastNameL = (LinearLayout) findViewById ( R.id.linearLayoutLastNameRegister );
        typeIdL = (LinearLayout) findViewById ( R.id.linearLayoutIdTypeRegister );
        idL = (LinearLayout) findViewById ( R.id.linearLayoutIdRegister );
        dateL = (LinearLayout) findViewById ( R.id.linearLayoutDateRegister );
        emailL = (LinearLayout) findViewById ( R.id.linearLayoutEmailRegister );
        passwordL = (LinearLayout) findViewById ( R.id.linearLayoutPasswordRegister );
        validatePasswordL = (LinearLayout) findViewById ( R.id.linearLayoutValidatePasswordRegister );
        phoneL = (LinearLayout) findViewById ( R.id.linearLayoutPhoneRegister );
        genderL = (LinearLayout) findViewById ( R.id.linearLayoutGenderRegister );

        //EditText de Register
        eName = (EditText) findViewById ( R.id.nameRegister );
        eLastName = (EditText) findViewById ( R.id.lastNameRegister );
        spinTypeId = (Spinner) findViewById ( R.id.idTypeRegister );
        eID = (EditText) findViewById ( R.id.idRegister );
        bDate = (Button) findViewById ( R.id.buttonDateRegister );
        eDate = (EditText) findViewById ( R.id.dateRegister );
        eEmail = (EditText) findViewById ( R.id.emailRegister );
        ePassword = (EditText) findViewById ( R.id.passwordRegister );
        eValidatePassword = (EditText) findViewById ( R.id.validatePasswordRegister );
        ePhone = (EditText) findViewById ( R.id.phoneRegister );
        spinGender = (Spinner) findViewById ( R.id.genderRegister );


        //Validaciones
        objValidar = new Validaciones();

        bDate.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance ();
                ano = c.get ( Calendar.YEAR );
                mes = c.get ( Calendar.MONTH );
                dia = c.get ( Calendar.DAY_OF_MONTH );
                DatePickerDialog datePickerDialog = new DatePickerDialog ( Register.this , new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker var1 , int var2 , int var3 , int var4) {
                        eDate.setText ( var4 + "/" + (var3 + 1) + "/" + var2 );
                        diaaux = var4;
                        Log.i ( "DIA" , String.valueOf ( diaaux ) );
                        mesaux = var3 + 1;
                        Log.i ( "MES" , String.valueOf ( mesaux ) );
                        anoaux = var2;
                        Log.i ( "ANO" , String.valueOf ( anoaux ) );
                    }
                }
                        , dia , mes , ano );
                datePickerDialog.show ();
            }
        } );

        LinearLayout btn = (LinearLayout) findViewById ( R.id.buttonNextRegister );
        btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                ThreadB b = new ThreadB();
                b.start();

                synchronized(b){

                    try{
                        System.out.println("Waiting for b to complete...");
                        b.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    if(validacionOK) {
                        llenarDatos ();
                        Perfil profile = crearPerfil();
                        PerfilBasico basicProfile = crearPerfilBasico();
                        Intent intent = new Intent ( v.getContext () , HealthRegister.class );
                        intent.putExtra ( "profile" , profile);
                        intent.putExtra ( "basicProfile" , basicProfile);
                        startActivity ( intent );
                    }
                }
            }
        } );

        /*
        Button btn = (Button) findViewById(R.id.buttonNextRegister);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarDatos()) {
                    llenarDatos ();
                    Intent intent = new Intent ( v.getContext () , HealthRegister.class );
                    intent.putExtra ( "name" , name );
                    intent.putExtra ( "lastName" , lastName );
                    intent.putExtra ( "typeId" , typeId );
                    intent.putExtra ( "id" , id );
                    intent.putExtra ( "age" , age );
                    intent.putExtra ( "email" , email );
                    intent.putExtra ( "password" , password );
                    intent.putExtra ( "phone" , phone );
                    intent.putExtra ( "gender" , gender );
                    startActivity ( intent );
                }
            }
        });
        */
    }

    private void llenarDatos(){

        name = eName.getText().toString().trim();
        lastName = eLastName.getText().toString().trim();
        typeId = spinTypeId.getSelectedItem().toString().trim();
        id = eID.getText().toString().trim();
        eAge= bussiness.getAge(anoaux,mesaux,diaaux);
        age = eAge;
        email = eEmail.getText().toString().trim();
        password = ePassword.getText().toString().trim();
        phone = ePhone.getText().toString().trim();
        gender = spinGender.getSelectedItem().toString().trim();
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
    Perfil user = new Perfil(email);
    user.setPassword(password);
    return user;
}
private PerfilBasico crearPerfilBasico(){
        PerfilBasico user = new PerfilBasico(email,name,lastName,typeId,id,age,phone,gender);
        return user;
}
    /*public void validar(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        validacionOK = validarDatos ();
                    }
                }
        ).start ();
    }*/

    public boolean validarDatos(){

        //Validación Nombres
        if(objValidar.Vacio ( eName )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su Nombre" , Toast.LENGTH_SHORT ).show ();
                    eName.setError("Campo Requerido");
                    eName.requestFocus();
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

        //Validación Apellidos
        if(objValidar.Vacio ( eLastName )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese sus Apellidos" , Toast.LENGTH_SHORT ).show ();
                    eLastName.setError("Campo Requerido");
                    eLastName.requestFocus();
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

        //Validación Tipo de Identificación
        String idType = spinTypeId.getSelectedItem().toString().trim();
        if(objValidar.isEquals ( idType, "Tipo de Identificación" )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su Tipo de Identificación" , Toast.LENGTH_SHORT ).show ();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                    typeIdL.setBackgroundColor ( Color.parseColor ( redColor ) );
                }
            });
            return false;
        }

        //Validación Número de Identificación
        if(objValidar.Vacio ( eID )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su Número de Identificación" , Toast.LENGTH_SHORT ).show ();
                    eID.setError("Campo Requerido");
                    eID.requestFocus();
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

        //Validación Fecha de Nacimiento
        if(objValidar.Vacio ( eDate )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su Fecha de Nacimiento" , Toast.LENGTH_SHORT ).show ();
                    eDate.setError("Campo Requerido");
                    eDate.requestFocus();
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

        //Validación Email
        String email = eEmail.getText().toString().trim();
        if(objValidar.Vacio ( eEmail )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su Email" , Toast.LENGTH_SHORT ).show ();
                    eEmail.setError("Campo Requerido");
                    eEmail.requestFocus();
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
        if(!objValidar.isEmail ( email )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    eEmail.setError ( "Campo Requerido" );
                    eEmail.requestFocus ();
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
        }

        //Validación Contraseña
        String password = ePassword.getText().toString().trim();
        if(objValidar.Vacio ( ePassword )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese una Contraseña" , Toast.LENGTH_SHORT ).show ();
                    ePassword.setError("Campo Requerido");
                    ePassword.requestFocus();
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
        String valPassword = eValidatePassword.getText().toString().trim();
        if(objValidar.Vacio ( eValidatePassword )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese Validación de Contraseña" , Toast.LENGTH_SHORT ).show ();
                    eValidatePassword.setError("Campo Requerido");
                    eValidatePassword.requestFocus();
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
        if(!objValidar.isEquals ( password, valPassword )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Las contraseñas no son iguales" , Toast.LENGTH_SHORT ).show ();
                    ePassword.getBackground ().setColorFilter ( getResources ().getColor ( R.color.colorRed ) , PorterDuff.Mode.SRC_ATOP );
                    passwordL.setBackgroundColor ( Color.parseColor ( redColor ) );
                    eValidatePassword.getBackground ().setColorFilter ( getResources ().getColor ( R.color.colorRed ) , PorterDuff.Mode.SRC_ATOP );
                    validatePasswordL.setBackgroundColor ( Color.parseColor ( redColor ) );
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

        //Validación Celular
        if(objValidar.Vacio ( ePhone )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su Celular" , Toast.LENGTH_SHORT ).show ();
                    ePhone.setError("Campo Requerido");
                    ePhone.requestFocus();
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

        //Validación Género
        String gender = spinGender.getSelectedItem().toString().trim();
        if(objValidar.isEquals ( gender, "Género" )){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText ( getApplicationContext () , "Ingrese su Género" , Toast.LENGTH_SHORT ).show ();
                    scrollView.post ( new Runnable () {
                        @Override
                        public void run() {
                            scrollView.fullScroll ( ScrollView.FOCUS_UP );
                        }
                    } );
                    genderL.setBackgroundColor ( Color.parseColor ( redColor ) );
                }
            });
            return false;
        }
        return true;
    }
}
