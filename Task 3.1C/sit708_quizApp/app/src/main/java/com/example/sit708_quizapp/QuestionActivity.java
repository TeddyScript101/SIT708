package com.example.sit708_quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuestionActivity extends AppCompatActivity {
    private String userName;
    private RadioGroup radioGroup;
    private RadioButton option1, option2, option3, option4;
    TextView displayWelcomeTextView;
    TextView progressTextView;

    TextView questionTextView;

    Button submitBtn;
    int currentQuestionNum = 0;

    int totalQuestionNum;

    String[] questions;
    String[][] options;

    String[] correctAnswers;

    int score = 0;

    boolean isAnswerSubmitted = false;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = getIntent().getStringExtra("USER_NAME");
        totalQuestionNum = getIntent().getIntExtra("TOTAL_NUM_OF_QUESTION", 0);
        questions = getIntent().getStringArrayExtra("QUESTION");
        options = (String[][]) getIntent().getSerializableExtra("OPTIONS");
        correctAnswers = getIntent().getStringArrayExtra("CORRECT_ANSWER");

        displayWelcomeTextView = findViewById(R.id.displayWelcomeText);
        progressBar = findViewById(R.id.progressBar);
        progressTextView = findViewById(R.id.progressTextView);
        questionTextView = findViewById(R.id.questionTitleTextView);
        radioGroup = findViewById(R.id.radioGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        submitBtn = findViewById(R.id.submitBtn);

        displayWelcomeTextView.setText("Welcome, " + userName + "!");
        progressTextView.setText(currentQuestionNum + 1 + " / " + totalQuestionNum);
        progressBar.setProgress(0);
        loadQuestion();

        submitBtn.setOnClickListener(v -> {
            RadioButton[] optionsList = {option1, option2, option3, option4};

            if (!isAnswerSubmitted) {
                int selectedOption = -1;
                for (int i = 0; i < optionsList.length; i++) {
                    if (optionsList[i].isChecked()) {
                        selectedOption = i;
                        break;
                    }
                }

                if (selectedOption == -1) {
                    Toast.makeText(QuestionActivity.this, "Please select an answer.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Disable all radio buttons after submitting the answer
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    radioGroup.getChildAt(i).setEnabled(false);
                }

                String correctAnswer = correctAnswers[currentQuestionNum];
                String selectedAnswer = options[currentQuestionNum][selectedOption];

                // Highlight the selected and correct answers
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    if (options[currentQuestionNum][i].equals(correctAnswer)) {
                        optionsList[i].setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
                    } else if (i == selectedOption) {
                        optionsList[i].setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
                    } else {
                        optionsList[i].setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
                    }
                }

                if (selectedAnswer.equals(correctAnswer)) {
                    score++;
                }

                submitBtn.setText(currentQuestionNum == totalQuestionNum - 1 ? "Finish" : "Next");
                isAnswerSubmitted = true;
            } else {
                currentQuestionNum++;
                if (currentQuestionNum >= totalQuestionNum) {
                    Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
                    intent.putExtra("SCORE", score);
                    intent.putExtra("TOTAL_NUM_OF_QUESTION", totalQuestionNum);
                    intent.putExtra("USER_NAME", userName);
                    startActivity(intent);
                    finish();
                } else {
                    radioGroup.clearCheck();
                    for (RadioButton rb : optionsList) {
                        rb.setEnabled(true);
                        rb.setTextColor(ContextCompat.getColor(this, android.R.color.black));
                        rb.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
                    }

                    progressTextView.setText((currentQuestionNum + 1) + " / " + totalQuestionNum);
                    loadQuestion();
                    submitBtn.setText("Submit");
                    isAnswerSubmitted = false;
                }
            }
        });



    }

    private void loadQuestion() {
        if (currentQuestionNum < questions.length) {

            questionTextView.setText(questions[currentQuestionNum]);

            option1.setText(options[currentQuestionNum][0]);
            option2.setText(options[currentQuestionNum][1]);
            option3.setText(options[currentQuestionNum][2]);
            option4.setText(options[currentQuestionNum][3]);

            progressBar.setProgress((currentQuestionNum + 1) * (100 / questions.length));
        }
    }
}