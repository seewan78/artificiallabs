package com.artificial.application;

import com.artificial.dao.LocationRisk;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * EarthquakeRiskApp - main application
 */
public class EarthquakeRiskApp {

    //Static list of potential risk to underwrite
    private static List<LocationRisk> locationRisks = Arrays.asList(
            new LocationRisk("West Anchorage High School",
                    "Anchorage, Alaska",
                    "1700 Hillcrest Dr, Anchorage, AK 99517"),
            new LocationRisk("City Hall", "San Francisco, CA",
                    "1 Dr Carlton B Goodlett Pl, San Francisco, CA 94102"),
            new LocationRisk("Los Angeles Memorial Coliseum", "Los Angeles, CA"
            ,"3911 S Figueroa St, Los Angeles, CA 90037"),
            new LocationRisk("Harrah's Reno (Former)", "Reno, Nevada"
                    ,"219 N Center St, Reno, NV 89501"),
            new LocationRisk("Benson Polytechnic High School", "Portland, Oregon"
                    ,"546 NE 12th Ave, Portland, OR 97232"),
            new LocationRisk("Salt Lake Temple", "Salt Lake City, Utah"
                    ,"219 N Center St, Reno, NV 89501"),
            new LocationRisk("Challis High School", "Challis, Idaho"
                    ,"1 Schoolhouse Rd, Challis, ID 83226")
    );

    public static void main(String[] args){
        try {
            Map<String, Integer> earthQuakeData = EarthquakeDataLoader.extractEarthquakeData();
            EarthquakeRiskEvaluator.assessRisk(locationRisks, earthQuakeData);
            System.out.println("Earthquake risk assessment for locations:");
            for (LocationRisk location : locationRisks) {
                System.out.println(location);
            }
        }catch (Exception ex){
            System.out.println("Error processing data : " + ex.getMessage());
        }
    }
}
