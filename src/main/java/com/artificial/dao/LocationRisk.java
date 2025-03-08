package com.artificial.dao;

public class LocationRisk {
    private String buildingName;
    private String location;
    private String fullAddress;
    private int earthquakeRisk;

    public LocationRisk(String buildingName, String location, String fullAddress) {
        this.buildingName = buildingName;
        this.location = location;
        this.fullAddress = fullAddress;
        this.earthquakeRisk = 0;
    }

    public String getState() {
        if (location.isEmpty() ){
            return null;
        }

        String[] values = location.split(",");
        return values.length==2?values[1].trim():null;
    }

    public void setEarthquakeRisk(int risk) {
        this.earthquakeRisk = risk;
    }

    @Override
    public String toString() {
        return new StringBuilder(buildingName).append(" | ").append( location )
                .append(" | ").append(fullAddress).append("  - (Earthquake Risk: ")
                .append(earthquakeRisk).append(")").toString();
    }
}
