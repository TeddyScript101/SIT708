package com.example.self_learning;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QuizApiHelper {

    public static List<Quiz> fetchQuizzes(String interest) {
        List<Quiz> quizzes = new ArrayList<>();
        String apiUrl = "http://10.0.2.2:5001/getQuiz?topic=" + interest;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            in.close();

            // Parse JSON
            JSONObject jsonResponse = new JSONObject(result.toString());
            JSONArray questionsArray = jsonResponse.getJSONArray("quiz");

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObject = questionsArray.getJSONObject(i);
                String questionText = questionObject.getString("question");
                JSONArray optionsArray = questionObject.getJSONArray("options");
                String correctAnswer = questionObject.getString("correct_answer");

                List<String> options = new ArrayList<>();
                for (int j = 0; j < optionsArray.length(); j++) {
                    options.add(optionsArray.getString(j));
                }

                int correctIndex = answerLetterToIndex(correctAnswer);
                quizzes.add(new Quiz(questionText, options, correctIndex));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("QuizApiHelper", "Error fetching quiz data: " + e.getMessage());
            return null;
        }

        return quizzes;
    }

    static int answerLetterToIndex(String letter) {
        switch (letter) {
            case "A": return 0;
            case "B": return 1;
            case "C": return 2;
            case "D": return 3;
            default: return -1;
        }
    }
}
