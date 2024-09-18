// RegistrationActivity.java
package com.example.volunteerapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private RadioGroup roleGroup;
    private Button registerButton, goToLoginButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new DatabaseHelper(this);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        roleGroup = findViewById(R.id.roleGroup);
        registerButton = findViewById(R.id.register);
        goToLoginButton = findViewById(R.id.go_to_login);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                int selectedRoleId = roleGroup.getCheckedRadioButtonId();

                if (selectedRoleId == -1) {
                    Toast.makeText(RegistrationActivity.this, "Please select a role", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedRoleButton = findViewById(selectedRoleId);
                String role = selectedRoleButton.getText().toString().toLowerCase();

                // Insert user into database
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("username", username);
                values.put("password", password);
                values.put("role", role);
                long newRowId = db.insert("users", null, values);

                if (newRowId != -1) {
                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}