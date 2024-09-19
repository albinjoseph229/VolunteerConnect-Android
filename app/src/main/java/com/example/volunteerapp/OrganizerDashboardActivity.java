package com.example.volunteerapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class OrganizerDashboardActivity extends AppCompatActivity {

    private Button addEventButton, viewRegisteredVolunteersButton, viewMyEventsButton;
    private DatabaseHelper dbHelper;
    private int organizerId; // Assume this is set when the organizer logs in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_dashboard);

        dbHelper = new DatabaseHelper(this);

        addEventButton = findViewById(R.id.add_event);
        viewRegisteredVolunteersButton = findViewById(R.id.view_registered_volunteers);
        viewMyEventsButton = findViewById(R.id.view_my_events);

        // Assume organizerId is passed from the login activity
        organizerId = getIntent().getIntExtra("ORGANIZER_ID", -1);
        Log.d("OrganizerDashboard", "Organizer ID: " + organizerId);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

        viewRegisteredVolunteersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRegisteredVolunteers();
            }
        });

        viewMyEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMyEvents();
            }
        });
    }

    private void addEvent() {
        showInputDialog("Add Event", "Enter Event Name, Description, and Date (comma separated):", new InputDialogCallback() {
            @Override
            public void onInput(String input) {
                String[] eventDetails = input.split(",");
                if (eventDetails.length != 3) {
                    Toast.makeText(OrganizerDashboardActivity.this, "Please enter all details correctly", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = eventDetails[0].trim();
                String description = eventDetails[1].trim();
                String date = eventDetails[2].trim();

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("description", description);
                values.put("date", date);
                values.put("organizer_id", organizerId); // Assuming you have a column for organizer_id in the events table

                try {
                    long newRowId = db.insertOrThrow("events", null, values);
                    if (newRowId != -1) {
                        Toast.makeText(OrganizerDashboardActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OrganizerDashboardActivity.this, "Failed to add event", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    Log.e("OrganizerDashboard", "Error inserting event", e);
                    Toast.makeText(OrganizerDashboardActivity.this, "Error inserting event: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void viewRegisteredVolunteers() {
        showInputDialog("View Registered Volunteers", "Enter Event ID:", new InputDialogCallback() {
            @Override
            public void onInput(String input) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = null;
                try {
                    cursor = db.rawQuery("SELECT COUNT(*) AS volunteer_count FROM registrations WHERE event_id = ?", new String[]{input});
                    if (cursor.moveToFirst()) {
                        int volunteerCount = cursor.getInt(cursor.getColumnIndex("volunteer_count"));
                        showAlert("Registered Volunteers", "Number of volunteers registered for this event: " + volunteerCount);
                    } else {
                        showAlert("Registered Volunteers", "No volunteers registered for this event.");
                    }
                } catch (Exception e) {
                    Log.e("OrganizerDashboard", "Error fetching registered volunteers", e);
                    Toast.makeText(OrganizerDashboardActivity.this, "Error fetching registered volunteers: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });
    }

    private void viewMyEvents() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("events", null, "organizer_id=?", new String[]{String.valueOf(organizerId)}, null, null, null);
            StringBuilder events = new StringBuilder();
            while (cursor.moveToNext()) {
                events.append("ID: ").append(cursor.getInt(cursor.getColumnIndex("id")))
                        .append(", Name: ").append(cursor.getString(cursor.getColumnIndex("name")))
                        .append(", Description: ").append(cursor.getString(cursor.getColumnIndex("description")))
                        .append(", Date: ").append(cursor.getString(cursor.getColumnIndex("date")))
                        .append("\n");
            }
            showAlert("My Events", events.toString());
        } catch (Exception e) {
            Log.e("OrganizerDashboard", "Error fetching events", e);
            Toast.makeText(this, "Error fetching events", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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