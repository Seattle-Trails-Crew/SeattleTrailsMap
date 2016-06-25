package gov.seattle.trails.entity;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Craig Morrison on 5/16/16.
 * Park Marker coordinates calculated using LatLngBounds of all LatLng
 * coordinates of trails in each ParkEntity.
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

    /*
    drawParkTrails gets each trail's coordinates and adds them as a separate entity in trailCoordinatePoints
    todo: add marker for each trail, set data to be displayed in info window of each marker
    todo: color-code trails based on terrain and difficulty
     */
    public ArrayList<PolylineOptions> drawParkTrails() {
        ArrayList<LatLng> trailCoordinatePoints;
        ArrayList<PolylineOptions> trailLines = new ArrayList<>();
        for (TrailEntity te : parkTrails) {
            trailCoordinatePoints = te.getCoordinatePointList();
            PolylineOptions trailLine = new PolylineOptions()
                    .addAll(trailCoordinatePoints)
                    .width(3)
                    .color(Color.rgb(34, 199, 45))
                    .clickable(true)
                    .geodesic(true);
            switch (te.getSurface_ty().toLowerCase()) {
                case "grass":trailLine.color(Color.rgb(10, 138,19));
                    break;
                case "soil":trailLine.color(Color.rgb(10, 138,19));
                    break;
                case "gravel":trailLine.color(Color.rgb(7, 103, 230));
                    break;
                case "bark":trailLine.color(Color.rgb(7, 103, 230));
                    break;
                case "stairs":trailLine.color(Color.rgb(166, 36, 36));
                    break;
                case "steps":trailLine.color(Color.rgb(166, 36, 36));
                    break;
                // if none of these statements trail will be on boardwalk, asphalt, bridge, or concrete
                // and are assumed to be easy to traverse... so will default to Color.GREEN
            }
            trailLines.add(trailLine);
        }
        return trailLines;
    }

    public String getTrailData() {
        ArrayList<String> trailFeaturesList = new ArrayList<>();
        for(TrailEntity te : parkTrails) {
            String trailFeature = te.getSurface_ty();
            if(trailFeaturesList.isEmpty() || !trailFeaturesList.contains(trailFeature)) {
                trailFeaturesList.add(trailFeature);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(String trailFeature : trailFeaturesList) {
            stringBuilder.append(trailFeature);
            stringBuilder.append(" ");
        }

         return stringBuilder.toString();
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
