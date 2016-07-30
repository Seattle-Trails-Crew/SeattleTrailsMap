package gov.seattle.trails;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;

/**
 * Created by randy.thedford on 4/10/16.
 */
public class TheApplication extends Application {

    public static String ServiceUrl = "https://data.seattle.gov/resource/vwtx-gvpm.json?$limit=999999999&$$app_token=o9zqUXd72sDpc0BWNR45Fc1TH";

    public static boolean isLocationServiceEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {

        }

        return gps_enabled || network_enabled;

    }
}
