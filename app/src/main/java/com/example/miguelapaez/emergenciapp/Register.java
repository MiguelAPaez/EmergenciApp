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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;

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
    String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +
            "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";

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
        super.onCreate(savedInstanceState);
        if (bussiness.verificarSesion()){
            startActivity(new Intent(Register.this, MainActivity.class));
        }
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

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
        eName = (EditText) findViewById(R.id.nameRegister);
        eLastName = (EditText) findViewById(R.id.lastNameRegister);
        spinTypeId = (Spinner) findViewById(R.id.idTypeRegister);
        eID = (EditText) findViewById(R.id.idRegister);
        bDate = (Button) findViewById(R.id.buttonDateRegister);
        eDate = (EditText) findViewById(R.id.dateRegister);
        eEmail = (EditText) findViewById(R.id.emailRegister);
        ePassword = (EditText) findViewById(R.id.passwordRegister);
        eValidatePassword = (EditText) findViewById(R.id.validatePasswordRegister);
        ePhone = (EditText) findViewById(R.id.phoneRegister);
        spinGender = (Spinner) findViewById(R.id.genderRegister);

        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                ano = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker var1, int var2, int var3, int var4) {
                        eDate.setText(var4 + "/" + (var3 + 1) + "/" + var2);
                        diaaux = var4;
                        Log.i( "DIA", String.valueOf ( diaaux ) );
                        mesaux = var3+1;
                        Log.i( "MES", String.valueOf ( mesaux ) );
                        anoaux = var2;
                        Log.i( "ANO", String.valueOf ( anoaux ) );
                    }
                }
                        , dia, mes, ano);
                datePickerDialog.show();
            }
        });

        getSupportActionBar().hide();

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
        Log.i( "CEL", String.valueOf ( phone ) );
        gender = spinGender.getSelectedItem().toString().trim();
    }

    public boolean validarDatos(){
        if(validarNombre() && validarApellidos () && validarIdType () && validarId () &&
                validarDate () && validarEmail () && validarPassword() && validarValidatePassword () &&
            validarContraseñas () && validarPhone () && validarGender ()){
            return true;
        } else {
            scrollView.post(new Runnable() { @Override public void run() { scrollView.fullScroll(ScrollView.FOCUS_UP); } });
            return false;
        }
    }

    public boolean validarNombre(){
        if(eName.getText().toString().trim().equals ("")){
            eName.getBackground().setColorFilter ( getResources ().getColor ( R.color.colorRed ), PorterDuff.Mode.SRC_ATOP );
            nameL.setBackgroundColor ( Color.parseColor ( redColor ) );
            return false;
        }else {
            return true;
        }
    }

    public boolean validarApellidos(){
        if(eLastName.getText().toString().trim().equals ("")){
            eLastName.getBackground().setColorFilter ( getResources ().getColor ( R.color.colorRed ), PorterDuff.Mode.SRC_ATOP );
            lastNameL.setBackgroundColor ( Color.parseColor ( redColor ) );
            return false;
        }else {
            return true;
        }
    }

    public boolean validarIdType(){
        if(spinTypeId.getSelectedItem().toString().trim().equals ("Tipo de Identificación")){
            typeIdL.setBackgroundColor ( Color.parseColor ( redColor ) );
            return false;
        }else {
            return true;
        }
    }

    public boolean validarId(){
        if(eID.getText().toString().trim().equals ("")){
            eID.getBackground().setColorFilter ( getResources ().getColor ( R.color.colorRed ), PorterDuff.Mode.SRC_ATOP );
            idL.setBackgroundColor ( Color.parseColor ( redColor ) );
            return false;
        }else {
            return true;
        }
    }

    public boolean validarDate(){
        if(eDate.getText().toString().trim().equals ("")){
            eDate.getBackground().setColorFilter ( getResources ().getColor ( R.color.colorRed ), PorterDuff.Mode.SRC_ATOP );
            dateL.setBackgroundColor ( Color.parseColor ( redColor ) );
            return false;
        }else {
            return true;
        }
    }

    public boolean validarEmail(){
        String aux = eEmail.getText().toString().trim();
        if(eEmail.getText().toString().trim().equals ("") || !validarEmailAux(aux) ){
            eEmail.getBackground().setColorFilter ( getResources ().getColor ( R.color.colorRed ), PorterDuff.Mode.SRC_ATOP );
            emailL.setBackgroundColor ( Color.parseColor ( redColor ) );
            return false;
        }else {
            return true;
        }
    }

    public boolean validarEmailAux(String email){
        Pattern pattern = Pattern.compile(emailPattern);
        if (email != null) {
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                System.out.println("Válido");
                return true;
            }
            else {
                System.out.println("NO Válido");
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean validarPassword(){
        if(ePassword.getText().toString().trim().equals ("")){
            ePassword.getBackground().setColorFilter ( getResources ().getColor ( R.color.colorRed ), PorterDuff.Mode.SRC_ATOP );
            passwordL.setBackgroundColor ( Color.parseColor ( redColor ) );
            return false;
        }else {
            return true;
        }
    }

    public boolean validarValidatePassword(){
        String aux = eValidatePassword.getText().toString().trim();
        if(aux.equals ("")){
            eValidatePassword.getBackground().setColorFilter ( getResources ().getColor ( R.color.colorRed ), PorterDuff.Mode.SRC_ATOP );
            validatePasswordL.setBackgroundColor ( Color.parseColor ( redColor ) );
            return false;
        }else {
            return true;
        }
    }

    public boolean validarContraseñas(){
        String pass1 = ePassword.getText().toString().trim();
        String pass2 = eValidatePassword.getText().toString().trim();
        if (pass1.equals ( pass2 )){
            return true;
        }else {
            ePassword.getBackground().setColorFilter ( getResources ().getColor ( R.color.colorRed ), PorterDuff.Mode.SRC_ATOP );
            passwordL.setBackgroundColor ( Color.parseColor ( redColor ) );
            eValidatePassword.getBackground().setColorFilter ( getResources ().getColor ( R.color.colorRed ), PorterDuff.Mode.SRC_ATOP );
            validatePasswordL.setBackgroundColor ( Color.parseColor ( redColor ) );
            return false;
        }
    }

    public boolean validarPhone(){
        if(ePhone.getText().toString().trim().equals ("") ){
            ePhone.getBackground().setColorFilter ( getResources ().getColor ( R.color.colorRed ), PorterDuff.Mode.SRC_ATOP );
            phoneL.setBackgroundColor ( Color.parseColor ( redColor ) );
            return false;
        }else {
            return true;
        }
    }

    public boolean validarGender(){
        if(spinGender.getSelectedItem().toString().trim().equals ("Género")){
            genderL.setBackgroundColor ( Color.parseColor ( redColor ) );
            return false;
        }else {
            return true;
        }
    }

}
