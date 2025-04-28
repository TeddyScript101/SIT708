package com.example.self_learning;

import static com.example.self_learning.QuizApiHelper.answerLetterToIndex;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    public static ArrayList<Quiz> parseQuizJson(String jsonResponse) {
        ArrayList<Quiz> quizzes = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray quizArray = jsonObject.getJSONArray("quiz");

            for (int i = 0; i < quizArray.length(); i++) {
                JSONObject quizJson = quizArray.getJSONObject(i);

                String question = quizJson.getString("question");

                // Parse options
                JSONArray optionsJson = quizJson.getJSONArray("options");
                ArrayList<String> options = new ArrayList<>();
                for (int j = 0; j < optionsJson.length(); j++) {
                    options.add(optionsJson.getString(j));
                }

                String correctAnswer = quizJson.getString("correct_answer");
                int correctAnswerIndex = answerLetterToIndex(correctAnswer);
                // Create a new Quiz object and add it to the list
                Quiz quiz = new Quiz(question, options, correctAnswerIndex);
                quizzes.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return quizzes;
    }
}

