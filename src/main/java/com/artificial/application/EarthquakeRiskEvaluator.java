package com.artificial.application;

import com.artificial.dao.LocationRisk;
import com.artificial.properties.USSate;

import java.util.List;
import java.util.Map;

/**
 * The class takes a list of potential locations and evaluates the earthquake data from the USGS
 * and attaches a number which is the number of earthquakes against each of the state
 */
public class EarthquakeRiskEvaluator {
    private static USSate usSate = USSate.getInstance();
    public static void assessRisk(List<LocationRisk> locations, Map<String, Integer> stateRiskData) throws Exception{
        for (LocationRisk location : locations) {
            int risk = stateRiskData.getOrDefault(usSate.getStateName(location.getState()), 0);
            location.setEarthquakeRisk(risk);
        }
    }
}
