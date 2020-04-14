package com.example.miguelapaez.emergenciapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.miguelapaez.emergenciapp.Entities.Especialidad;
import com.example.miguelapaez.emergenciapp.Entities.PerfilBasico;
import com.example.miguelapaez.emergenciapp.Entities.PerfilXEPS;
import com.example.miguelapaez.emergenciapp.Entities.PerfilxPrepagada;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicalCenters extends AppCompatActivity {

    TextView message;

    private FusedLocationProviderClient mFusedLocationClient;
    private double latitudUser = 0;
    private double longitudUser = 0;
    private String distance = "";
    private String duration = "";

    ListView listItemsMedicalCenters;
    private AdapterMedicalCenters adaptador;
    ArrayList<EntityMedicalCenter> listItems = new ArrayList<>();


    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_medical_centers );
        getSupportActionBar ().hide ();

        request = Volley.newRequestQueue( getApplicationContext());

        message = (TextView) findViewById ( R.id.textMessageMedicalCenters );
        String font_path = "font/Arvo-Regular.ttf";
        Typeface TF = Typeface.createFromAsset ( getAssets () , font_path );
        message.setTypeface ( TF );

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient ( this );

        cargarLista();
        listItemsMedicalCenters = findViewById(R.id.listViewMedicalCenters);
        adaptador = new AdapterMedicalCenters ( this, listItems);
        listItemsMedicalCenters.setAdapter(adaptador);

        listItemsMedicalCenters.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView <?> adapterView , View view , int i , long l) {
                Toast.makeText ( adapterView.getContext (), "Selecciona: "
                        +listItems.get(i).getName (), Toast.LENGTH_SHORT).show ();

                String latUser = String.valueOf(latitudUser);
                String lonUser = String.valueOf(longitudUser);

                webServiceObtenerRuta ( latUser , lonUser ,
                                        listItems.get(i).getLatitud () , listItems.get(i).getLongitud () );

                Intent intent = new Intent ( MedicalCenters.this , MapsActivity.class );
                intent.putExtra ( "latitud" , Double.valueOf ( listItems.get(i).getLatitud () ) );
                intent.putExtra ( "longitud" , Double.valueOf ( listItems.get(i).getLongitud () ) );
                intent.putExtra ( "distance", distance );
                intent.putExtra ( "duration", duration );
                intent.putExtra ( "name" , listItems.get(i).getName () );
                startActivityForResult ( intent , 0 );
            }
        } );

        if (checkSelfPermission ( Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission ( Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation ().addOnSuccessListener (
                this , new OnSuccessListener <Location> () {
                    @Override
                    public void onSuccess(Location location) {
                        Log.i ( "LOCATION" , "onSuccess location" );
                        if (location != null) {
                            longitudUser = location.getLongitude ();
                            latitudUser = location.getLatitude ();
                        }
                    }
                } );


    }

    private void cargarLista(){
        listItems.add(new EntityMedicalCenter ( "Hospital San Ignacio", "Cra. 7 No. 40-62", "4.628551", "-74.064039" , 0, 100));
        listItems.add(new EntityMedicalCenter ( "Clinica Marly", "Cl. 50 No. 9-67", "4.636828", "-74.065194", 0, 100 ));
        listItems.add(new EntityMedicalCenter ( "Hospital Militar", "Tv. 3 # 49-02", "4.635096", "-74.062111", 0, 100 ));
        listItems.add(new EntityMedicalCenter ( "Clinica El Country", "Cra. 16 #82-95", "4.669296", "-74.057093", 0, 100 ));
    }

    private void webServiceObtenerRuta(String latitudInicial, String longitudInicial, String latitudFinal, String longitudFinal) {

        String url="https://maps.googleapis.com/maps/api/directions/json?origin="+latitudInicial+","+longitudInicial
                +"&destination="+latitudFinal+","+longitudFinal+"&key=AIzaSyBcbJP6b85tsc2tS5vdPnGwVnp89RQE9HY";

        Log.i( "URL: ", url);

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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
                    for(int i=0;i<jRoutes.length();i++){
                        jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                        List<HashMap<String, String>> path = new ArrayList <HashMap<String, String>> ();

                        /** Traversing all legs */
                        for(int j=0;j<jLegs.length();j++){
                            distance = (String)((JSONObject)((JSONObject)jLegs.get(j)).get("distance")).get("text");
                            duration = (String)((JSONObject)((JSONObject)jLegs.get(j)).get("duration")).get("text");
                            jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                            /** Traversing all steps */
                            for(int k=0;k<jSteps.length();k++){
                                String polyline = "";
                                polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                                List <LatLng> list = decodePoly( polyline);

                                /** Traversing all points */
                                for(int l=0;l<list.size();l++){
                                    HashMap <String, String> hm = new HashMap<String, String>();
                                    hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                                    hm.put("lng", Double.toString(((LatLng)list.get( l)).longitude) );
                                    path.add(hm);
                                }
                            }
                            Utilidades.routes.add(path);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                Log.d( "ERROR: ", error.toString());
            }
        }
        );

        request.add(jsonObjectRequest);
    }

    public List<List<HashMap<String,String>>> parse(JSONObject jObject){
        //Este método PARSEA el JSONObject que retorna del API de Rutas de Google devolviendo
        //una lista del lista de HashMap Strings con el listado de Coordenadas de Lat y Long,
        //con la cual se podrá dibujar pollinas que describan la ruta entre 2 puntos.
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    distance = (String)((JSONObject)((JSONObject)jLegs.get(j)).get("distance")).get("text");
                    duration = (String)((JSONObject)((JSONObject)jLegs.get(j)).get("duration")).get("text");
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    Utilidades.routes.add(path);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<EntityMedicalCenter> filtradoDeHospitales(ArrayList<EntityMedicalCenter> IPSCandidatas, ArrayList<Especialidad> especialistasNecesarios, PerfilXEPS perfilEps, PerfilxPrepagada perfilPrepagada, PerfilBasico perfilBasico){


        ArrayList<EntityMedicalCenter> IPSConConveniosMedicos = new ArrayList<>();
        ArrayList<EntityMedicalCenter> IPSConConveniosMedicoPrepagada = new ArrayList<>();
        ArrayList<EntityMedicalCenter> IPSConConveniosMedicoEPS = new ArrayList<>();
        ArrayList<EntityMedicalCenter> IPSQueAtiendanPorLaEdad = new ArrayList<>();
        ArrayList<EntityMedicalCenter> IPSConEspecialistasNecesarios = new ArrayList<>();
        ArrayList<EntityMedicalCenter> IPSDescartadas = new ArrayList<>();

        IPSConConveniosMedicoPrepagada = filtradoPrepagada(IPSCandidatas, perfilPrepagada);

        if(IPSConConveniosMedicoPrepagada.size() == 0){
            IPSConConveniosMedicoEPS = filtradoEPS(IPSCandidatas, perfilEps);
            IPSConConveniosMedicos = IPSConConveniosMedicoEPS;
        }else{
            IPSConConveniosMedicos = IPSConConveniosMedicoPrepagada;
        }


        for(int i = 0; i < IPSConConveniosMedicos.size(); i++){

            if(IPSQueAtiendanPorLaEdad.get(i).AtiendeSegunLaEdad(perfilBasico.getAge2())){

                IPSQueAtiendanPorLaEdad.add(IPSConConveniosMedicos.get(i));
            }
        }

        if(IPSQueAtiendanPorLaEdad.isEmpty()){

            IPSQueAtiendanPorLaEdad = IPSConConveniosMedicos;
        }

        int max = 0;
        for(int i = 0; i < IPSQueAtiendanPorLaEdad.size(); i++){
            if(max <= IPSQueAtiendanPorLaEdad.get(i).especialidadesDisponibles(especialistasNecesarios)){
                max = IPSQueAtiendanPorLaEdad.get(i).especialidadesDisponibles(especialistasNecesarios);
            }
        }

        if(max > 0) {
            while (true) {
                for (int i = 0; i < IPSQueAtiendanPorLaEdad.size(); i++) {

                    if (IPSQueAtiendanPorLaEdad.get(i).especialidadesDisponibles(especialistasNecesarios) == max) {

                        IPSConEspecialistasNecesarios.add(IPSQueAtiendanPorLaEdad.get(i));
                    }
                }
                max--;
                if (max <= 0) {
                    break;
                }
            }
        }

        if(IPSConEspecialistasNecesarios.isEmpty()){

            IPSConEspecialistasNecesarios = IPSQueAtiendanPorLaEdad;
        }

        int n = IPSConEspecialistasNecesarios.size() / 2;
        ordenamientoDeIPSSegunTiempoAcceso(IPSConEspecialistasNecesarios, 0, n- 1);
        ordenamientoDeIPSSegunPercepcion(IPSConEspecialistasNecesarios, 0, obtenerIndiceIPSMasLejana(IPSConEspecialistasNecesarios));

        return IPSConEspecialistasNecesarios;
    }

    private void ordenamientoDeIPSSegunTiempoAcceso(ArrayList<EntityMedicalCenter> IPSCandidatas, int l, int r){

        if( l >= r){
            return;
        }

        int pivot = CalcularDistanciasEneGoogleMaps(IPSCandidatas.get(r));
        int cnt = l;

        for (int i = l; i <= r; i++)
        {
            if (CalcularDistanciasEneGoogleMaps(IPSCandidatas.get(i)) <= pivot)
            {
                swap(IPSCandidatas,cnt, i);
                cnt++;
            }
        }


        ordenamientoDeIPSSegunTiempoAcceso(IPSCandidatas, l, cnt-2);
        ordenamientoDeIPSSegunTiempoAcceso(IPSCandidatas, cnt, r);

    }

    private void swap(ArrayList<EntityMedicalCenter> IPSCandidatas ,int a, int b)
    {
        EntityMedicalCenter temp = IPSCandidatas.get(a);
        IPSCandidatas.set(a, IPSCandidatas.get(b));
        IPSCandidatas.set(b, temp);

    }

    private void ordenamientoDeIPSSegunPercepcion(ArrayList<EntityMedicalCenter> IPSCandidatas, int l, int r){

        if( l >= r){
            return;
        }

        float pivot = CalcularPuntuacionesEnGoogleMaps(IPSCandidatas.get(r));
        int cnt = l;

        for (int i = l; i <= r; i++)
        {
            if (CalcularPuntuacionesEnGoogleMaps(IPSCandidatas.get(i)) > pivot + 1)
            {
                swap(IPSCandidatas,cnt, i);
                cnt++;
            }
        }


        ordenamientoDeIPSSegunPercepcion(IPSCandidatas, l, cnt-2);
        ordenamientoDeIPSSegunPercepcion(IPSCandidatas, cnt, r);


    }

    private int obtenerIndiceIPSMasLejana(ArrayList<EntityMedicalCenter> IPSCandidatas){

        int n = 0;
        for(int i = 0; i < IPSCandidatas.size(); i++){
            if(CalcularDistanciasEneGoogleMaps(IPSCandidatas.get(i)) <= 20){
                n++;
            }else{
                return n;
            }

        }
        return n;
    }

    private int CalcularDistanciasEneGoogleMaps(EntityMedicalCenter entityMedicalCenter) {

        return 10;
    }

    private float CalcularPuntuacionesEnGoogleMaps(EntityMedicalCenter entityMedicalCenter) {

        return (float)3.1;
    }


    public ArrayList filtradoPrepagada(ArrayList<EntityMedicalCenter> IPSCandidatas, PerfilxPrepagada perfilPrepagada){

        ArrayList<EntityMedicalCenter> IPSConConveniosMedicoPrepagada = new ArrayList<>();
        for(int i = 0; i < IPSCandidatas.size(); i++){

            if(IPSCandidatas.get(i).TieneConvenio(perfilPrepagada.getNombre())){

                IPSConConveniosMedicoPrepagada.add(IPSCandidatas.get(i));
            }
        }
        return IPSConConveniosMedicoPrepagada;

    }

    public ArrayList filtradoEPS(ArrayList<EntityMedicalCenter> IPSCandidatas,  PerfilXEPS perfilEps){

        ArrayList<EntityMedicalCenter> IPSConConveniosMedicoEPS = new ArrayList<>();
        for(int i = 0; i < IPSCandidatas.size(); i++){

            if(IPSCandidatas.get(i).TieneConvenio(perfilEps.getNombreEPS())){

                IPSConConveniosMedicoEPS.add(IPSCandidatas.get(i));
            }
        }

        if(IPSConConveniosMedicoEPS.isEmpty()){

            IPSConConveniosMedicoEPS = IPSCandidatas;
        }
        return IPSConConveniosMedicoEPS;
    }

    public void concatenadorDeListasDeIPS(ArrayList<EntityMedicalCenter> IPSMasAdecuadas, ArrayList<EntityMedicalCenter> IPSSinEspecialistasNecesarios,
                                          ArrayList<EntityMedicalCenter> IPSQueNoAtiendeSegunLaEdad, ArrayList<EntityMedicalCenter> IPSSinSeguros){

    }

    }
