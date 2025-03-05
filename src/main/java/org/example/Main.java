package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {
    private static final String USGS_API_URL =
            "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_day.geojson";
    private static final String NOMINATIM_API_URL =
            "https://nominatim.openstreetmap.org/search?format=json&addressdetails=1&limit=1&polygon_svg=1&q=";

    public static void main(String[] args) {
        System.out.println("Hello world!");
        try {
            String address = "Los Angeles, CA";  // Example address (can be user input)
            double[] latLon = getLatLonFromAddress(address);

            if (latLon != null) {
                double latitude = latLon[0];
                double longitude = latLon[1];

                System.out.println("Searching for earthquakes near: " + address);
                String jsonResponse = getEarthquakeData(USGS_API_URL);
                filterAndDisplayEarthquakes(jsonResponse, latitude, longitude, 500);  // 500 km radius
            } else {
                System.err.println("Could not get coordinates for address: " + address);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static double[] getLatLonFromAddress(String address) throws Exception {
        String query = NOMINATIM_API_URL + address.replace(" ", "%20");
        query = "https://geocode.maps.co/search?q=22 Ravenbank Road Luton&api_key=67c85ea23018d047863017zls27b041".replace(" ", "%20");
        String jsonResponse = getHttpResponse(query);

        JSONArray jsonArray = new JSONArray(jsonResponse);
        if (jsonArray.length() > 0) {
            JSONObject location = jsonArray.getJSONObject(0);
            return new double[]{ location.getDouble("lat"), location.getDouble("lon") };
        }
        return null;
    }

    private static String getEarthquakeData(String apiUrl) throws Exception {
        return getHttpResponse(apiUrl);
    }

    private static void filterAndDisplayEarthquakes(String jsonResponse, double lat, double lon, double radiusKm) throws JSONException {
        JSONObject json = new JSONObject(jsonResponse);
        JSONArray earthquakes = json.getJSONArray("features");

        System.out.println("Recent Earthquakes within " + radiusKm + " km:");
        for (int i = 0; i < earthquakes.length(); i++) {
            JSONObject quake = earthquakes.getJSONObject(i);
            JSONObject properties = quake.getJSONObject("properties");
            JSONObject geometry = quake.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");

            double quakeLon = coordinates.getDouble(0);
            double quakeLat = coordinates.getDouble(1);
            double distance = haversine(lat, lon, quakeLat, quakeLon);

            if (distance <= radiusKm) {
                double magnitude = properties.getDouble("mag");
                String place = properties.getString("place");

                System.out.printf("Magnitude: %.1f | Location: %s | Distance: %.1f km%n",
                        magnitude, place, distance);
            }
        }
    }

    private static String getHttpResponse(String urlString) throws Exception {
        StringBuilder result = new StringBuilder();
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(urlString);
        URL url = new URL(urlString);
        //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //conn.setRequestMethod("GET");
        //conn.setRequestProperty("User-Agent", "Mozilla/5.0");  // Required for Nominatim

        HttpResponse httpResponse = closeableHttpClient.execute(httpGet);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }

    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of Earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}