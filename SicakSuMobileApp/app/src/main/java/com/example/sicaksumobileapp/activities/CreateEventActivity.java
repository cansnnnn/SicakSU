package com.example.sicaksumobileapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sicaksumobileapp.R;
import com.example.sicaksumobileapp.SicakSuApp;
import com.example.sicaksumobileapp.models.SicakSuProfile;
import com.example.sicaksumobileapp.repository.EventRepo;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    private EditText editTextHeadline;
    private EditText editTextContent;
    private EditText editTextLimit;
    private Button buttonDatePicker;
    private TextView textViewSelectedDate;
    private Button buttonCreateEvent;
    private String profileId;


    private LocalDateTime selectedDate;

    Handler createEventHandler = new Handler(message -> {
        if ("created".equals(message.obj)) {
            showSnackbar("Event Created");
            finish();
        } else if ("notCreated".equals(message.obj)) {
            // The join event failed
            showSnackbar("Event is not created");
        }
        return true;
    });

    // Function to show a Snackbar
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Retrieve the profile information from the Intent extras
        if (getIntent().hasExtra("profileId")) {
            profileId = getIntent().getStringExtra("profileId");
            // Use the profile object as needed
        }

        editTextHeadline = findViewById(R.id.editTextHeadline);
        editTextContent = findViewById(R.id.editTextContent);
        editTextLimit = findViewById(R.id.editTextLimit);
        buttonDatePicker = findViewById(R.id.buttonDatePicker);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        buttonCreateEvent = findViewById(R.id.buttonCreateEvent);

        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date and time
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Create a DatePickerDialog to choose the date
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Handle the selected date
                        // This method will be called when the user selects a date
                        // Store the selected date
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Create a TimePickerDialog to choose the time
                        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Handle the selected time
                                // This method will be called when the user selects a time
                                // Store the selected time
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                // Do something with the selected date and time
                                // For example, update the UI to display the selected date and time
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                String selectedDateTime = dateFormat.format(calendar.getTime());

                                // Define the formatter based on the pattern of the input string
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                                // Parse the String to LocalDateTime using the formatter
                                selectedDate = LocalDateTime.parse(dateFormat.format(calendar.getTime()), formatter);
                                //selectedDate = selectedDateTime
                                textViewSelectedDate.setText(selectedDateTime);
                            }
                        }, hour, minute, false);

                        // Show the TimePickerDialog
                        timePickerDialog.show();
                    }
                }, year, month, day);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headline = editTextHeadline.getText().toString();
                String content = editTextContent.getText().toString();
                int limit = Integer.parseInt(editTextLimit.getText().toString());

                // Check if all fields are filled
                if (!headline.isEmpty() && !content.isEmpty() && limit > 0 && selectedDate != null) {
                    // Call createEvent function from EventRepo with the provided information
                    EventRepo repo = new EventRepo();
                    repo.createEvent(((SicakSuApp)getApplication()).srv,createEventHandler,profileId,content,headline, limit, selectedDate);

                } else {
                    Snackbar.make(v, "There are empty fields", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}