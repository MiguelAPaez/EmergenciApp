package com.example.miguelapaez.emergenciapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Adapters.AdapterFamilyGroup;
import com.example.miguelapaez.emergenciapp.Entities.EntityFamilyGroup;

import java.util.ArrayList;

public class FamilyGroup extends AppCompatActivity {

    TextView message;
    ListView listItemsFamilyGroup;
    private AdapterFamilyGroup adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_family_group );
        getSupportActionBar().hide();

        listItemsFamilyGroup = findViewById ( R.id.listViewFamilyGroup );
        adaptador = new AdapterFamilyGroup ( this, GetArrayItems ());
        listItemsFamilyGroup.setAdapter(adaptador);

        listItemsFamilyGroup.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView <?> adapterView , View view , int i , long l) {
                Toast.makeText (adapterView.getContext (),"Selecciona: "
                    +i,Toast.LENGTH_SHORT).show ();
            }
        } );


        message = (TextView) findViewById ( R.id.messageAgregar );
        String font_path = "font/Arvo-Regular.ttf";
        Typeface TF = Typeface.createFromAsset ( getAssets (), font_path );
        message.setTypeface ( TF );

        final TextView txtSub = (TextView)findViewById(R.id.messageSignUpFamilyGroup);

        txtSub.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent ( v.getContext(), activity_buscarFamiliar.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    private ArrayList<EntityFamilyGroup> GetArrayItems(){
        ArrayList<EntityFamilyGroup> listItems = new ArrayList <> ();
        listItems.add(new EntityFamilyGroup ( R.drawable.hombre, "Antonio", "Padre" ));
        listItems.add(new EntityFamilyGroup ( R.drawable.mujer, "Andrea", "Madre" ));
        listItems.add(new EntityFamilyGroup ( R.drawable.hombre, "Evaristo", "Abuelo" ));
        listItems.add(new EntityFamilyGroup ( R.drawable.mujer, "Luisa", "Sobrina" ));
        listItems.add(new EntityFamilyGroup ( R.drawable.mujer, "Ángela", "La Profe :)" ));
        return listItems;
    }


}
