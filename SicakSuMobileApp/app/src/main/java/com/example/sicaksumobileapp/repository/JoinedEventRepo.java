package com.example.sicaksumobileapp.repository;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.sicaksumobileapp.models.SicakSuEvent;
import com.example.sicaksumobileapp.models.SicakSuProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JoinedEventRepo {

    private ExecutorService executorService;
    private Handler uiHandler;
    private String userId;
    public JoinedEventRepo(String id) {
        executorService = Executors.newSingleThreadExecutor();
        uiHandler = new Handler(Looper.getMainLooper());
        userId = id;
    }

    public interface JoinedEventCallback {
        void onSuccess(List<SicakSuEvent> updatedData);
        void onError(String errorMessage);
    }
    public void retrieveJoinedEvents(JoinedEventCallback callback) {
        Log.e("joined events","Baslangic");
        executorService.submit(() -> {
            try {
                List<SicakSuEvent> data = new ArrayList<>();
                // Construct the URL to retrieve joined events
                URL url = new URL("http://10.0.2.2:8080/sicaksu/event/joined/"+userId);
                // Replace {profileId} with the actual profile id of the user

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.e("Noluyo","connected");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                JSONArray arr = new JSONArray(buffer.toString());

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject current = arr.getJSONObject(i);

                    JSONArray joinedPeopleJson = current.getJSONArray("joinedPeople");
                    List<SicakSuProfile> joinedPeople = new ArrayList<>();
                    //turn joined people objects into java class
                    for(int j = 0; j < joinedPeopleJson.length() ; j++){
                        JSONObject currentPeople = joinedPeopleJson.getJSONObject(j);
                        SicakSuProfile currentProfile = new SicakSuProfile(
                                currentPeople.getString("id"),
                                currentPeople.getString("name"),
                                currentPeople.getString("lastname"),
                                currentPeople.getString("imageUrl")
                        );
                        joinedPeople.add(currentProfile);
                        Log.e("Joined people",currentProfile.toString());
                    }

                    //Get the creator of the event
                    JSONObject obj = current.getJSONObject("createdBy");
                    SicakSuProfile creator = new SicakSuProfile(obj.getString("id"),
                            obj.getString("name"), obj.getString("lastname"), obj.getString("imageUrl"));

                    // Extract the necessary information from the response
                    // and create SicakSuEvent objects
                    SicakSuEvent sicakEvent = new SicakSuEvent(
                            current.getString("id"),
                            current.getString("content"),
                            current.getString("headline"),
                            current.getInt("limit"),
                            current.getInt("joinCount"),
                            joinedPeople,
                            StringToDate(current.getString("requestDate")),
                            creator);

                    data.add(sicakEvent);
                    Log.e("Noluyo",sicakEvent.toString());
                }

                uiHandler.post(() -> callback.onSuccess(data));
            } catch (JSONException | IOException e) {
                uiHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public LocalDateTime StringToDate(String dateString){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        return dateTime;
    }

}
