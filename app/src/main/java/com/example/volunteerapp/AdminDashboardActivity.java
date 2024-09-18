// AdminDashboardActivity.java
package com.example.volunteerapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button viewEventsButton, deleteEventButton, viewVolunteersButton, deleteVolunteerButton, viewOrganizersButton, deleteOrganizerButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        dbHelper = new DatabaseHelper(this);

        viewEventsButton = findViewById(R.id.view_events);
        deleteEventButton = findViewById(R.id.delete_event);
        viewVolunteersButton = findViewById(R.id.view_volunteers);
        deleteVolunteerButton = findViewById(R.id.delete_volunteer);
        viewOrganizersButton = findViewById(R.id.view_organizers);
        deleteOrganizerButton = findViewById(R.id.delete_organizer);

        viewEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewEvents();
            }
        });

        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });

        viewVolunteersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewVolunteers();
            }
        });

        deleteVolunteerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVolunteer();
            }
        });

        viewOrganizersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOrganizers();
            }
        });

        deleteOrganizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrganizer();
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

    private void deleteEvent() {
        showInputDialog("Delete Event", "Enter Event ID to delete:", new InputDialogCallback() {
            @Override
            public void onInput(String input) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int rowsDeleted = db.delete("events", "id=?", new String[]{input});
                if (rowsDeleted > 0) {
                    Toast.makeText(AdminDashboardActivity.this, "Event deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminDashboardActivity.this, "Event not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void viewVolunteers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "role=?", new String[]{"volunteer"}, null, null, null);
        StringBuilder volunteers = new StringBuilder();
        while (cursor.moveToNext()) {
            volunteers.append("ID: ").append(cursor.getInt(cursor.getColumnIndex("id")))
                    .append(", Username: ").append(cursor.getString(cursor.getColumnIndex("username")))
                    .append("\n");
        }
        cursor.close();
        showAlert("Volunteers", volunteers.toString());
    }

    private void deleteVolunteer() {
        showInputDialog("Delete Volunteer", "Enter Volunteer ID to delete:", new InputDialogCallback() {
            @Override
            public void onInput(String input) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int rowsDeleted = db.delete("users", "id=? AND role=?", new String[]{input, "volunteer"});
                if (rowsDeleted > 0) {
                    Toast.makeText(AdminDashboardActivity.this, "Volunteer deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminDashboardActivity.this, "Volunteer not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void viewOrganizers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "role=?", new String[]{"organizer"}, null, null, null);
        StringBuilder organizers = new StringBuilder();
        while (cursor.moveToNext()) {
            organizers.append("ID: ").append(cursor.getInt(cursor.getColumnIndex("id")))
                    .append(", Username: ").append(cursor.getString(cursor.getColumnIndex("username")))
                    .append("\n");
        }
        cursor.close();
        showAlert("Organizers", organizers.toString());
    }

    private void deleteOrganizer() {
        showInputDialog("Delete Organizer", "Enter Organizer ID to delete:", new InputDialogCallback() {
            @Override
            public void onInput(String input) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int rowsDeleted = db.delete("users", "id=? AND role=?", new String[]{input, "organizer"});
                if (rowsDeleted > 0) {
                    Toast.makeText(AdminDashboardActivity.this, "Organizer deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminDashboardActivity.this, "Organizer not found", Toast.LENGTH_SHORT).show();
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