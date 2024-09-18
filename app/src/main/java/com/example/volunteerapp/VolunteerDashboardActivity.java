package com.example.volunteerapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class VolunteerDashboardActivity extends AppCompatActivity {

    private Button viewEventsButton, registerEventButton, viewHoursButton, viewRegisteredEventsButton, deleteEventButton;
    private DatabaseHelper dbHelper;
    private int volunteerId; // Assume this is set when the volunteer logs in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_dashboard);

        dbHelper = new DatabaseHelper(this);

        viewEventsButton = findViewById(R.id.view_events);
        registerEventButton = findViewById(R.id.register_event);
        viewHoursButton = findViewById(R.id.view_hours);
        viewRegisteredEventsButton = findViewById(R.id.view_registered_events);
        deleteEventButton = findViewById(R.id.delete_event);

        // Assume volunteerId is passed from the login activity
        volunteerId = getIntent().getIntExtra("VOLUNTEER_ID", -1);
        Log.d("VolunteerDashboard", "Volunteer ID: " + volunteerId);

        viewEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewEvents();
            }
        });

        registerEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForEvent();
            }
        });

        viewHoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewVolunteeringHours();
            }
        });

        viewRegisteredEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRegisteredEvents();
            }
        });

        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterFromEvent();
            }
        });
    }

    private void viewEvents() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("events", null, null, null, null, null, null);
        StringBuilder events = new StringBuilder();
        while (cursor.moveToNext()) {
            events.append("ID: ").append(cursor.getInt(cursor.getColumnIndex("id")))
                    .append(", Name: ").append(cursor.getString(cursor.getColumnIndex("name")))
                    .append(", Description: ").append(cursor.getString(cursor.getColumnIndex("description")))
                    .append(", Date: ").append(cursor.getString(cursor.getColumnIndex("date")))
                    .append("\n");
        }
        cursor.close();
        showAlert("Events", events.toString());
    }

    private void registerForEvent() {
        showInputDialog("Register for Event", "Enter Event ID and Hours (comma separated):", new InputDialogCallback() {
            @Override
            public void onInput(String input) {
                String[] registrationDetails = input.split(",");
                if (registrationDetails.length != 2) {
                    Toast.makeText(VolunteerDashboardActivity.this, "Please enter all details correctly", Toast.LENGTH_SHORT).show();
                    return;
                }

                int eventId = Integer.parseInt(registrationDetails[0].trim());
                int hours = Integer.parseInt(registrationDetails[1].trim());

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("user_id", volunteerId);
                values.put("event_id", eventId);
                values.put("hours", hours);
                long newRowId = db.insert("registrations", null, values);

                if (newRowId != -1) {
                    Toast.makeText(VolunteerDashboardActivity.this, "Registered for event successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VolunteerDashboardActivity.this, "Failed to register for event", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void viewVolunteeringHours() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(hours) AS total_hours FROM registrations WHERE user_id = ?", new String[]{String.valueOf(volunteerId)});
        if (cursor.moveToFirst()) {
            int totalHours = cursor.getInt(cursor.getColumnIndex("total_hours"));
            showAlert("Volunteering Hours", "Total Volunteering Hours: " + totalHours);
        } else {
            showAlert("Volunteering Hours", "No volunteering hours found");
        }
        cursor.close();
    }

    private void viewRegisteredEvents() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT events.id, events.name, events.description, events.date FROM registrations INNER JOIN events ON registrations.event_id = events.id WHERE registrations.user_id = ?", new String[]{String.valueOf(volunteerId)});
            StringBuilder events = new StringBuilder();
            while (cursor.moveToNext()) {
                events.append("ID: ").append(cursor.getInt(cursor.getColumnIndex("id")))
                        .append(", Name: ").append(cursor.getString(cursor.getColumnIndex("name")))
                        .append(", Description: ").append(cursor.getString(cursor.getColumnIndex("description")))
                        .append(", Date: ").append(cursor.getString(cursor.getColumnIndex("date")))
                        .append("\n");
            }
            if (events.length() == 0) {
                events.append("No events registered.");
            }
            showAlert("Registered Events", events.toString());
        } catch (Exception e) {
            Log.e("VolunteerDashboard", "Error fetching registered events", e);
            Toast.makeText(VolunteerDashboardActivity.this, "Error fetching registered events: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void unregisterFromEvent() {
        showInputDialog("Unregister from Event", "Enter Event ID:", new InputDialogCallback() {
            @Override
            public void onInput(String input) {
                int eventId = Integer.parseInt(input.trim());

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int rowsDeleted = db.delete("registrations", "user_id=? AND event_id=?", new String[]{String.valueOf(volunteerId), String.valueOf(eventId)});

                if (rowsDeleted > 0) {
                    Toast.makeText(VolunteerDashboardActivity.this, "Unregistered from event successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VolunteerDashboardActivity.this, "Failed to unregister from event", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showInputDialog(String title, String message, final InputDialogCallback callback) {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        callback.onInput(value);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private interface InputDialogCallback {
        void onInput(String input);
    }
}