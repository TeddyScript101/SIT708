package com.example.self_learning;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.animation.ObjectAnimator;

import java.util.ArrayList;

public class QuizHomeActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private LinearLayout quizContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);

        String username = getIntent().getStringExtra("username");
        if (username == null) {
            Toast.makeText(this, "Username missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DBHelper(this);
        quizContainer = findViewById(R.id.quizContainer);

        TextView greetingText = findViewById(R.id.greetingText);
        greetingText.setText("Hello,\n" + username);

        ArrayList<String> userInterests = dbHelper.getUserInterests(username);
        int taskCount = userInterests.size();

        TextView taskDueText = findViewById(R.id.taskDueText);
        taskDueText.setText("You have " + taskCount + " tasks due");

        if (userInterests.isEmpty()) {
            Toast.makeText(this, "No interests found.", Toast.LENGTH_SHORT).show();
        } else {
            for (String interest : userInterests) {
                View cardView = LayoutInflater.from(this).inflate(R.layout.quiz_card, quizContainer, false);

                TextView descriptionTextView = cardView.findViewById(R.id.quizDescription);
                descriptionTextView.setText("This is a quiz related to " + interest);

                TextView titleTextView = cardView.findViewById(R.id.quizTitle);
                titleTextView.setText("Quiz on " + interest);

                Button startQuizButton = cardView.findViewById(R.id.startQuizButton);
                startQuizButton.setOnClickListener(v -> {
                    Intent intent = new Intent(QuizHomeActivity.this, QuestionActivity.class);
                    intent.putExtra("interest", interest);
                    startActivity(intent);
                });

                quizContainer.addView(cardView);

                ObjectAnimator animator = ObjectAnimator.ofFloat(cardView, "translationY", 1000f, 0f);
                animator.setDuration(1000);
                animator.start();
            }
        }
    }
}
