package gov.seattle.trails;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import gov.seattle.trails.entity.TrailEntity;

//main thread
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //JSON Node Names
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String PARK_NAME = "name";
    private static final String PARK_ID = "id";
    Button BtnOffLeash;
    //variable to hold Off-Leash park data
    JSONArray offLeashArray = new JSONArray();
    private GoogleMap mMap;

    private final int PERMISSION_REQUEST_LOCATION_SERVICE = 100;

    //instantiate app with Map Fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*
        TODO: Create list for various park features (Trails, Off-Leash)
         */
        //Listeners for buttons to instantiate map data
        BtnOffLeash = (Button) findViewById(R.id.offLeashButton);
        BtnOffLeash.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new GetTrailData().execute();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * Map defaults to Seattle area.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment.
     * This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //initialize map view to greater Seattle area
        LatLng seattle = new LatLng(47.609895, -122.330259);
        //mMap.addMarker(new MarkerOptions().position(seattle).title("Marker in Seattle"));
        CameraUpdate panToSeattle = CameraUpdateFactory.newLatLng(seattle);
        mMap.moveCamera(panToSeattle);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        askForLocationPermissionIfNeeded();
    }

    /*
     check to very persmissions for location data
    */
    public void askForLocationPermissionIfNeeded(){

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_LOCATION_SERVICE);

                // PERMISSION_REQUEST_LOCATION_SERVICE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else{
            //Permission already enabled
            mMap.setMyLocationEnabled(true);
            askUserToEnableLocationIfNeeded();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION_SERVICE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location task you need to do.
                    try {
                        mMap.setMyLocationEnabled(true);
                        askUserToEnableLocationIfNeeded();
                    } catch (SecurityException e) {
                        //Had an issue with security? oops!
                        e.printStackTrace();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void askUserToEnableLocationIfNeeded(){
        if (!TheApplication.isLocationServiceEnabled(this)) {
            // Ask user if they would like to go and enabled settings
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            takeUserToLocationSettings();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked

                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Would you like to enabled location services?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }

    public void takeUserToLocationSettings(){
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }

    /*
    TODO: implement satellite view button
     */
//    public void satelliteViewButton(View view) {
//        int mapType = mMap.getMapType();
//        if (mapType == 1)
//            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        else
//            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//    }

    //TODO: Study AsyncTask - find what gets passed into it and how it works
    private class GetTrailData extends AsyncTask<String, Void, String> {

        StringBuilder jString = new StringBuilder();
        //TODO create onPreExecute() to load a progress bar of some sort

        @Override
        protected String doInBackground(String... url) {

            try {
                //open connection
                //URL offLeashURL = new URL("https://data.seattle.gov/resource/ybmn-w2mc.json");
                URL serviceUrl = new URL(TheApplication.ServiceUrl);
                HttpsURLConnection con = (HttpsURLConnection) serviceUrl.openConnection();
                InputStream ins = con.getInputStream();
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    jString.append(inputLine);
                }

                //display JSON string in logcat for verification that download is successful
                Log.i("JSON", jString.toString());

                //close connection
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return jString.toString();
        }

        protected void onPostExecute(String dataString) {
            Gson gson = new Gson();
            TrailEntity[] data = gson.fromJson(dataString, TrailEntity[].class);

            for(TrailEntity trail : data) {
                //LatLng mark = new LatLng(park.getLatitude(), park.getLongitude());
                Log.i("TrailName", trail.getPma_name());
                Log.i("Canopy", trail.getCanopy());
                trail.getThe_geom();
                //mMap.addMarker(new MarkerOptions().position(mark).title(park.getName()));
                //TODO SET BREAK POINT HERE AND DEBUG SO YOU CAN SEE\/ THE DATA FOR  trail 

            }
        }
    }


}
