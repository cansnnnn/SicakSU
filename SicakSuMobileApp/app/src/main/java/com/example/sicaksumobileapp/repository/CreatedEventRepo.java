package com.example.sicaksumobileapp.repository;

import static java.lang.String.valueOf;

import android.os.Handler;
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
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class CreatedEventRepo {

    public void retrieveCreatedEvents(ExecutorService srv, Handler uiHandler, String profileId) {
        srv.submit(() -> {
            try {
                // Construct the URL to retrieve joined events
                URL url = new URL("http://10.0.2.2:8080/sicaksu/event/created/"+profileId);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.e("Noluyo","connected");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                StringBuilder buffer = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    Log.e("Noluyo",line);
                    buffer.append(line);
                }

                JSONArray arr = new JSONArray(buffer.toString());
                Log.e("array length", Integer.toString(arr.length()));
                for (int j = 0; j< arr.length(); j++){
                    Log.e("buffer events", arr.getJSONObject(j).toString());
                }

                List<SicakSuEvent> data = new ArrayList<>();
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
                    Log.e("Events:",current.toString());

                    SicakSuEvent createdEvent = new SicakSuEvent(
                            current.getString("id"),
                            current.getString("content"),
                            current.getString("headline"),
                            current.getInt("limit"),
                            current.getInt("joinCount"),
                            joinedPeople,
                            StringToDate(current.getString("requestDate")),
                            creator);

                    Log.e("BEFORE ADD",createdEvent.getHeadline());

                    data.add(createdEvent);

                    Log.e("AFTER ADD",createdEvent.toString());
                }
                Log.e("data size",valueOf(data.size()));
                Log.e("Noluyo",data.toString());
                Message msg = new Message();
                msg.obj = data;
                uiHandler.sendMessage(msg);

            } catch (JSONException | IOException e) {
                Log.e("DEV", e.getMessage());
            }
        });
    }


    public LocalDateTime StringToDate(String dateString){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateString, formatter);
    }

}
