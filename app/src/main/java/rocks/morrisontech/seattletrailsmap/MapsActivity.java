package rocks.morrisontech.seattletrailsmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.JsonReader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.io.IOException;

//main thread
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //JSON Node Names
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String PARK_NAME = "name";
    private static final String PARK_ID = "id";
    private static String url = "https://data.seattle.gov/resource/ybmn-w2mc.json";
    Button BtnOffLeash;
    //variable to hold Off-Leash park data
    JSONArray offLeashArray = new JSONArray();
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
        Need to change button listeners to list with checkboxes so that multiple variables can
        be queried and displayed on the map. Buttons for now to get functionality correct
         */
        //Listeners for buttons to instantiate map data
        BtnOffLeash = (Button) findViewById(R.id.offLeashButton);
        BtnOffLeash.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new OffLeashData().execute();
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
        check to very persmissions for location data
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }


    }
    /*
    implement satellite view button using this code
     */
//    public void satelliteViewButton(View view) {
//        int mapType = mMap.getMapType();
//        if (mapType == 1)
//            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        else
//            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//    }


    private class OffLeashData extends AsyncTask<String, Void, JsonReader> {


        @Override
        protected JsonReader doInBackground(String... params) {


            rocks.morrisontech.seattletrailsmap.OffLeashData parseJSON
                    = new rocks.morrisontech.seattletrailsmap.OffLeashData();

            try {
                parseJSON.getJSONFromUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JsonReader reader = null;

            return reader;
        }
    }


}
