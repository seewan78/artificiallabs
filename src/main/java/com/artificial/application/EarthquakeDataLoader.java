package com.artificial.application;

import com.artificial.properties.USSate;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * EarthquakeDataLoader connects to USGS and extract earthquake data for last week
 * The processes the data - filtering out data keeping only data with valid US states listed in src/main/resources/config/usstates.properties
 *
 */
public class EarthquakeDataLoader {
    private static final Logger log = LoggerFactory.getLogger(EarthquakeDataLoader.class);
    private static final String USGS_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson";
    private static USSate usSate = USSate.getInstance();

    /**
     * This method calls the USGS API to pull last week earthquake data
     * @return InputStream - Data input stream
     * @throws IOException if there are any issues with connectivity or the API was not successful
     */
    private static InputStream loadData() throws IOException {
        log.debug("Attempting to make API call to " + USGS_URL);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(USGS_URL);
        HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == 200){
            return httpResponse.getEntity().getContent();
        }
        throw new IOException("Error extracing USGS Earthquake data : " + statusLine.getReasonPhrase());
    }

    /**
     * processData method process the data from the API Call into state and the number of earthquake occurences
     * @param in InputStream
     * @return Map<String, Integer> - State and the number of earthquake incidents reported
     * @throws Exception - Any errors when processing the data stream received from the API Call
     */
    private static Map<String, Integer> processData(InputStream in) throws Exception{
        JSONObject result = (JSONObject) JSONValue.parse(new InputStreamReader(in));
        Map<String, Integer> earthquakeData = new HashMap<>();
        JSONArray dataArray = (JSONArray) result.get("features");
        for (int index=0; index < dataArray.size(); index++){
            JSONObject feature = (JSONObject) dataArray.get(index);
            JSONObject properties = (JSONObject)feature.get("properties");
            String place = (String)properties.get("place");
            String state = extractState(place);
            if (usSate.isValidUSState(state)){
                String stateName = usSate.getStateName(state);
                earthquakeData.put(stateName, earthquakeData.getOrDefault(stateName, 0) + 1);
            }
        }
        return earthquakeData;
    }

    /**
     * extractData calls the above method to make API call to USGS and process the data received
     * @return Map<String, Integer> - State and the number of earthquake incidents reported
     * @throws IOException
     * @throws Exception
     */
    public static Map<String, Integer> extractEarthquakeData() throws IOException, Exception{
        InputStream in = loadData();
        return processData(in);
    }

    /**
     * Method included for testing purposes.
     * @param in InputStream - JSON Data as input stream
     * @return Map<String, Integer> - Map with result i.e. count of earthquake occurences reporte
     * @throws IOException
     * @throws Exception
     */
    public static Map<String, Integer> extractEarthquakeData(InputStream in) throws IOException, Exception{
        return processData(in);
    }

    /**
     * extractState return the State value
     * @param place String
     * @return String
     */
    private static String extractState(String place) {
        String[] parts = place.split(", ");
        return parts.length > 1 ? parts[parts.length - 1] : "Unknown";
    }

}
