package com.example.miguelapaez.emergenciapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.miguelapaez.emergenciapp.Adapters.AdapterMedicalCenters;
import com.example.miguelapaez.emergenciapp.Entities.EntityMedicalCenter;
import com.example.miguelapaez.emergenciapp.Persistence.CalificacionPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.EntidadPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.EspecialidadPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.IPSPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilBasicoPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilXEPSPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.PerfilXPrepagadaPersistence;
import com.example.miguelapaez.emergenciapp.Persistence.ReferenciaConvenioPersistence;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MedicalCenters extends AppCompatActivity {

    TextView message;
    private String especialidad;
    private String latitudUser;
    private String longitudUser;
    private String distance = "";
    private String duration = "";
    private String email;
    //private String answer;
    private DatabaseReference mDatabasePerfilEPS, mDatabasePerfilPrepagada, mDatabaseEPS, mDatabasePrepagada, mDatabaseIPS, mDatabaseBasic, mDatabaseCalificaciones;
    private ArrayList<String> listEspecialidades;
    ListView listItemsMedicalCenters;
    private AdapterMedicalCenters adaptador;
    ArrayList<EntityMedicalCenter> listItems = new ArrayList<>();


    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_centers);
        getSupportActionBar().hide();

        request = Volley.newRequestQueue(getApplicationContext());
        message = (TextView) findViewById(R.id.textMessageMedicalCenters);
        String font_path = "font/Arvo-Regular.ttf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        message.setTypeface(TF);
        latitudUser = getIntent().getStringExtra("latitud");
        longitudUser = getIntent().getStringExtra("longitud");
        email = getIntent().getStringExtra("email");
        //answer =  getIntent().getStringExtra("answer1");
        listEspecialidades = getIntent().getStringArrayListExtra("especialidades");
        //Firebase
        mDatabasePerfilEPS = FirebaseDatabase.getInstance().getReference("PerfilesXEPS");
        mDatabasePerfilEPS.orderByChild("emailPerfil").equalTo(email);
        mDatabasePerfilPrepagada = FirebaseDatabase.getInstance().getReference("PerfilesXPrepagada");
        mDatabasePerfilPrepagada.orderByChild("emailPerfil").equalTo(email);
        mDatabaseEPS = FirebaseDatabase.getInstance().getReference("EPSs");
        mDatabasePrepagada = FirebaseDatabase.getInstance().getReference("Prepagadas");
        mDatabaseIPS = FirebaseDatabase.getInstance().getReference("IPSs");
        mDatabaseBasic = FirebaseDatabase.getInstance().getReference("Perfiles Basicos");
        mDatabaseCalificaciones = FirebaseDatabase.getInstance().getReference("Calificaciones");

        listItemsMedicalCenters = findViewById(R.id.listViewMedicalCenters);
        adaptador = new AdapterMedicalCenters(this, listItems);
        listItemsMedicalCenters.setAdapter(adaptador);
        cargarEntidadPrepagada();

        listItemsMedicalCenters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(adapterView.getContext(), "Selecciona: "
                        + listItems.get(i).getName(), Toast.LENGTH_SHORT).show();

                webServiceObtenerRuta(latitudUser, longitudUser,
                        listItems.get(i).getLatitud(), listItems.get(i).getLongitud());

                Intent intent = new Intent(MedicalCenters.this, MapsActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("idIPS", listItems.get(i).getId());
                intent.putExtra("latitud", Double.valueOf(listItems.get(i).getLatitud()));
                intent.putExtra("longitud", Double.valueOf(listItems.get(i).getLongitud()));
                intent.putExtra("distance", distance);
                intent.putExtra("duration", duration);
                intent.putExtra("name", listItems.get(i).getName());
                startActivityForResult(intent, 0);
            }
        });

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

    }

    private void cargarEntidadPrepagada() {
        mDatabasePerfilPrepagada.orderByChild("emailPerfil").equalTo(email);
        mDatabasePerfilPrepagada.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilXPrepagadaPersistence user;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilXPrepagadaPersistence.class);
                        if (!user.getEmailPerfil().isEmpty() && user.getEmailPerfil().equals(email) && !user.getNombrePrepada().equals("Ninguna")) {
                            cargarPrepagada(user.getNombrePrepada());
                            break;
                        } else {
                            cargarEntidadEPS();
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

    private void cargarEntidadEPS() {
        mDatabasePerfilEPS.orderByChild("emailPerfil").equalTo(email);
        mDatabasePerfilEPS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilXEPSPersistence user;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilXEPSPersistence.class);
                        if (!user.getEmailPerfil().isEmpty() && user.getEmailPerfil().equals(email)) {
                            cargarEPS(user);
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

    private void cargarEPS(final PerfilXEPSPersistence user) {
        mDatabaseEPS.orderByChild("nombre").equalTo(user.getNombreEPS());
        mDatabaseEPS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    EntidadPersistence entidad;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        entidad = snapshot.getValue(EntidadPersistence.class);
                        if (!entidad.getNombre().isEmpty() && entidad.getNombre().equals(user.getNombreEPS())) {
                            mDatabaseEPS.child(snapshot.getKey()).child("Convenios").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        ReferenciaConvenioPersistence convenio;
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            convenio = snapshot.getValue(ReferenciaConvenioPersistence.class);
                                            if (!user.isPlanComplementario()) {
                                                if (!convenio.isPlanC()) {
                                                    cargarIPS(convenio.getIdIPS());
                                                }
                                            } else {
                                                cargarIPS(convenio.getIdIPS());
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cargarPrepagada(final String nPrepagada) {
        mDatabasePrepagada.orderByChild("nombre").equalTo(nPrepagada);
        mDatabasePrepagada.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    EntidadPersistence entidad;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        entidad = snapshot.getValue(EntidadPersistence.class);
                        if (!entidad.getNombre().isEmpty() && entidad.getNombre().equals(nPrepagada)) {
                            mDatabasePrepagada.child(snapshot.getKey()).child("Convenios").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        ReferenciaConvenioPersistence convenio;
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            convenio = snapshot.getValue(ReferenciaConvenioPersistence.class);
                                            cargarIPS(convenio.getIdIPS());
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cargarIPS(final String id) {
        mDatabaseIPS.orderByChild("id").equalTo(id);
        mDatabaseIPS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    IPSPersistence ips;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ips = snapshot.getValue(IPSPersistence.class);
                        if (!ips.getId().isEmpty() && ips.getId().equals(id)) {
                            String latitud = String.valueOf(ips.getLatitud());
                            String longitud = String.valueOf(ips.getLongitud());
                            EntityMedicalCenter med = new EntityMedicalCenter(ips.getNombre(), ips.getDireccion(), latitud, longitud, ips.getEdadMin(), ips.getEdadMax(), ips.getCalificacion());
                            med.setId(ips.getId());
                            verificarEdad(med, snapshot.getKey());
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

    private void verificarEdad(final EntityMedicalCenter med, final String id) {
        mDatabaseBasic.orderByChild("email").equalTo(email);
        mDatabaseBasic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PerfilBasicoPersistence user;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(PerfilBasicoPersistence.class);
                        if (!user.getEmail().isEmpty() && user.getEmail().equals(email)) {
                            int edad = Integer.parseInt(user.getAge());
                            if (edad > med.getEdadMin() && edad < med.getEdadMax()) {
                                verificarEspecialidades(med, id);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void verificarEspecialidades(final EntityMedicalCenter med, String id) {
        mDatabaseIPS.child(id).child("Especialidades").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String[] days;
                    Calendar calendar = Calendar.getInstance();
                    ArrayList<String> especialidades = new ArrayList<>();
                    ArrayList<String> copyListEsp = (ArrayList<String>) listEspecialidades.clone();
                    int day = calendar.get(Calendar.DAY_OF_WEEK);
                    String dayS = String.valueOf(day);
                    int hora = calendar.get(Calendar.HOUR_OF_DAY);
                    EspecialidadPersistence especialidad;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            especialidad = snapshot.getValue(EspecialidadPersistence.class);
                            days = especialidad.getDias().split(",");
                            List<String> listDays = new ArrayList<>(Arrays.asList(days));
                            if (listDays.contains(dayS) && hora > especialidad.gethInicio() && hora < especialidad.gethFin()) {
                                especialidades.add(especialidad.getEspecialidad());
                            }
                        }
                    }
                    if (especialidades.containsAll(copyListEsp)) {
                        System.out.println("2" + copyListEsp);
                        getDuracion(med);
                    } else {
                        copyListEsp.remove(0);
                        if (especialidades.containsAll(copyListEsp)) {
                            System.out.println("2" + copyListEsp);
                            getDuracion(med);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDuracion(final EntityMedicalCenter med) {
        final String[] url = {"https://maps.googleapis.com/maps/api/directions/json?origin=" + latitudUser + "," + longitudUser
                + "&destination=" + med.getLatitud() + "," + med.getLongitud() + "&key=AIzaSyBcbJP6b85tsc2tS5vdPnGwVnp89RQE9HY"};

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url[0], null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Este método PARSEA el JSONObject que retorna del API de Rutas de Google devolviendo
                //una lista del lista de HashMap Strings con el listado de Coordenadas de Lat y Long,
                //con la cual se podrá dibujar pollinas que describan la ruta entre 2 puntos.
                JSONArray jRoutes = null;
                JSONArray jLegs = null;
                JSONArray jSteps = null;

                try {
                    int duracionIPS = 0;
                    jRoutes = response.getJSONArray("routes");

                    /** Traversing all routes */
                    for (int i = 0; i < jRoutes.length(); i++) {
                        jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                        /** Traversing all legs */
                        for (int j = 0; j < jLegs.length(); j++) {
                            duracionIPS = (int) ((JSONObject) ((JSONObject) jLegs.get(j)).get("duration")).get("value");
                        }
                    }

                    med.setDuration(duracionIPS);
                    getCalificationUser(med);
                    //Algoritmo de alejo
                    /*listItems.add(med);
                    algortimoOrdenamientoTiempoAcceso(0, listItems.size() - 1);
                    imprimirLista();
                    algoritmoOrdenamientoPuntuacionGoogle(indexDeIpsAUnMaximoDeVeinteMins());
                    imprimirLista();*/
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se puede conectar " + error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                Log.d("ERROR: ", error.toString());
            }
        }
        );
        request.add(jsonObjectRequest);
    }

    private void getCalificationUser(final EntityMedicalCenter med) {
        mDatabaseCalificaciones.orderByChild("email").equalTo(email);
        mDatabaseCalificaciones.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean carried = false;
                    CalificacionPersistence note;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        System.out.println(snapshot.getKey());
                        if (snapshot.exists()) {
                            note = snapshot.getValue(CalificacionPersistence.class);
                            if (note.getIdIPS().equals(med.getId()) && note.getEmail().equals(email) && note.isCalificacion()) {
                                System.out.println(med.getName());
                                med.setQualificated(note.isCalificacion());
                                listItems.add(0, med);
                                adaptador.notifyDataSetChanged();
                                carried = true;
                                break;
                            }
                        }
                    }
                    if (!carried) {
                        listItems.add(med);
                        adaptador.notifyDataSetChanged();
                        algortimoOrdenamientoTiempoAcceso(indexPref(), listItems.size() - 1);
                        algoritmoOrdenamientoPuntuacionGoogle(indexDeIpsAUnMaximoDeVeinteMins());
                    }
                } else {
                    listItems.add(med);
                    adaptador.notifyDataSetChanged();
                    algortimoOrdenamientoTiempoAcceso(indexPref(), listItems.size() - 1);
                    algoritmoOrdenamientoPuntuacionGoogle(indexDeIpsAUnMaximoDeVeinteMins());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void imprimirLista() {

        System.out.println("----------------------");
        for (int i = 0; i < listItems.size(); i++) {

            System.out.println("Nombre: " + listItems.get(i).getName());
            System.out.println("                                Duracion:     " + listItems.get(i).getDuration());
            System.out.println("                                Calificación: " + listItems.get(i).getCalificacion());

        }
        System.out.println("----------------------");
    }

    private int indexPref() {
        for (int i = 0; i < listItems.size(); i++) {
            if (!listItems.get(i).isQualificated()) {
                return i;
            }
        }
        return 0;
    }

    private int indexDeIpsAUnMaximoDeVeinteMins() {

        for (int i = indexPref(); i < listItems.size(); i++) {

            if (listItems.get(i).getDuration() > (1200 + listItems.get(indexPref()).getDuration()) && i > 0) {

                return i - 1;
            } else if (listItems.get(i).getDuration() > (1200 + listItems.get(indexPref()).getDuration())) {

                return i;
            }
        }
        return 0;
    }

    private void algortimoOrdenamientoTiempoAcceso(int l, int r) {

        if (l >= r) {
            return;
        }

        // Choose pivot to be the last element in the subarray
        double pivot = listItems.get(r).getDuration();

        // Index indicating the "split" between elements smaller than pivot and
        // elements greater than pivot
        int cnt = l;

        // Traverse through array from l to r
        for (int i = l; i <= r; i++) {
            // If an element less than or equal to the pivot is found...
            if (listItems.get(i).getDuration() <= pivot) {
                // Then swap arr[cnt] and arr[i] so that the smaller element arr[i]
                // is to the left of all elements greater than pivot

                EntityMedicalCenter medCentAuxiliar = listItems.get(i);
                EntityMedicalCenter medCentAuxiliar2 = listItems.get(cnt);
                listItems.set(cnt, medCentAuxiliar);
                listItems.set(i, medCentAuxiliar2);

                // Make sure to increment cnt so we can keep track of what to swap
                // arr[i] with
                cnt++;
            }
        }

        // NOTE: cnt is currently at one plus the pivot's index
        // (Hence, the cnt-2 when recursively sorting the left side of pivot)
        algortimoOrdenamientoTiempoAcceso(l, cnt - 2); // Recursively sort the left side of pivot
        algortimoOrdenamientoTiempoAcceso(cnt, r);   // Recursively sort the right side of pivot

        adaptador.notifyDataSetChanged();
    }

    private void algoritmoOrdenamientoPuntuacionGoogle(int n) {

        for (int i = 0; i < (n); i++) {
            for (int j = 0; j < n - i; j++) {
                if (listItems.get(j).getCalificacion() + 0.8 < listItems.get(j + 1).getCalificacion()) {
                    /*
                    temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
*/
                    EntityMedicalCenter medCentAuxiliar = listItems.get(j);
                    EntityMedicalCenter medCentAuxiliar2 = listItems.get(j + 1);
                    listItems.set(j, medCentAuxiliar2);
                    listItems.set(j + 1, medCentAuxiliar);
                }
            }
        }

    }

    private void algortimoOrdenamientoPuntuacionGoogle(int l, int r) {


        if (l >= r) {
            return;
        }

        // Choose pivot to be the last element in the subarray
        double pivot = listItems.get(r).getCalificacion();
        double pivot2 = pivot + 0.8;
        boolean bandera = listItems.get(0).getCalificacion() >= (pivot + 0.8);

        // Index indicating the "split" between elements smaller than pivot and
        // elements greater than pivot
        int cnt = l;

        // Traverse through array from l to r
        for (int i = l; i <= r; i++) {
            // If an element less than or equal to the pivot is found...
            if (listItems.get(i).getCalificacion() >= (pivot)) {
                // Then swap arr[cnt] and arr[i] so that the smaller element arr[i]
                // is to the left of all elements greater than pivot

                EntityMedicalCenter medCentAuxiliar = listItems.get(i);
                EntityMedicalCenter medCentAuxiliar2 = listItems.get(cnt);
                listItems.set(cnt, medCentAuxiliar);
                listItems.set(i, medCentAuxiliar2);

                // Make sure to increment cnt so we can keep track of what to swap
                // arr[i] with
                cnt++;
            }
        }

        // NOTE: cnt is currently at one plus the pivot's index
        // (Hence, the cnt-2 when recursively sorting the left side of pivot)
        algortimoOrdenamientoPuntuacionGoogle(l, cnt - 2); // Recursively sort the left side of pivot
        algortimoOrdenamientoPuntuacionGoogle(cnt, r);   // Recursively sort the right side of pivot


        adaptador.notifyDataSetChanged();
    }

    private void webServiceObtenerRuta(String latitudInicial, String longitudInicial, String latitudFinal, String longitudFinal) {

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latitudInicial + "," + longitudInicial
                + "&destination=" + latitudFinal + "," + longitudFinal + "&key=AIzaSyBcbJP6b85tsc2tS5vdPnGwVnp89RQE9HY";

        Log.i("URL: ", url);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Este método PARSEA el JSONObject que retorna del API de Rutas de Google devolviendo
                //una lista del lista de HashMap Strings con el listado de Coordenadas de Lat y Long,
                //con la cual se podrá dibujar pollinas que describan la ruta entre 2 puntos.
                JSONArray jRoutes = null;
                JSONArray jLegs = null;
                JSONArray jSteps = null;

                try {

                    jRoutes = response.getJSONArray("routes");

                    /** Traversing all routes */
                    for (int i = 0; i < jRoutes.length(); i++) {
                        jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                        List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                        /** Traversing all legs */
                        for (int j = 0; j < jLegs.length(); j++) {
                            distance = (String) ((JSONObject) ((JSONObject) jLegs.get(j)).get("distance")).get("text");
                            duration = (String) ((JSONObject) ((JSONObject) jLegs.get(j)).get("duration")).get("text");
                            jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                            /** Traversing all steps */
                            for (int k = 0; k < jSteps.length(); k++) {
                                String polyline = "";
                                polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                                List<LatLng> list = decodePoly(polyline);

                                /** Traversing all points */
                                for (int l = 0; l < list.size(); l++) {
                                    HashMap<String, String> hm = new HashMap<String, String>();
                                    hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                                    hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                                    path.add(hm);
                                }
                            }
                            Utilidades.routes.add(path);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se puede conectar " + error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                Log.d("ERROR: ", error.toString());
            }
        }
        );

        request.add(jsonObjectRequest);
    }

    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
        //Este método PARSEA el JSONObject que retorna del API de Rutas de Google devolviendo
        //una lista del lista de HashMap Strings con el listado de Coordenadas de Lat y Long,
        //con la cual se podrá dibujar pollinas que describan la ruta entre 2 puntos.
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    distance = (String) ((JSONObject) ((JSONObject) jLegs.get(j)).get("distance")).get("text");
                    duration = (String) ((JSONObject) ((JSONObject) jLegs.get(j)).get("duration")).get("text");
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                    Utilidades.routes.add(path);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return Utilidades.routes;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

}
