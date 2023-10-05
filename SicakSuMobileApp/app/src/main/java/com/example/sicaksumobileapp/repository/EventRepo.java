package com.example.sicaksumobileapp.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.sicaksumobileapp.models.AuthenticationPayload;
import com.example.sicaksumobileapp.models.SicakSuEvent;
import com.example.sicaksumobileapp.models.SicakSuProfile;
import com.example.sicaksumobileapp.models.SicakSuUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

// Eventle ilgili api requestleri gonderecegimiz repository
public class EventRepo {
    // kendi bilgisayarinzda calistirdiginiz dockerdaki database erismek icin emulator kullanarak,
    // burdaki ipyi kendi ipiniz ile degistirin
    String yourIp = "10.51.14.11";
    // todo :su an hocanin verdigi apiden planet bilgileri cekip gosteriyor daha sonra duzenlenir
    // todo :backend duzenlenip duzenlenecek
    public void getAllEvents(ExecutorService srv, Handler uiHandler){
        Log.e("Noluyo","Baslangic");
        srv.submit(()->{
            try {

                List<SicakSuEvent> data = new ArrayList<>();
                // bu emulatorde calistigi icin local hosta baglanmasi icin lazimmis
                URL url =
                        new URL("http://"+yourIp+":8080/sicaksu/event");


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.e("Noluyo","1");

                BufferedReader reader
                        = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                Log.e("Noluyo","2");

                StringBuilder buffer = new StringBuilder();
                String line ="";
                while((line=reader.readLine())!=null){
                    //Log.e("Noluyo",line);
                    buffer.append(line);
                }

                JSONArray arr = new JSONArray(buffer.toString());

                for (int i = 0; i <arr.length() ; i++) {

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
                    }
                    JSONObject createdByJson = current.getJSONObject("createdBy");
                    SicakSuProfile createdBy = new SicakSuProfile(
                            createdByJson.getString("id"),
                            createdByJson.getString("name"),
                            createdByJson.getString("lastname"),
                            createdByJson.getString("imageUrl")
                    );
                    //create event class with taken informations from request
                    SicakSuEvent sicakEvent = new SicakSuEvent(
                            current.getString("id"),
                            current.getString("content"),
                            current.getString("headline"),
                            current.getInt("limit"),
                            current.getInt("joinCount"),
                            joinedPeople,
                            StringToDate(current.getString("requestDate")),
                            createdBy
                            );

                    data.add(sicakEvent);
                }

                Message msg = new Message();
                msg.obj = data;
                uiHandler.sendMessage(msg);
                conn.disconnect();

            } catch (JSONException | IOException e) {
                Log.e("DEV",e.getMessage());
            }
        });

    }

    public void joinEvent(ExecutorService srv, Handler uiHandler,String eventId,String profileId){
        srv.submit(()->{
            try {
                // bu emulatorde calistigi icin local hosta baglanmasi icin lazimmis
                URL url =
                        new URL("http://"+yourIp+":8080/sicaksu/profile/"+profileId+"/event/"+eventId);

                // Create a new HttpURLConnection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                conn.setRequestMethod("POST");

                // Set any additional headers if required
                // conn.setRequestProperty("Content-Type", "application/json");

                // Optionally, set request parameters or pass request body

                // Check the response code to handle success or failure
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // The request was successful
                    Message msg = new Message();
                    msg.obj = "joined";
                    uiHandler.sendMessage(msg);
                } else {
                    // The request failed
                    Message msg = new Message();
                    msg.obj = "notJoined";
                    uiHandler.sendMessage(msg);
                }

                // Close the connection
                conn.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void leaveEvent(ExecutorService srv, Handler uiHandler,String eventId,String profileId){
        srv.submit(()->{
            try {
                // bu emulatorde calistigi icin local hosta baglanmasi icin lazimmis
                URL url =
                        new URL("http://"+yourIp+":8080/sicaksu/profile/"+profileId+"/event/"+eventId);

                // Create a new HttpURLConnection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                conn.setRequestMethod("DELETE");

                // Set any additional headers if required
                // conn.setRequestProperty("Content-Type", "application/json");

                // Optionally, set request parameters or pass request body

                // Check the response code to handle success or failure
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // The request was successful
                    Message msg = new Message();
                    msg.obj = "leaved";
                    uiHandler.sendMessage(msg);
                } else {
                    // The request failed
                    Message msg = new Message();
                    msg.obj = "notLeaved";
                    uiHandler.sendMessage(msg);
                }

                // Close the connection
                conn.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void createEvent(ExecutorService srv, Handler uiHandler, String profileId, String content, String headline, int limit, LocalDateTime requestDate) {
        srv.submit(() -> {
            try {
                // Construct the request URL
                URL url = new URL("http://" + yourIp + ":8080/sicaksu/event/" + profileId);

                // Create a new HttpURLConnection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                conn.setRequestMethod("POST");

                // Set the Content-Type header to indicate the request body format
                conn.setRequestProperty("Content-Type", "application/json");

                // Create a JSON object to hold the event information
                JSONObject requestBody = new JSONObject();
                requestBody.put("content", content);
                requestBody.put("headline", headline);
                requestBody.put("limit", limit);
                requestBody.put("requestDate", requestDate.toString());

                // Convert the JSON object to a byte array
                byte[] postDataBytes = requestBody.toString().getBytes("UTF-8");

                // Enable output and set the content length of the request body
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

                // Write the request body bytes to the connection's output stream
                conn.getOutputStream().write(postDataBytes);

                // Check the response code to handle success or failure
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // The request was successful
                    Message msg = new Message();
                    msg.obj = "created";
                    uiHandler.sendMessage(msg);
                } else {
                    // The request failed
                    Message msg = new Message();
                    msg.obj = "notCreated";
                    uiHandler.sendMessage(msg);
                }

                // Close the connection
                conn.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void login(ExecutorService srv, Handler uiHandler, String username, String password) {
        srv.submit(() -> {
            try {
                // Construct the request URL
                URL url = new URL("http://" + yourIp + ":8080/sicaksu/login");

                // Create a new HttpURLConnection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                conn.setRequestMethod("POST");

                // Set the Content-Type header to indicate the request body format
                conn.setRequestProperty("Content-Type", "application/json");

                // Create a JSON object to hold the login information
                JSONObject requestBody = new JSONObject();
                requestBody.put("username", username);
                requestBody.put("password", password);

                // Convert the JSON object to a byte array
                byte[] postDataBytes = requestBody.toString().getBytes("UTF-8");

                // Enable output and set the content length of the request body
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

                // Write the request body bytes to the connection's output stream
                conn.getOutputStream().write(postDataBytes);

                // Check the response code to handle success or failure
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // The login was successful
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse the response JSON
                    JSONObject responseJson = new JSONObject(response.toString());

                    // Extract the user profile information
                    JSONObject profileJson = responseJson.getJSONObject("profile");

                    // Create a new user object with the profile data
                    AuthenticationPayload payload = new AuthenticationPayload();
                    payload.setProfile(new SicakSuProfile(
                            profileJson.getString("id"),
                            profileJson.getString("name"),
                            profileJson.getString("lastname"),
                            profileJson.getString("imageUrl")
                    ));
                    payload.setStatus("authenticated");

                    // Send the user object to the UI thread
                    Message msg = new Message();
                    msg.obj = payload;
                    uiHandler.sendMessage(msg);
                } else {
                    // The login failed
                    Message msg = new Message();
                    AuthenticationPayload payload = new AuthenticationPayload();
                    payload.setStatus("notAuthenticated");
                    msg.obj = payload;
                    uiHandler.sendMessage(msg);
                }

                // Close the connection
                conn.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void register(ExecutorService srv, Handler uiHandler, String username, String password, String imageUrl, String firstName, String lastName) {
        srv.submit(() -> {
            try {
                // Construct the request URL
                URL url = new URL("http://" + yourIp + ":8080/sicaksu/register");

                // Create a new HttpURLConnection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                conn.setRequestMethod("POST");

                // Set the Content-Type header to indicate the request body format
                conn.setRequestProperty("Content-Type", "application/json");

                // Create a JSON object to hold the user information
                JSONObject requestBody = new JSONObject();
                requestBody.put("username", username);
                requestBody.put("password", password);

                // Create a JSON object to hold the profile information
                JSONObject profile = new JSONObject();
                profile.put("name", firstName);
                profile.put("lastname", lastName);
                profile.put("imageUrl", imageUrl);

                // Add the profile object to the main user object
                requestBody.put("profile", profile);

                // Convert the JSON object to a byte array
                byte[] postDataBytes = requestBody.toString().getBytes("UTF-8");

                // Enable output and set the content length of the request body
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

                // Write the request body bytes to the connection's output stream
                conn.getOutputStream().write(postDataBytes);

                // Check the response code to handle success or failure
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // The request was successful
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse the response JSON
                    JSONObject responseJson = new JSONObject(response.toString());
                    JSONObject profileJson = responseJson.getJSONObject("profile");

                    // Create a new user object with the profile data
                    AuthenticationPayload payload = new AuthenticationPayload();
                    payload.setProfile(new SicakSuProfile(
                            profileJson.getString("id"),
                            profileJson.getString("name"),
                            profileJson.getString("lastname"),
                            profileJson.getString("imageUrl")
                    ));
                    payload.setStatus("authenticated");

                    // Send the user object to the UI thread
                    Message msg = new Message();
                    //using payload because I want to send status and profile information
                    msg.obj = payload;
                    uiHandler.sendMessage(msg);
                } else {
                    // The request failed
                    Message msg = new Message();
                    AuthenticationPayload payload = new AuthenticationPayload();
                    payload.setStatus("notAuthenticated");
                    //using payload because I want to send status and profile information
                    msg.obj = payload;
                    uiHandler.sendMessage(msg);
                }

                // Close the connection
                conn.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void downloadImage(ExecutorService srv, Handler uiHandler, String path) {
        srv.submit(() -> {
            try {
                URL url =
                        new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                Bitmap bmp = BitmapFactory.decodeStream(conn.getInputStream());

                Message msg = new Message();
                msg.obj = bmp;
                uiHandler.sendMessage(msg);

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
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


/*Example event list
[
    {
        "id": "64720cbeae0bd83f1f643874",
        "content": "Hell yeaaaaa",
        "headline": "Event 4",
        "limit": 100,
        "joinCount": 6,
        "requestDate": "2023-05-28T09:45:00",
        "joinedPeople": [
            {
                "id": "64720c90ae0bd83f1f643872",
                "name": "Koyu",
                "lastname": "Kante",
                "imageUrl": "https://example.com/profile_images/johndoe.jpg"
            }
        ],
        "createdBy": {
            "id": "6471dc1fe27cea661daa54b9",
            "name": "John",
            "lastname": "Doe",
            "imageUrl": "https://example.com/profile_images/johndoe.jpg"
        }
    },
    {
        "id": "64720cf0ae0bd83f1f643875",
        "content": "Jojo Season 8 Episode 5",
        "headline": "Anime Watching",
        "limit": 100,
        "joinCount": 6,
        "requestDate": "2023-05-28T09:45:00",
        "joinedPeople": [
            {
                "id": "64720c90ae0bd83f1f643872",
                "name": "Koyu",
                "lastname": "Kante",
                "imageUrl": "https://example.com/profile_images/johndoe.jpg"
            }
        ],
        "createdBy": {
            "id": "64720c80ae0bd83f1f643870",
            "name": "Jonathan",
            "lastname": "Joestar",
            "imageUrl": "https://example.com/profile_images/johndoe.jpg"
        }
    },
    {
        "id": "64720d0dae0bd83f1f643876",
        "content": "Dabbe bir cin vakasi",
        "headline": "Horror movie night",
        "limit": 100,
        "joinCount": 5,
        "requestDate": "2023-05-28T09:45:00",
        "joinedPeople": [],
        "createdBy": {
            "id": "64720c90ae0bd83f1f643872",
            "name": "Koyu",
            "lastname": "Kante",
            "imageUrl": "https://example.com/profile_images/johndoe.jpg"
        }
    },
    {
        "id": "64721352a2d2ec66ca21bea8",
        "content": "Hizli and Ofkeli",
        "headline": "Adventure Movie Night",
        "limit": 100,
        "joinCount": 0,
        "requestDate": "2023-05-28T09:45:00",
        "joinedPeople": [],
        "createdBy": {
            "id": "64720c90ae0bd83f1f643872",
            "name": "Koyu",
            "lastname": "Kante",
            "imageUrl": "https://example.com/profile_images/johndoe.jpg"
        }
    }
]
* */