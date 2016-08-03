package gov.seattle.trails;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import gov.seattle.trails.entity.GeoPathEntity;
import gov.seattle.trails.entity.ParkEntity;
import gov.seattle.trails.entity.TrailEntity;

//main thread
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private final int PERMISSION_REQUEST_LOCATION_SERVICE = 100;
    private Toolbar toolbar;
    private FloatingActionButton satelliteButton;
    private FloatingActionButton navigationButton;
    private HashMap<String, ParkEntity> parkEntityHashMap = new HashMap<>();
    private HashMap<String, Marker> markerHashMap = new HashMap<>();
    private HashMap<String, String> markerIdPmaidHashMap = new HashMap<>();
    private HashMap<Integer, Polyline> polyLineHashMap = new HashMap<>();
    private ArrayList<Polyline> currentPolylinesArrayList = new ArrayList<>();
    private Uri selectedMarkerData;

    //instantiate app with Map Fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        setupToolbar();
        setupButtons();

        if (isConnectedToInternet() && parkEntityHashMap.isEmpty()) {
            new GetTrailData().execute();
        } else {
            networkConnectionDialog();
        }
    }

    public void networkConnectionDialog() {
        DialogFragment settingsDialog = NoInternetDialogFragment.newInstance(R.string.no_internet_dialog);
        settingsDialog.show(getFragmentManager(), "message");
    }

    public boolean isConnectedToInternet() {

        boolean isConnected = false;

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            isConnected = true;
        }

        return isConnected;
    }


    public void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(R.id.maps_toolbar);
        setSupportActionBar(this.toolbar);
    }

    public void setupButtons() {
        satelliteButton = (FloatingActionButton) findViewById(R.id.satellite_fab);
        navigationButton = (FloatingActionButton) findViewById(R.id.navigation_fab);

        OnClickListener fabListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.satellite_fab:
                        int mapType = mMap.getMapType();
                        if (mapType == 1) {
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            satelliteButton.setImageResource(R.drawable.ic_map_black_24px);
                        } else {
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            satelliteButton.setImageResource(R.drawable.ic_satellite_black_24px);
                        }
                        break;
                    case R.id.navigation_fab:
                        //TODO: set intent to open maps app with direction from current location
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, selectedMarkerData);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                        break;
                }
            }
        };
        satelliteButton.setOnClickListener(fabListener);
        navigationButton.setOnClickListener(fabListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        //TODO: Set menu items to change Map View Type and select some specific trail features

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.search_hint));
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("on text change text: " + newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("on query submit: " + query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isConnectedToInternet()) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.no_internet_toast, Toast.LENGTH_LONG);
            toast.show();
        } else if (isConnectedToInternet() && parkEntityHashMap.isEmpty()) {
            new GetTrailData().execute();
        }
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
        LatLng seattle = new LatLng(47.6062, -122.3321);
        //mMap.addMarker(new MarkerOptions().position(seattle).title("Marker in Seattle"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seattle, 11.2f));

        askForLocationPermissionIfNeeded();
    }


    /*
     check to very permissions for location data
    */
    public void askForLocationPermissionIfNeeded() {

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result != PackageManager.PERMISSION_GRANTED) {

            // TODO: Show reason to request location permissions
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
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
        } else {
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

            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void askUserToEnableLocationIfNeeded() {
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

    public void takeUserToLocationSettings() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private class GetTrailData extends AsyncTask<String, Void, String> {

        StringBuilder jString = new StringBuilder();
        //TODO create onPreExecute() to load a progress bar of some sort

        @Override
        protected String doInBackground(String... url) {

            //connect and download data
            try {
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

        //draw polylines and drop markers
        protected void onPostExecute(String dataString) {
            Gson gson = new Gson();

            TrailEntity[] trailEntities = gson.fromJson(dataString, TrailEntity[].class);
            //store each group of trails in a single park entity

            if (trailEntities != null) {

                //each trail represents just one of a group of trails for each park
                for (TrailEntity trail : trailEntities) {

                    // add park to ArrayList<ParkEntity> if it hasn't already been created (based on pmaid value)
                    // create int value of pmaid for hashCode

                    //coordinatePointsList stores each point of this trail
                    ArrayList<LatLng> coordinatePointsList = new ArrayList<>();

                    //get all points to draw polyline for this trail
                    GeoPathEntity geoData = trail.getThe_geom();

                    if (geoData != null) {
                        List<float[]> coordinateArray = geoData.getCoordinates();

                        //array to hold each coordinate
                        float[] point;

                        for (int i = 0; i < coordinateArray.size(); i++) {
                            //assign latitude and longitude values from array list of arrays
                            point = coordinateArray.get(i);
                            if (point != null && point.length == 2) {
                                // Socrata trailEntities downloads with longitude at index 0
                                float lat = point[1];
                                float lng = point[0];
                                LatLng pointCoordinate = new LatLng(lat, lng);
                                coordinatePointsList.add(pointCoordinate);
                            }
                        }
                    }
                    trail.setCoordinatePointList(coordinatePointsList);

                    //use this instance of ParkEntity to create a new ParkEntity object in HashMap
                    if (parkEntityHashMap.isEmpty() || !parkEntityHashMap.containsKey(trail.getPmaid())) {
                        ParkEntity pe = new ParkEntity(trail.getPmaid(), trail.getPma_name());
                        parkEntityHashMap.put(trail.getPmaid(), pe);
                        //add first trail that occurs for this pmaid
                        pe.addParkTrails(trail);
                        pe.setBounds(coordinatePointsList);
                    } else {
                        //add remaining trails to the same pmaid
                        ParkEntity pe = parkEntityHashMap.get(trail.getPmaid());
                        pe.addParkTrails(trail);
                        pe.setBounds(coordinatePointsList);
                    }


                }
            }// end trails loop

            /*
                Iterate through HashMap to place markers at the center of each park or set of trails
             */
            for (Object o : parkEntityHashMap.entrySet()) {
                // get next key/value mapping in parkEntityHashMap
                Map.Entry pair = (Map.Entry) o;
                // get that instance of ParkEntity using key (park name)
                ParkEntity pe = parkEntityHashMap.get(pair.getKey());
                // get center coordinate for park
                LatLng parkCenterCoordinate = pe.getParkCenter();

                // add park marker
                Marker parkCenterMarker = mMap.addMarker(new MarkerOptions()
                        .position(parkCenterCoordinate)
                        .title(pe.getPma_name()));
                // add marker to markerHashMap to reference in ClickListener
                markerHashMap.put(parkCenterMarker.getId(), parkCenterMarker);
                markerIdPmaidHashMap.put(parkCenterMarker.getId(), pe.getPmaid());
            }

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    ParkEntity pe = parkEntityHashMap.get(markerIdPmaidHashMap.get(marker.getId()));

                    //set text view formatter for info window contents
                    View markerView = getLayoutInflater().inflate(R.layout.marker_info_window_layout, null);
                    marker = markerHashMap.get(marker.getId());

                    TextView markerTitleText = (TextView) markerView.findViewById(R.id.marker_label);
                    markerTitleText.setText(marker.getTitle());

                    TextView markerInfoText = (TextView) markerView.findViewById(R.id.marker_info_text);
                    markerInfoText.setText(pe.getTrailData(), TextView.BufferType.SPANNABLE);

                    return markerView;
                }
            });

            GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Marker selectedMarker = markerHashMap.get(marker.getId());
                    // get ParkEntity object by getting pmaid value for selected pin using pin's id value
                    ParkEntity pe = parkEntityHashMap.get(markerIdPmaidHashMap.get(selectedMarker.getId()));

                    if (!currentPolylinesArrayList.isEmpty()) {
                        for (Polyline polyline : currentPolylinesArrayList) {
                            polyline.remove();
                        }
                    }

                    // retrieve all trail objects for the selected park
                    // zoom to park
                    if (marker.equals(selectedMarker)) {

                        // clear existing polylines
                        for (Polyline polyline : currentPolylinesArrayList) {
                            polyline.remove();
                        }

                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(pe.getBounds(), 50));
                        selectedMarker.showInfoWindow();

                        for (PolylineOptions polylineOptions : pe.drawParkTrails()) {
                            Polyline trailLine = mMap.addPolyline(polylineOptions);
                            polyLineHashMap.put(trailLine.hashCode(), trailLine);
                            // add polyline to global array list to use in OnMapClickListener
                            // to clear lines when map is clicked
                            currentPolylinesArrayList.add(trailLine);
                        }

                        selectedMarkerData = Uri.parse("geo:" + selectedMarker.getPosition() + "?q=" + selectedMarker.getTitle());

                    }
                    return true;
                }
            };
            mMap.setOnMarkerClickListener(markerClickListener);

            final GoogleMap.OnPolylineClickListener polylineClickListener = new GoogleMap.OnPolylineClickListener() {
                @Override
                public void onPolylineClick(Polyline polyline) {
                    polyline.setWidth(10);
                }
            };
            mMap.setOnPolylineClickListener(polylineClickListener);
        }
    }
}


