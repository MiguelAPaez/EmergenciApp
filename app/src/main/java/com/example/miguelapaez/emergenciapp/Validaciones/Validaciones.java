package com.example.miguelapaez.emergenciapp.Validaciones;

import android.text.TextUtils;
import android.widget.EditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validaciones {

    // Patrón para validar el email
    Pattern pattern = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                             + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    //metodo para validar si es un valor numerico
    public  boolean isNumeric(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }

    //metodo para validar si es un email
    public  boolean isEmail(String cadena) {
        boolean resultado;
        Matcher mather = pattern.matcher( cadena );
        if (mather.find() == true) {
            System.out.println("El email ingresado es válido.");
            resultado = true;
        } else {
            System.out.println("El email ingresado es inválido.");
            resultado = false;
        }

        return resultado;
    }

    //metodo para validar si editext esta vacio
    public  boolean Vacio(EditText campo){
        String dato = campo.getText().toString().trim();
        if(TextUtils.isEmpty(dato)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isEquals(String cadena, String valorGenerico){
        boolean resultado;
        if(cadena.equals (valorGenerico)){
            resultado=true;
        }else{
            resultado=false;
        }
        return resultado;
    }

    public boolean isPassword (String cadena){
        boolean resultado = false;
        boolean tieneLetras = false;
        boolean tieneNumeros = false;
        char letra;
        if(cadena.length () >= 8 ){
            for (byte i = 0; i < cadena.length(); i++) {
                letra = cadena.charAt(i);
                String passValue = String.valueOf(letra);
                if (passValue.matches("[a-z]")) {
                    tieneLetras = true;
                } else if (passValue.matches("[0-9]")) {
                    tieneNumeros = true;
                }
            }
            if (tieneLetras && tieneNumeros){
                resultado = true;
            }
        }
        return resultado;
    }

}
