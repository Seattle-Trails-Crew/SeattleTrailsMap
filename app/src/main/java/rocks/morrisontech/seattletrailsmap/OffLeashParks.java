package rocks.morrisontech.seattletrailsmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Quinn on 3/23/16.
 */
public class OffLeashParks {

    //Connect to server and get data
    public static void main(String[] args) throws Exception {

        //do stuff here


    }

    /*
    Use static block initializer to download data immediately (and only once) when this class is called
    This reduces the number of times a data connection is opened
     */

    static {
        double latitude, longitude;
        int id, pmaid;
        String name;
        long point_x, point_y;

        String socAppToken = "ZSFz0bJfTTnGi8aQxJJLQ9TuB";
        String httpsURL = "https://data.seattle.gov/resource/ybmn-w2mc.json";

        /*
        initiate and connect to the above URL
        appToken gives us permission to download data from Socrata
         */

        //find out where to pass app token to socrata, if I even do
        URL offLeashURL = null;
        try {
            offLeashURL = new URL(httpsURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) offLeashURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream ins = null;
        try {
            ins = con.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);

        ArrayList<OffLeashData> parks = new ArrayList<>();

        String inputLine;
        int numEntries = 0; //counter to set number of elements in array

        while ((inputLine = in.readLine()) != null) {

            System.out.println(inputLine);
            //add a new park entry and write relevant data to each object
            parks.add(latitude, longitude, pmaid, name, point_x, point_y);
            numEntries += 1;


        }

        in.close();
    }




    //class to store data downloaded from connection - this will be used to overlay points on the map
    public class OffLeashData {


        //"the_geom":{"type":"Point","coordinates":[-122.39198161916639,47.651552429619265]}}

        public OffLeashData(double lat, double lon, String n, int pm, long px, long py)
        {
            latitude = lat;
            longitude = lon;
            name = n;
            pmaid = pm;
            point_x = px;
            point_y = py;
        }
    }

    public static void getItems()
    {
        String parkName;
        double latitude, longitude;
    }
}
