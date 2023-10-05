package com.example.sicaksumobileapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sicaksumobileapp.R;
import com.example.sicaksumobileapp.SicakSuApp;
import com.example.sicaksumobileapp.models.AuthenticationPayload;
import com.example.sicaksumobileapp.models.SicakSuProfile;
import com.example.sicaksumobileapp.repository.EventRepo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button registerButton;

    private ExecutorService executorService;
    private Handler uiHandler;
    private EventRepo eventRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.createAccountButton);

        executorService = Executors.newSingleThreadExecutor();
        uiHandler = new Handler(getMainLooper(), new UiHandlerCallback());
        eventRepo = new EventRepo();

        registerButton.setOnClickListener(v -> {
            // Create an Intent to open RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String enteredUsername = username.getText().toString().trim();
            String enteredPassword = password.getText().toString().trim();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else {
                login(enteredUsername, enteredPassword);
            }
        });
    }

    private void login(String username, String password) {
        eventRepo.login(executorService, uiHandler, username, password);
    }

    private class UiHandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            AuthenticationPayload payload = (AuthenticationPayload) msg.obj;
            if (payload.getStatus().equals("authenticated")) {
                SicakSuProfile userProfile = payload.getProfile();
                // Login successful
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                ((SicakSuApp)getApplication()).setUserProfile(payload.getProfile());

                // Create an Intent to open RegisterActivity
                Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                //finish(); // Finish the LoginActivity to prevent going back to it with the back button
            } else {
                // Login failed, show an error message
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}

