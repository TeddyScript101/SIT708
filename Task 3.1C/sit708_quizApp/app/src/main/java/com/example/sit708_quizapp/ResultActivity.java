package com.example.sit708_quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class ResultActivity extends AppCompatActivity {

    int score;
    int totalQuestionNum;
    String userName;

    TextView congratTextView;

    TextView scoreTextView;

    Button newQuizButton;

    Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = getIntent().getStringExtra("USER_NAME");
        totalQuestionNum = getIntent().getIntExtra("TOTAL_NUM_OF_QUESTION", 0);
        score = getIntent().getIntExtra("SCORE", 0);

        congratTextView = findViewById(R.id.congratTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        newQuizButton = findViewById(R.id.newQuizBtn);
        finishButton = findViewById(R.id.finishBtn);

        congratTextView.setText("Congratulations\n" + userName);
        scoreTextView.setText(score + " / " + totalQuestionNum);

        newQuizButton.setOnClickListener(v -> {

            Intent intent = new Intent(ResultActivity.this, MainActivity.class);

            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
            finish();
        });

        finishButton.setOnClickListener(v -> {
            finishAffinity();
            System.exit(0);
        });

    }
}