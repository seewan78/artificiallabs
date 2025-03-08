import com.artificial.properties.USSate;
import com.artificial.application.EarthquakeDataLoader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class EarthquakeDataTest {
    @Test
    public void testExampleSampleEarthquakeData(){
        try {
            String sampleData = "{ \"features\": [" +
                    "{ \"properties\": { \"place\": \"10 km from Los Angeles, California\" } }," +
                    "{ \"properties\": { \"place\": \"Near Reno, Nevada\" } }" +
                    "{ \"properties\": { \"place\": \"Far from Reno, Nevada\" } }" +
                    "] }";

            Map<String, Integer> result = EarthquakeDataLoader.extractEarthquakeData(new ByteArrayInputStream(sampleData.getBytes()));
            assertEquals(1, result.get("California"));
            assertEquals(2, result.get("Nevada"));
        }catch (Exception ex){
            assertTrue(false);
        }
    }

    @Test
    public void testProcessEarthquakeData() {
        try {
            Map<String, Integer> earthquakeData = EarthquakeDataLoader.extractEarthquakeData();
            assertTrue(earthquakeData.keySet().size() > 0);
        }catch (Exception Exception){
            assertTrue(false);
        }
    }

    @Test
    public void testNoHawaiDataIsStored(){
        try {
            Map<String, Integer> earthquakeData = EarthquakeDataLoader.extractEarthquakeData();
            boolean hasData = earthquakeData.containsKey("Hawai");
            assertFalse(hasData);
        }catch (Exception Exception){
            assertTrue(false);
        }
    }

    @Test
    public void testStateValidity(){
        try{
            USSate usSate = USSate.getInstance();
            assertFalse(usSate.isValidUSState("Hawaii"));
            assertTrue(usSate.isValidUSState("CA"));
            assertTrue(usSate.isValidUSState("Alaska"));
        }catch (Exception ex){

        }
    }


} 