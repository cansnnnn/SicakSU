package com.example.sicaksumobileapp.activities.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.example.sicaksumobileapp.R;
import com.example.sicaksumobileapp.SicakSuApp;
import com.example.sicaksumobileapp.activities.FeedActivity;
import com.example.sicaksumobileapp.activities.LoginActivity;
import com.example.sicaksumobileapp.models.SicakSuProfile;
import com.example.sicaksumobileapp.models.SicakSuUser;
import com.example.sicaksumobileapp.repository.EventRepo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;


public class ProfileActivity extends AppCompatActivity {

    private Button btnCreatedEvents;
    private Button btnJoinedEvents;
    private Button btnLogout;
    private FrameLayout fragmentContainer;
    private String profileId ;
    private ImageView profileImage;
    boolean imageDownloaded = false;


    Handler imageHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            profileImage.setImageBitmap((Bitmap) msg.obj);
            imageDownloaded = true;

            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnCreatedEvents = findViewById(R.id.btnCreatedEvents);
        btnJoinedEvents = findViewById(R.id.btnJoinedEvents);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        profileImage = findViewById(R.id.profileProfileImage);
        btnLogout = findViewById(R.id.logoutButton);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        String profileId = getIntent().getStringExtra("id");
        if(Objects.equals(((SicakSuApp) getApplication()).getUserProfile().getId(), profileId)){
            btnLogout.setVisibility(View.VISIBLE);
        }else{
            btnLogout.setVisibility(View.INVISIBLE);
        }
        // Loginden gelecek
        fetchUserProfile(profileId);





        //NAVIGATION BARRRR BACK  BUTTON!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SicakSuApp)getApplication()).setUserProfile(new SicakSuProfile());
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });
        // Set click listeners for the buttons
        btnCreatedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreatedEventsFragment();
            }
        });

        btnJoinedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinedEventsFragment();
            }
        });

    }

    private void showCreatedEventsFragment() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new CreatedEventsFragment())
                .commit();
    }

    public void downloadImage(ExecutorService srv, String path){

        if(imageDownloaded==false){
            EventRepo repo = new EventRepo();
            repo.downloadImage(srv,imageHandler,path);
        }

    }
    private void showJoinedEventsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new JoinedEventsFragment())
                .commit();
    }

    //username fetch
    private void fetchUserProfile(String profileId) {
        String url = "http://10.0.2.2:8080/sicaksu/profile/" + profileId;

        // Make the API call using a networking library like Retrofit, Volley, or OkHttp
        // Here, I'll demonstrate using the HttpURLConnection as in your existing code

        Thread thread = new Thread(() -> {
            try {
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

                // Set up the connection and make the GET request

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse the response JSON and extract the user's name
                    JSONObject profileJson = new JSONObject(response.toString());
                    String userName = profileJson.getString("name") +" "+ profileJson.getString("lastname");
                    SicakSuApp app = (SicakSuApp) getApplication();
                    downloadImage(app.srv,profileJson.getString("imageUrl"));
                    // Update the UI on the main thread
                    runOnUiThread(() -> {
                        // Find the TextView by its ID
                        TextView tvUserName = findViewById(R.id.profileName);

                        // Set the user's name as the text of the TextView
                        tvUserName.setText(userName);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close the current activity and navigate back
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

}

