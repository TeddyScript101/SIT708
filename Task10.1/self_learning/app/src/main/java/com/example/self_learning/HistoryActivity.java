package com.example.self_learning;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HistoryActivity extends AppCompatActivity {
    LinearLayout historyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyContainer = findViewById(R.id.historyContainer);
        fetchHistory();
    }

    private void fetchHistory() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:5001/history");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) response.append(line);
                in.close();

                JSONObject responseJson = new JSONObject(response.toString());
                JSONArray quizs = responseJson.getJSONArray("quizs");

                runOnUiThread(() -> displayQuizzes(quizs));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void displayQuizzes(JSONArray quizs) {
        try {
            for (int i = 0; i < quizs.length(); i++) {
                JSONObject quiz = quizs.getJSONObject(i);

                // Create bordered container for one quiz
                LinearLayout quizBox = new LinearLayout(this);
                quizBox.setOrientation(LinearLayout.VERTICAL);
                quizBox.setBackgroundResource(R.drawable.quiz_border);
                quizBox.setPadding(24, 24, 24, 24);

                LinearLayout.LayoutParams boxParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                boxParams.setMargins(0, 16, 0, 16);
                quizBox.setLayoutParams(boxParams);

                // Topic title
                TextView topicView = new TextView(this);
                topicView.setText(quiz.getString("topic"));
                topicView.setTextSize(20);
                topicView.setTypeface(null, Typeface.BOLD);
                topicView.setPadding(0, 0, 0, 8);
                quizBox.addView(topicView);

                JSONArray record = quiz.getJSONArray("record");
                for (int j = 0; j < record.length(); j++) {
                    JSONObject q = record.getJSONObject(j);

                    TextView questionView = new TextView(this);
                    questionView.setText("Q: " + q.getString("question"));
                    questionView.setPadding(0, 8, 0, 4);
                    quizBox.addView(questionView);

                    JSONArray options = q.getJSONArray("options");
                    for (int k = 0; k < options.length(); k++) {
                        String optionText = (char) ('A' + k) + ". " + options.getString(k);
                        TextView optionView = new TextView(this);
                        optionView.setText(optionText);
                        optionView.setPadding(16, 0, 0, 4);

                        String correct = q.getString("correct_answer");
                        String user = q.getString("user_answer");

                        if (String.valueOf((char) ('A' + k)).equals(correct)) {
                            optionView.setTextColor(Color.parseColor("#4CAF50")); // green
                        }
                        if (String.valueOf((char) ('A' + k)).equals(user)
                                && !user.equals(correct)) {
                            optionView.setTextColor(Color.parseColor("#F44336")); // red
                        }

                        quizBox.addView(optionView);
                    }
                }

                // Add bordered quiz box to main container
                historyContainer.addView(quizBox);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
