package com.example.miguelapaez.emergenciapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Adapters.AdapterFamilyGroup;
import com.example.miguelapaez.emergenciapp.Entities.EntityFamilyGroup;

import java.util.ArrayList;

public class FamilyGroupHealth extends AppCompatActivity {

    ListView listItemsFamilyGroup;
    private AdapterFamilyGroup adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_family_group_health );
        getSupportActionBar().hide();

        listItemsFamilyGroup = findViewById ( R.id.listViewFamilyGroupHealth );
        adaptador = new AdapterFamilyGroup ( this, GetArrayItems ());
        listItemsFamilyGroup.setAdapter(adaptador);

        listItemsFamilyGroup.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView <?> adapterView , View view , int i , long l) {
                Toast.makeText ( adapterView.getContext (), "Selecciona: "
                        +i, Toast.LENGTH_SHORT).show ();
                Intent intent = new Intent ( FamilyGroupHealth.this , HealthQuestion.class );
                startActivityForResult ( intent , 0 );
            }
        } );

    }

    private ArrayList<EntityFamilyGroup> GetArrayItems(){
        ArrayList<EntityFamilyGroup> listItems = new ArrayList <> ();
        listItems.add(new EntityFamilyGroup ( R.drawable.mujer, "Mi Nombre", "Yo", "prueba01@gmail.com" ));
        listItems.add(new EntityFamilyGroup ( R.drawable.hombre, "Nombre Familiar", "Parentesco", "prueba02@gmail.com" ));
        listItems.add(new EntityFamilyGroup ( R.drawable.hombre, "Nombre Familiar", "Parentesco", "prueba03@gmail.com" ));
        listItems.add(new EntityFamilyGroup ( R.drawable.incognito, "Otro", "", "prueba04@gmail.com" ));
        return listItems;
    }

}
