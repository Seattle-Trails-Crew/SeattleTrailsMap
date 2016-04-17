package gov.seattle.trails;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Quinn on 4/3/16.
 */
public class OffLeashData implements Serializable {

    public OffLeashData() {


    }


/*
    //String socAppToken = "ZSFz0bJfTTnGi8aQxJJLQ9TuB";

    static String jsonString = "";
    static JSONArray jsonArray;

    public JSONArray getJSONFromUrl() throws IOException {

        final String url = "https://data.seattle.gov/resource/ybmn-w2mc.json";

        //open connection
        URL offLeashURL = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) offLeashURL.openConnection();
        InputStream ins = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);


        String inputLine;
        StringBuilder jString = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {

            jString.append(inputLine);
        }

        //convert StringBuilder jString to String jsonString
        jsonString = jString.toString();

        //display JSON string in logcat for verification that download is successful
        Log.i("OFD", jsonString);

        //close connection
        in.close();

        try {
            jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

*/
}


