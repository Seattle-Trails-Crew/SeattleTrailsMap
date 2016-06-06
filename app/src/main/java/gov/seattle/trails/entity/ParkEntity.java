package gov.seattle.trails.entity;

import java.util.ArrayList;

/**
 * Created by Craig Morrison on 5/16/16.
 * Each park marker calculated based on top-right to bottom-left lat/lon coordinates
 */
public class ParkEntity {

    String pma_name;
    String pmaid;
    ArrayList<TrailEntity> parkTrails = new ArrayList<>();

    public ParkEntity() {
        //default constructor
    }

    public ParkEntity(String pma_name, String pmaid) {
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

    public void parkTrails(TrailEntity trails) {

        parkTrails.add(trails);
    }

    public void dropParkMarker() {

    }




    // calculate center of all trail points and drop pin on park

    // zoom to park and display trail polylines
}
