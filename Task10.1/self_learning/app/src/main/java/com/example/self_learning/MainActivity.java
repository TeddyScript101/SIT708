package com.example.self_learning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView signupPrompt = findViewById(R.id.signupPrompt);
        signupPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> handleLogin());

        TextView titleText = findViewById(R.id.titleText);
        titleText.setAlpha(0f);
        titleText.animate().alpha(1f).setDuration(800);

        EditText usernameInput = findViewById(R.id.usernameInput);
        usernameInput.setAlpha(0f);
        usernameInput.animate().alpha(1f).setDuration(1000).setStartDelay(300);

        EditText passwordInput = findViewById(R.id.passwordInput);
        passwordInput.setAlpha(0f);
        passwordInput.animate().alpha(1f).setDuration(1000).setStartDelay(500);

        loginButton.setAlpha(0f);
        loginButton.animate().alpha(1f).setDuration(1000).setStartDelay(700);
    }

    private void handleLogin() {
        EditText usernameField = findViewById(R.id.usernameInput);
        EditText passwordField = findViewById(R.id.passwordInput);

        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return;
        }

        DBHelper dbHelper = new DBHelper(this);
        boolean isValidUser = dbHelper.validateUser(username, password);

        if (isValidUser) {
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, QuizHomeActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
