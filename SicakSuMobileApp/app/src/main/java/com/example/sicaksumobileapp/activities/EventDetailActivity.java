package com.example.sicaksumobileapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sicaksumobileapp.R;
import com.example.sicaksumobileapp.SicakSuApp;
import com.example.sicaksumobileapp.activities.profile.JoinedEventAdapter;
import com.example.sicaksumobileapp.activities.profile.ProfileActivity;
import com.example.sicaksumobileapp.models.SicakSuEvent;
import com.example.sicaksumobileapp.models.SicakSuProfile;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class EventDetailActivity extends AppCompatActivity {
    private TextView txtHeadline;
    private TextView txtContent;
    private TextView txtCreatedBy;
    private TextView txtLimit;
    private TextView txtJoinedPeople;
    private TextView txtJoinCount;
    private TextView txtDate;
    private Button joinBtn;
    private Button leaveBtn;
    private JoinedEventAdapter eventAdapter;
    private String profileId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        // Get the event object from the intent extras
        SicakSuEvent event = (SicakSuEvent) getIntent().getSerializableExtra("event");

        // Initialize the views
        txtHeadline = findViewById(R.id.txtHeadline);
        txtContent = findViewById(R.id.txtContent);
        txtCreatedBy = findViewById(R.id.txtCreatedBy);
        txtLimit = findViewById(R.id.txtLimit);
        txtJoinCount = findViewById(R.id.txtJoinCount);
        txtJoinedPeople = findViewById(R.id.txtJoinedPeople);
        txtDate = findViewById(R.id.txtDate);

        joinBtn = findViewById(R.id.btnJoin);
        leaveBtn = findViewById(R.id.btnLeave);
        profileId = ((SicakSuApp) getApplication()).getUserProfile().getId();
        if(Objects.equals(profileId, event.getCreatedBy().getId())){
            joinBtn.setVisibility(View.INVISIBLE);
            leaveBtn.setVisibility(View.INVISIBLE);
        }else{
            joinBtn.setVisibility(View.VISIBLE);
            leaveBtn.setVisibility(View.VISIBLE);
        }

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);


        // Display the event details
        if (event != null) {
            txtHeadline.setText(event.getHeadline());
            txtContent.setText(event.getContent());
            txtCreatedBy.setText(getString(R.string.created_by, event.getCreatedBy().getName() + " "+ event.getCreatedBy().getSurname()));
            txtJoinCount.setText(getString(R.string.join_count, event.getJoinCount()));

            List<SicakSuProfile> joinedPeople = event.getJoinedPeople();
            StringBuilder joinedPeopleNames = new StringBuilder();
            for (SicakSuProfile profile : joinedPeople) {
                String nameSurname = profile.getName() + " " + profile.getSurname();
                joinedPeopleNames.append(nameSurname).append(", ");
            }

            if (joinedPeopleNames.length() > 0) {
                joinedPeopleNames.setLength(joinedPeopleNames.length() - 2);
            }
            txtJoinedPeople.setText(getString(R.string.joined_people, joinedPeopleNames.toString()));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
            // Format the LocalDateTime object as a string
            String formattedDateTime = event.getRequestDate().format(formatter);
            txtDate.setText(getString(R.string.date, formattedDateTime));

            txtLimit.setText(getString(R.string.limit, event.getLimit()));

            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinEvent(profileId,event.getId());
                }
            });

            leaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leaveEvent(profileId,event.getId());
                }
            });

        }
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



    public void joinEvent(String profileId, String eventId) {
        AsyncTask<Void, Void, Integer> joinEventTask = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    // Construct the URL for the joinEvent endpoint
                    String urlString = "http://10.0.2.2:8080/sicaksu/profile/" + profileId + "/event/" + eventId;
                    URL url = new URL(urlString);

                    // Create the HttpURLConnection object
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    // Set the request body
                    String requestBody = "profileId=" + profileId + "&eventId=" + eventId;
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(requestBody.getBytes());
                    outputStream.flush();

                    // Get the response
                    int responseCode = connection.getResponseCode();

                    // Close the connection
                    connection.disconnect();

                    return responseCode;
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
            }

            @Override
            protected void onPostExecute(Integer responseCode) {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    showSnackbar("Event joined successfully");
                } else {
                    showSnackbar("Failed to join the event");
                }
            }
        };

        joinEventTask.execute();
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
    // Leave event method
    public void leaveEvent(String profileId, String eventId) {
        AsyncTask<Void, Void, Integer> leaveEventTask = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    // Construct the URL for the leaveEvent endpoint
                    String urlString = "http://10.0.2.2:8080/sicaksu/profile/" + profileId + "/event/" + eventId;
                    URL url = new URL(urlString);

                    // Create the HttpURLConnection object
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("DELETE");

                    // Get the response
                    int responseCode = connection.getResponseCode();

                    // Close the connection
                    connection.disconnect();

                    return responseCode;
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
            }

            @Override
            protected void onPostExecute(Integer responseCode) {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    showSnackbarDel("Event left successfully");
                } else {
                    showSnackbar("Failed to leave the event");
                    showSnackbarDel("Failed to leave the event");
                }
            }

        };

        leaveEventTask.execute();
    }


    // Show a snackbar message
    private void showSnackbarDel(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
