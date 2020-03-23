package com.example.miguelapaez.emergenciapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Negocio.FacadeNegocio;
import com.example.miguelapaez.emergenciapp.Negocio.ImplementacionNegocio;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_buscarFamiliar extends AppCompatActivity {
    FacadeNegocio bussiness = new ImplementacionNegocio();
    TextView message;
    EditText eEmail;
    String email;
    DatabaseReference mDatabaseBasic;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_familiar);
        getSupportActionBar().hide();
        message = (TextView) findViewById ( R.id.messageBuscar );
        eEmail = (EditText) findViewById(R.id.InputEmailABuscar);
        mDatabaseBasic = FirebaseDatabase.getInstance().getReference("Perfiles basicos");
        progressDialog = new ProgressDialog(this);
        String font_path = "font/Arvo-Regular.ttf";
        Typeface TF = Typeface.createFromAsset ( getAssets (), font_path );
        message.setTypeface ( TF );

        final TextView txtSub = (TextView)findViewById(R.id.messageSignUpBuscar);
        final TextView txtSub2 = (TextView)findViewById(R.id.buttonBuscarFamiliar);

        txtSub.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent ( v.getContext(), activity_registro_familiar.class);
                startActivityForResult(intent, 0);
            }
        });

        txtSub2.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                email = eEmail.getText().toString().trim();
                buscarFamiliar(email);
            }
        });
    }
    private void buscarFamiliar(final String email){
        mDatabaseBasic.orderByChild("email").equalTo(email);
        progressDialog.setMessage("Cargando perfil");
        progressDialog.show();
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilBasicoPersistence user = new PerfilBasicoPersistence();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                    }
                    if (!user.getEmail().isEmpty() && user.getEmail().equals(email)) {
                        Intent intent = new Intent(getApplicationContext(), mostrarFamiliarEncontrado.class);
                        intent.putExtra("basicProfile", user);
                        startActivity(intent);
                   }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Usuario no encontrado",Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
