package com.example.miguelapaez.emergenciapp;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_CHECK_SETTINGS = 0;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final int RADIUS_OF_EARTH_KM = 6371;
    private static GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private double latitudUser = 0;
    private double longitudUser = 0;
    private double altitud = 0;
    private String distance = "";
    private String duration = "";
    private double latitudMedicalCenter = 0;
    private double longitudMedicalCenter = 0;
    private String nameMedicalCenter = "";

    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_maps );

        /*
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        */


        //Recepción de datos Activity MedicalCenters
        latitudMedicalCenter = (Double) getIntent().getSerializableExtra( "latitud");
        longitudMedicalCenter = (Double) getIntent().getSerializableExtra( "longitud");
        distance = (String) getIntent().getSerializableExtra( "distance");
        duration = (String) getIntent().getSerializableExtra( "duration");
        nameMedicalCenter = (String) getIntent().getSerializableExtra( "name");

        request = Volley.newRequestQueue( getApplicationContext());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager ()
                .findFragmentById ( R.id.map );
        mapFragment.getMapAsync ( this );
        initGoogleAPIClient ();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient ( this );
        //mLocationRequest = createLocationRequest ();
        if (checkSelfPermission ( Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission ( Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission ();
        } else {
            showSettingDialog ();
        }

        Button btnAct = (Button) findViewById( R.id.buttonActMap);
        btnAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient ( v.getContext () );
                webServiceObtenerRuta(String.valueOf(latitudUser),String.valueOf(longitudUser),String.valueOf(latitudMedicalCenter),String.valueOf(longitudMedicalCenter));
            }
        });

        mFusedLocationClient.getLastLocation ().addOnSuccessListener (
                this , new OnSuccessListener <Location> () {
                    @Override
                    public void onSuccess(Location location) {
                        Log.i ( "LOCATION" , "onSuccess location" );
                        if (location != null) {
                            longitudUser = location.getLongitude ();
                            latitudUser = location.getLatitude ();
                            altitud = location.getAltitude ();
                            agregarMarcador ( latitudUser, longitudUser );
                        }
                    }
                } );
        /*
        mLocationCallback = new LocationCallback () {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation ();
                Log.i ( "LOCATION" , "Location update in the callback: " + location );
                latitudUser = location.getLatitude ();
                longitudUser = location.getLongitude ();
                altitud = location.getAltitude ();
            }
        };*/

        mapFragment = (SupportMapFragment) getSupportFragmentManager ()
                .findFragmentById ( R.id.map );
        assert mapFragment != null;
        mapFragment.getMapAsync ( this );

    }


    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale ( MapsActivity.this , android.Manifest.permission.ACCESS_FINE_LOCATION )) {
            ActivityCompat.requestPermissions ( MapsActivity.this , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION} , ACCESS_FINE_LOCATION_INTENT_ID );
        } else {
            ActivityCompat.requestPermissions ( MapsActivity.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , ACCESS_FINE_LOCATION_INTENT_ID );
        }
    }

    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create ();
        locationRequest.setPriority ( LocationRequest.PRIORITY_HIGH_ACCURACY );
        locationRequest.setInterval ( 30 * 1000 );
        locationRequest.setFastestInterval ( 5 * 1000 );
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder ()
                .addLocationRequest ( locationRequest );
        builder.setAlwaysShow ( true );

        PendingResult <LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings ( mGoogleApiClient , builder.build () );
        result.setResultCallback ( new ResultCallback <LocationSettingsResult> () {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus ();
                final LocationSettingsStates state = result.getLocationSettingsStates ();
                switch (status.getStatusCode ()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e ( "TAG" , "SUCCESS" );
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e ( "TAG" , "RESOLUTION_REQUIRED" );
                        try {
                            status.startResolutionForResult ( MapsActivity.this , REQUEST_CHECK_SETTINGS );
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace ();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e ( "TAG" , "GPS NO DISPONIBLE" );
                        break;
                }
            }
        } );
    }



    private void initGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder ( MapsActivity.this )
                .addApi ( LocationServices.API )
                .build ();
        mGoogleApiClient.connect ();
    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent data) {
        super.onActivityResult ( requestCode , resultCode , data );
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    startLocationUpdates (); //Se encendió la localización!!!
                } else {
                    Toast.makeText (
                            this ,
                            "Sin acceso a localización, hardware deshabilitado!" ,
                            Toast.LENGTH_LONG
                    ).show ();
                }
                return;
            }
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest ();
        mLocationRequest.setInterval ( 10000 ); //tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval ( 5000 ); //máxima tasa de refresco
        mLocationRequest.setPriority ( LocationRequest.PRIORITY_HIGH_ACCURACY );
        return mLocationRequest;
    }

    @Override
    protected void onResume() {
        super.onResume ();
        startLocationUpdates ();
    }

    @Override
    protected void onPause() {
        super.onPause ();
        // stopLocationUpdates ();
    }

    private void startLocationUpdates() {
        //Verificación de permiso!!
        if (ContextCompat.checkSelfPermission (
                this ,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates ( mLocationRequest , null );
        }
    }

    /*
    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates ( mLocationCallback );
    }
    */

    @Override
    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult ( requestCode , permissions , grantResults );
        Log.e ( "TAG" , "onRequestPermissionsResult" );
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                // Si se cancela la solicitud, las matrices de resultados están vacías.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Si el permiso otorgado muestra el cuadro de diálogo de ubicación si APIClient no es nulo
                    if (mGoogleApiClient == null) {
                        initGoogleAPIClient ();
                        showSettingDialog ();
                    } else
                        showSettingDialog ();


                } else {
                    Toast.makeText ( MapsActivity.this , "Location Permission denied." , Toast.LENGTH_SHORT ).show ();
                }
                return;
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));


        if (checkSelfPermission ( Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        LatLng center = null;
        ArrayList <LatLng> points = new ArrayList<LatLng>();
        PolylineOptions lineOptions = new PolylineOptions();

        // setUpMapIfNeeded();

        // recorriendo todas las rutas
        for(int i=0;i<Utilidades.routes.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Obteniendo el detalle de la ruta
            List <HashMap <String, String>> path = Utilidades.routes.get( i);

            // Obteniendo todos los puntos y/o coordenadas de la ruta
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                if (center == null) {
                    //Obtengo la 1ra coordenada para centrar el mapa en la misma.
                    center = new LatLng(lat, lng);
                }
                points.add(position);
            }

            // Agregamos todos los puntos en la ruta al objeto LineOptions
            lineOptions.addAll(points);
            //Definimos el grosor de las Polilíneas
            lineOptions.width(2);
            //Definimos el color de la Polilíneas
            lineOptions.color( Color.BLUE);
        }

        // Dibujamos las Polilineas en el Google Map para cada ruta
        mMap.addPolyline(lineOptions);

        /*
        Log.i("Latitud", Utilidades.coordenadas.getLatitudInicial().toString ());
        Log.i("Longitud", Utilidades.coordenadas.getLongitudInicial().toString ());

        LatLng origen = new LatLng(Utilidades.coordenadas.getLatitudInicial(), Utilidades.coordenadas.getLongitudInicial());
        mMap.addMarker(new MarkerOptions().position(origen).title("Lat: "+Utilidades.coordenadas.getLatitudInicial()+" - Long: "+Utilidades.coordenadas.getLongitudInicial()));

        LatLng destino = new LatLng(Utilidades.coordenadas.getLatitudFinal(), Utilidades.coordenadas.getLongitudFinal());
        mMap.addMarker(new MarkerOptions().position(destino).title("Lat: "+Utilidades.coordenadas.getLatitudFinal()+" - Long: "+Utilidades.coordenadas.getLongitudFinal()));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 15));
        /////////////
        */

        markerMedicalCenter(latitudMedicalCenter,longitudMedicalCenter,nameMedicalCenter);

    }


    public void markerMedicalCenter(double latitud, double longitud, String name){
        LatLng position = new LatLng(latitud, longitud);
        if (mMap != null) {
            MarkerOptions myMarkerOptions = new MarkerOptions();
            myMarkerOptions.position(position);
            myMarkerOptions.title(name);
            myMarkerOptions.snippet ( "Distancia: " + distance + " " + "Duración: " + duration );
            myMarkerOptions.icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_ORANGE));
            mMap.addMarker(myMarkerOptions);
            // mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( position, 15));
        }
    }

    public void agregarMarcador(double latitud, double longitud){
        LatLng posicion = new LatLng ( latitud , longitud );
        mMap.addMarker ( new MarkerOptions ().position ( posicion ).title ( "Marcador en mi posición" )
                                 .snippet("Latitud: " + latitud + " Longitud: " + longitud) //Texto de información
                                 .alpha(0.5f)
                                 .icon(BitmapDescriptorFactory
                                               .defaultMarker( BitmapDescriptorFactory.HUE_BLUE)));
        // mMap.moveCamera ( CameraUpdateFactory.newLatLng ( posicion ) );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15));
    }


    private void webServiceObtenerRuta(String latitudInicial, String longitudInicial, String latitudFinal, String longitudFinal) {

        String url="https://maps.googleapis.com/maps/api/directions/json?origin="+latitudInicial+","+longitudInicial
                +"&destination="+latitudFinal+","+longitudFinal+"&key=AIzaSyBcbJP6b85tsc2tS5vdPnGwVnp89RQE9HY";

        Log.i( "URL: ", url);

        jsonObjectRequest=new JsonObjectRequest ( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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
        Log.i( "jsonObjectRequest: ", String.valueOf ( jsonObjectRequest ) );
        request.add(jsonObjectRequest);
        ActualizarMap();
    }

    private void ActualizarMap() {

        LatLng center = null;
        ArrayList <LatLng> points = new ArrayList<LatLng>();
        PolylineOptions lineOptions = new PolylineOptions();

        // setUpMapIfNeeded();

        // recorriendo todas las rutas
        for(int z=0;z<Utilidades.routes.size();z++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Obteniendo el detalle de la ruta
            List <HashMap <String, String>> pathAux = Utilidades.routes.get(z);

            // Obteniendo todos los puntos y/o coordenadas de la ruta
            for(int y=0;y<pathAux.size();y++){
                HashMap<String,String> point = pathAux.get(y);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                if (center == null) {
                    //Obtengo la 1ra coordenada para centrar el mapa en la misma.
                    center = new LatLng(lat, lng);
                }
                points.add(position);
            }

            // Agregamos todos los puntos en la ruta al objeto LineOptions
            lineOptions.addAll(points);
            //Definimos el grosor de las Polilíneas
            lineOptions.width(2);
            //Definimos el color de la Polilíneas
            lineOptions.color( Color.BLUE);
        }

        // Dibujamos las Polilineas en el Google Map para cada ruta
        mMap.addPolyline(lineOptions);

        markerMedicalCenter(latitudMedicalCenter,longitudMedicalCenter,nameMedicalCenter);

        agregarMarcador ( latitudUser,longitudUser );

        Toast.makeText ( MapsActivity.this, "¡Mapa Actualizado!", Toast.LENGTH_SHORT).show ();

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




}
