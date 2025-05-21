package com.example.self_learning;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {
    TextView profileName, profileEmail, scoreText, quizCountText, correctText, incorrectText, totalQuestionsText;
    Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        quizCountText = findViewById(R.id.quizCount);
        scoreText = findViewById(R.id.userScore);
        correctText = findViewById(R.id.correctAnswers);
        incorrectText = findViewById(R.id.incorrectAnswers);
        shareButton = findViewById(R.id.shareButton);
        totalQuestionsText = findViewById(R.id.totalQuestions);


        String username = getIntent().getStringExtra("username");
        if (username != null) profileName.setText(username);

        shareButton.setOnClickListener(v -> shareProgress());

        fetchProfileData();
    }

    private void fetchProfileData() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:5001/profile");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) response.append(line);
                in.close();

                JSONObject json = new JSONObject(response.toString());
                int quizCount = json.getInt("numberOfQuizzes");
                double score = json.getDouble("score");
                int correct = json.getInt("correctAnswer");
                int incorrect = json.getInt("incorrectAnswer");
                int totalQuestions = json.getInt("totalQuestions");

                new Handler(Looper.getMainLooper()).post(() -> {
                    quizCountText.setText(quizCount + "");
                    scoreText.setText("Score: " + (int) (score * 100) + "%");
                    correctText.setText(correct + "");
                    incorrectText.setText("Incorrect Answers: " + incorrect);
                    totalQuestionsText.setText(totalQuestions + "");
                });

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(this, "Failed to load profile data", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void shareProgress() {
        String shareText = "Check out my learning progress! I'm using Self Learning App to improve my skills";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Learning Progress");

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        } else {
            Toast.makeText(this, "No sharing apps available", Toast.LENGTH_SHORT).show();
        }
    }
}
