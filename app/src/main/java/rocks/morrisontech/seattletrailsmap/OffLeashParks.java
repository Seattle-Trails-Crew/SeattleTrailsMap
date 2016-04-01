package rocks.morrisontech.seattletrailsmap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Quinn on 3/23/16.
 */
public class OffLeashParks {

    //Connect to server and get data
    public static void main(String[] args) throws Exception {

        String socAppToken = "ZSFz0bJfTTnGi8aQxJJLQ9TuB"; //find where to pass this to Socrata for authorization
        String httpsURL = "https://data.seattle.gov/resource/ybmn-w2mc.json";

        URL offLeashURL = new URL(httpsURL);
        HttpsURLConnection con = (HttpsURLConnection) offLeashURL.openConnection();
        InputStream ins = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);

        String inputLine;

        /*
        Create ArrayList of off-leash parks with data from Socrata
        ArrayList allows flexibility of size without reprogramming when the number of objects changes
         */
        ArrayList<OffLeashData> parkData = new ArrayList<>();


        while ((inputLine = in.readLine()) != null) {

            //write each line into an OffLeashData object
            System.out.println(inputLine);

            //parse data from connection into OffLeashData objects
            }

        //close data connection once all park objects are instantiated
        in.close();
    }

    //class used in Array... all relevant park data will be stored in these objects
    public class OffLeashData {

        double latitude, longitude;
        String parkName;

        public OffLeashData(double lat, double lon, String name)
        {
            latitude = lat;
            longitude = lon;
            parkName = name;
        }
    }

    public static void getItems()
    {
        String parkName;
        double latitude, longitude;
    }

}
