package com.example.self_learning;

import java.util.List;

public class Quiz {
    private String question;
    private List<String> options;
    private int correctAnswerIndex;

    public Quiz(String question, List<String> options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
