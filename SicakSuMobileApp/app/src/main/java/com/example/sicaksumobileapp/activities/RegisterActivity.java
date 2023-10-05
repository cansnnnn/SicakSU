package com.example.sicaksumobileapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sicaksumobileapp.R;
import com.example.sicaksumobileapp.SicakSuApp;
import com.example.sicaksumobileapp.models.AuthenticationPayload;
import com.example.sicaksumobileapp.repository.EventRepo;
import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText imageUrl;
    private EditText username;
    private EditText password;
    private Button buttonRegister;
    private ProgressBar progressBar;

    Handler registerHandler = new Handler(message -> {
        AuthenticationPayload payload = (AuthenticationPayload)message.obj;

        if ("authenticated".equals(payload.getStatus())) {
            showSnackbar("Registered");
            // initilaize user's profile
            ((SicakSuApp)getApplication()).setUserProfile(payload.getProfile());

            // Create an Intent to open CreateEventActivity
            Intent intent = new Intent(RegisterActivity.this, FeedActivity.class);
            // Clear the activity stack and start the new activity (when pressed back button do not come back)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if ("notAuthenticated".equals(payload.getStatus())) {
            // The register event failed
            showSnackbar("Not registered");
        }
        progressBar.setVisibility(View.INVISIBLE);
        buttonRegister.setVisibility(View.VISIBLE);
        return true;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = findViewById(R.id.registerName);
        lastName = findViewById(R.id.registerLastName);
        imageUrl = findViewById(R.id.registerImageUrl);
        username = findViewById(R.id.registerUsername);
        password = findViewById(R.id.registerPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.registerProgressbar);
        progressBar.setVisibility(View.INVISIBLE);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                buttonRegister.setVisibility(View.INVISIBLE);
                register();
                //Snackbar.make(v, "Register button pressed", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    // Function to show a Snackbar
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    private void register() {
        String simageUrl = imageUrl.getText().toString().trim();
        String susername = username.getText().toString().trim();
        String spassword = password.getText().toString().trim();
        String sfirstName = firstName.getText().toString().trim();
        String slastName = lastName.getText().toString().trim();

        if (TextUtils.isEmpty(susername) || TextUtils.isEmpty(spassword) || TextUtils.isEmpty(sfirstName) || TextUtils.isEmpty(slastName) ) {
            showSnackbar("There is empty fields");
            return;
        }
        if(TextUtils.isEmpty(simageUrl)){
            simageUrl = "https://cdn-icons-png.flaticon.com/512/20/20079.png";
        }
        EventRepo repo = new EventRepo();
        SicakSuApp app = (SicakSuApp)getApplication();
        repo.register(app.srv,registerHandler,susername,spassword,simageUrl,sfirstName,slastName);
    }
}
