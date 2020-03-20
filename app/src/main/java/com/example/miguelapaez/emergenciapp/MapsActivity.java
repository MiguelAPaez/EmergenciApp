package com.example.miguelapaez.emergenciapp;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
    private double latitudMedicalCenter = 0;
    private double longitudMedicalCenter = 0;
    private String nameMedicalCenter = "";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_maps );

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*
        //Recepción de datos Activity MedicalCenters
        latitudMedicalCenter = (Double) getIntent().getSerializableExtra( "latitud");
        longitudMedicalCenter = (Double) getIntent().getSerializableExtra( "longitud");
        nameMedicalCenter = (String) getIntent().getSerializableExtra( "name");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager ()
                .findFragmentById ( R.id.map );
        mapFragment.getMapAsync ( this );
        initGoogleAPIClient ();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient ( this );
        mLocationRequest = createLocationRequest ();
        if (checkSelfPermission ( Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission ( Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission ();
        } else {
            showSettingDialog ();
        }

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
        mLocationCallback = new LocationCallback () {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation ();
                Log.i ( "LOCATION" , "Location update in the callback: " + location );
                latitudUser = location.getLatitude ();
                longitudUser = location.getLongitude ();
                altitud = location.getAltitude ();
            }
        };

        mapFragment = (SupportMapFragment) getSupportFragmentManager ()
                .findFragmentById ( R.id.map );
        assert mapFragment != null;
        mapFragment.getMapAsync ( this );
        */
    }

    /*
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
        stopLocationUpdates ();
    }

    private void startLocationUpdates() {
        //Verificación de permiso!!
        if (ContextCompat.checkSelfPermission (
                this ,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates ( mLocationRequest , mLocationCallback , null );
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates ( mLocationCallback );
    }

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
    */

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

        /*
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

        markerMedicalCenter(latitudMedicalCenter,longitudMedicalCenter,nameMedicalCenter);
        */

        LatLng center = null;
        ArrayList<LatLng> points = new ArrayList<LatLng>();
        PolylineOptions lineOptions = new PolylineOptions();

        // setUpMapIfNeeded();

        // recorriendo todas las rutas
        for(int i=0;i<Utilidades.routes.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Obteniendo el detalle de la ruta
            List<HashMap<String, String>> path = Utilidades.routes.get(i);

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
            lineOptions.color(Color.BLUE);
        }

        // Dibujamos las Polilineas en el Google Map para cada ruta
        mMap.addPolyline(lineOptions);

        LatLng origen = new LatLng(Utilidades.coordenadas.getLatitudInicial(), Utilidades.coordenadas.getLongitudInicial());
        mMap.addMarker(new MarkerOptions().position(origen).title("Lat: "+Utilidades.coordenadas.getLatitudInicial()+" - Long: "+Utilidades.coordenadas.getLongitudInicial()));

        LatLng destino = new LatLng(Utilidades.coordenadas.getLatitudFinal(), Utilidades.coordenadas.getLongitudFinal());
        mMap.addMarker(new MarkerOptions().position(destino).title("Lat: "+Utilidades.coordenadas.getLatitudFinal()+" - Long: "+Utilidades.coordenadas.getLongitudFinal()));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 15));
        /////////////


    }

    /*

    public void markerMedicalCenter(double latitud, double longitud, String name){
        LatLng position = new LatLng(latitud, longitud);
        if (mMap != null) {
            MarkerOptions myMarkerOptions = new MarkerOptions();
            myMarkerOptions.position(position);
            myMarkerOptions.title(name);
            double result = distance ( latitudUser,longitudUser, latitud, longitud);
            myMarkerOptions.snippet ( "Distancia: " + result + " km" );
            myMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            mMap.addMarker(myMarkerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
        }
    }

    public void agregarMarcador(double latitud, double longitud){
        LatLng posicion = new LatLng ( latitud , longitud );
        mMap.addMarker ( new MarkerOptions ().position ( posicion ).title ( "Marcador en mi posición" )
                                 .snippet("Latitud: " + latitud + " Longitud: " + longitud) //Texto de información
                                 .alpha(0.5f)
                                 .icon(BitmapDescriptorFactory
                                               .defaultMarker( BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera ( CameraUpdateFactory.newLatLng ( posicion ) );
        mMap.moveCamera ( CameraUpdateFactory.zoomTo ( 15 ) );
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double sindLat = Math.sin(latDistance / 2);
        double sindLng = Math.sin(lngDistance / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double result = RADIUS_OF_EARTH_KM * va2;
        return Math.round(result*100.0)/100.0;
    }
    */

}
