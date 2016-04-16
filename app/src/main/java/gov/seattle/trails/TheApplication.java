package gov.seattle.trails;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;

/**
 * Created by randy.thedford on 4/10/16.
 */
public class TheApplication extends Application {


    public static boolean isLocationServiceEnabled(Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {

        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {

        }

        return gps_enabled || network_enabled;

    }
}
