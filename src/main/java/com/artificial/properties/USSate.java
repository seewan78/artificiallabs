package com.artificial.properties;

import java.io.*;
import java.util.Properties;

/**
 * USState class loads the data in file src/main/resources/config/usstates.properties
 * The properties file contains all the US states that are covered
 * For purpose of this task - Hawaii is not included in the file
 * File contains data StateCode=StateName e.g. CA=California
 */
public class USSate {
    private static final String US_STATES_PROP = "src/main/resources/config/usstates.properties";
    private static USSate instance = null;
    private Properties validStates = loadState() ;

    private USSate(){

    }

    /**
     * load the properties file
     * @return
     */
    private Properties loadState(){
        try {
            Properties properties = new Properties();
            InputStream input = new FileInputStream(US_STATES_PROP);
            properties.load(input);
            return properties;
        }catch (Exception ex){
            System.out.println("Error loading properties");
            return null;
        }
    }

    /**
     * Singleton implementation
     * @return
     */
    public static USSate getInstance(){
        if (instance == null){
            instance = new USSate();
        }
        return instance;
    }

    /**
     * Verifies if the value provided is a valid US State
     * @param value String - state name or code
     * @return boolean - true if valid else false
     * @throws Exception - if the properties load failed
     */
    public boolean isValidUSState(String value) throws Exception{
        if (validStates == null){
            throw new Exception("Unfortunately US States weren't loaded");
        }

        return validStates.containsKey(value) || validStates.contains(value);
    }

    /**
     * Method implements the functionality to return the actual state name if a valid code is provided
     * @param value String - state code or name
     * @return String - return state name
     * @throws Exception - if the properties load failed
     */
    public String getStateName(String value) throws Exception{
        if (validStates.containsKey(value)){
            return validStates.getProperty(value);
        }

        if (validStates.contains(value)){
            return value;
        }

        throw new Exception("Invalid state value provided : " + value);
    }
}
