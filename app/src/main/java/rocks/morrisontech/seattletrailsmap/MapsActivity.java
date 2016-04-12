package rocks.morrisontech.seattletrailsmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.JsonReader;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

//main thread
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    Button BtnOffLeash;

    private GoogleMap mMap;

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
        TODO: Create list for park features (Trails, Off-Leash)
         */
        //Listeners for buttons to instantiate map data
        BtnOffLeash = (Button) findViewById(R.id.offLeashButton);
        BtnOffLeash.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new GetOffLeashData().execute();
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
        /*
        check to verify permissions for location data
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
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
    private class GetOffLeashData extends AsyncTask<String, Void, String> {

        StringBuilder jString = new StringBuilder();
        //TODO create onPreExecute() to load a progress bar of some sort

        @Override
        protected String doInBackground(String... url) {

            try {
                //open connection
                URL offLeashURL = new URL("https://data.seattle.gov/resource/ybmn-w2mc.json");
                HttpsURLConnection con = (HttpsURLConnection) offLeashURL.openConnection();
                InputStream ins = con.getInputStream();
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    jString.append(inputLine);
                }

                //display JSON string in logcat for verification that download is successful
                Log.i("OFD", jString.toString());

                //close connection
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return jString.toString();
        }

        protected void onPostExecute(String dataString) {
            Gson gson = new Gson();
            OffLeashData[] ofd = gson.fromJson(dataString, OffLeashData[].class);
            for(OffLeashData park : ofd) {
                LatLng mark = new LatLng(park.getLatitude(), park.getLongitude());

                mMap.addMarker(new MarkerOptions().position(mark).title(park.getName()));
            }
        }
    }


}
