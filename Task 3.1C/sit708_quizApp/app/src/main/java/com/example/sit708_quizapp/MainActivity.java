package com.example.sit708_quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    EditText nameInputEditText;
    Button startBtn;
    String userName;
    private String[] questions = {
            "1: What is the capital of France?",
            "2: What is 2 + 2?",
            "3: Who wrote 'Romeo and Juliet'?",
            "4: What is the largest planet in our solar system?",
            "5: What is the smallest country in the world?"
    };

    private String[][] options = {
            {"Berlin", "Madrid", "Paris", "Rome"},
            {"3", "4", "5", "6"},
            {"Shakespeare", "Dickens", "Austen", "Hemingway"},
            {"Earth", "Jupiter", "Mars", "Saturn"},
            {"Vatican City", "Monaco", "Nauru", "San Marino"}
    };

    private String[] correctAnswers = {
            "Paris", "4", "Shakespeare", "Jupiter", "Vatican City"
    };


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

        nameInputEditText = findViewById(R.id.nameEditText);
        startBtn = findViewById(R.id.startBtn);

        userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null) {

            nameInputEditText.setText(userName);
//            welcomeTextView.setText("Welcome back, " + userName + "!");
        }

        startBtn.setOnClickListener(v -> {
                userName = nameInputEditText.getText().toString().trim();
            if (!userName.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra("USER_NAME", userName);
                intent.putExtra("TOTAL_NUM_OF_QUESTION", 5);
                intent.putExtra("QUESTION",questions);
                intent.putExtra("OPTIONS",options);
                intent.putExtra("CORRECT_ANSWER",correctAnswers);

                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
            }
        });


    }


}