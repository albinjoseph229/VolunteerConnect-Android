// LoginActivity.java
package com.example.volunteerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query("users", new String[]{"id", "role"}, "username=? AND password=?", new String[]{username, password}, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    String role = cursor.getString(cursor.getColumnIndex("role"));
                    Intent intent;
                    switch (role) {
                        case "admin":
                            intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                            break;
                        case "organizer":
                            intent = new Intent(LoginActivity.this, OrganizerDashboardActivity.class);
                            break;
                        case "volunteer":
                            intent = new Intent(LoginActivity.this, VolunteerDashboardActivity.class);
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "Invalid role", Toast.LENGTH_SHORT).show();
                            return;
                    }
                    startActivity(intent);
                    finish(); // Close the login activity
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        });
    }
}