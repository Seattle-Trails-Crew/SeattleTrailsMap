package gov.seattle.trails.entity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Craig Morrison on 5/16/16.
 * Each park marker calculated based on top-right to bottom-left lat/lon coordinates
 */
public class ParkEntity {

    private String pma_name;
    private String pmaid;
    private ArrayList<TrailEntity> parkTrails = new ArrayList<>();
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    private LatLngBounds bounds;

    public ParkEntity() {
        //default constructor
    }

    public ParkEntity(String pmaid, String pma_name) {
        this.pma_name = pma_name;
        this.pmaid = pmaid;
    }


    public String getPma_name() {

        return pma_name;
    }

    public void setPma_name(String pma_name) {

        this.pma_name = pma_name;
    }

    public String getPmaid() {

        return pmaid;
    }

    public void setPmaid(String pmaid) {

        this.pmaid = pmaid;
    }

    public void addParkTrails(TrailEntity trails) {
        //add TrailEntity to ParkEntity
        parkTrails.add(trails);
    }

    public ArrayList<TrailEntity> getParkTrails() {
        return parkTrails;
    }

    public void setBounds(ArrayList<LatLng> coordinates) {
        //set bounds for all trails in park
        for (LatLng singleCoordinate : coordinates) {
            builder.include(singleCoordinate);
        }
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public LatLng getParkCenter() {
        //get center of bounds for all trails in park
        bounds = builder.build();
        LatLng centerCoordinate = bounds.getCenter();
        return centerCoordinate;
    }


    // calculate center of all trail points and drop pin on park

    // zoom to park and display trail polylines
}
