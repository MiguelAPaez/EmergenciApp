package com.example.miguelapaez.emergenciapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miguelapaez.emergenciapp.Adapters.AdapterFamilyGroup;
import com.example.miguelapaez.emergenciapp.Entities.EntityFamilyGroup;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.ReferenciaGrupoPersistence;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FamilyGroupHealth extends AppCompatActivity {

    DatabaseReference mDatabaseGroup, mDatabaseBasic;
    ListView listItemsFamilyGroup;
    private AdapterFamilyGroup adaptador;
    ArrayList<EntityFamilyGroup> listItems = new ArrayList<>();
    String emailActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_family_group_health );
        getSupportActionBar().hide();
        mDatabaseBasic = FirebaseDatabase.getInstance().getReference("Perfiles Basicos");
        emailActual = getIntent().getStringExtra("emailActual");
        listItemsFamilyGroup = findViewById ( R.id.listViewFamilyGroupHealth );
        adaptador = new AdapterFamilyGroup ( this, listItems);
        listItemsFamilyGroup.setAdapter(adaptador);
        cargarGrupo();

        listItemsFamilyGroup.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView <?> adapterView , View view , int i , long l) {
                Toast.makeText ( adapterView.getContext (), "Selecciona: "
                        +i, Toast.LENGTH_SHORT).show ();
                Intent intent = new Intent ( FamilyGroupHealth.this , HealthQuestion.class );
                intent.putExtra("email", listItems.get(i).getEmail());
                intent.putExtra("email", listItems.get(i).getParent());
                startActivityForResult ( intent , 0 );
            }
        } );

    }

    private void cargarGrupo(){
        mDatabaseBasic.orderByChild("email").equalTo(emailActual);
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilBasicoPersistence user;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(emailActual)) {
                            ReferenciaGrupoPersistence me = new ReferenciaGrupoPersistence();
                            me.setEmail(emailActual);
                            me.setRol("Yo");
                            cargarFamiliar(me);
                            mDatabaseGroup = FirebaseDatabase.getInstance().getReference("Perfiles Basicos").child(snapshot.getKey()).child("Grupo");
                            mDatabaseGroup.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        ReferenciaGrupoPersistence familiar;
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            familiar = snapshot.getValue(ReferenciaGrupoPersistence.class);
                                            cargarFamiliar(familiar);
                                        }
                                    }
                                    else {
                                        EntityFamilyGroup other = new EntityFamilyGroup(R.drawable.incognito,"Otro",null,null);
                                        listItems.add(other);
                                        adaptador.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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

    private void cargarFamiliar(final ReferenciaGrupoPersistence familiar) {
        mDatabaseBasic.orderByChild("email").equalTo(familiar.getEmail());
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilBasicoPersistence user;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(familiar.getEmail())) {
                            int gender;
                            String name = user.getName() + " " + user.getLastName();
                            if (user.getGender().equals("Masculino")) {
                                gender = R.drawable.hombre;
                            } else {
                                gender = R.drawable.mujer;
                            }
                            EntityFamilyGroup fam = new EntityFamilyGroup(gender, name, familiar.getRol(), user.getEmail());
                            listItems.add(fam);
                            adaptador.notifyDataSetChanged();
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
