package com.s22010189.pil_pal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//helper class for map page
//Using to get nearby places and details
public class DataParser {
    // Method to extract details of a single nearby place from JSON object
    private HashMap<String,String> getSingleNearbyPlace(JSONObject googlePlaceJSON){
        HashMap<String,String> googlePlaceMap = new HashMap<>();
        String nameOfPlace = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        try {
            if(!googlePlaceJSON.isNull("name")){
                nameOfPlace = googlePlaceJSON.getString("name");
            }
            if(!googlePlaceJSON.isNull("vicinity")){
                vicinity = googlePlaceJSON.getString("vicinity");
            }
            latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJSON.getString("reference");
            googlePlaceMap.put("place_name", nameOfPlace);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
    // Method to extract details of all nearby places from JSON array
    private List<HashMap<String, String>> getAllNearbyPlaces(JSONArray jsonArray){
        List<HashMap<String,String>> nearbyPlacesList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            try {
                HashMap<String, String> nearbyPlaceMap = getSingleNearbyPlace(jsonArray.getJSONObject(i));
                nearbyPlacesList.add(nearbyPlaceMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return nearbyPlacesList;
    }
    // Main parse method to parse JSON data
    public List<HashMap<String,String>> parse(String jsonData){
        JSONArray jsonArray = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (jsonArray != null) ? getAllNearbyPlaces(jsonArray) : null;
    }
}
