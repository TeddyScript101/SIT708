package com.example.self_learning;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuestionActivity extends AppCompatActivity {

    private TextView interestTextView;
    private LinearLayout questionContainer;
    private ProgressBar loadingSpinner;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private HashMap<Integer, Integer> userAnswers = new HashMap<>();
    private List<Quiz> quizList = new ArrayList<>();
    private ArrayList<RadioGroup> radioGroupList = new ArrayList<>();
    private Button submitButton;
    private boolean submitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        String interest = getIntent().getStringExtra("interest");

        interestTextView = findViewById(R.id.interestTitleTextView);
        questionContainer = findViewById(R.id.questionContainer);
        loadingSpinner = findViewById(R.id.loadingSpinner);

        interestTextView.setText("This is a quiz related to " + interest);

        fetchQuizData(interest);
    }

    private void fetchQuizData(final String interest) {
        // Show the spinner while data is loading
        loadingSpinner.setVisibility(View.VISIBLE);

        executorService.submit(() -> {
            List<Quiz> quizzes = QuizApiHelper.fetchQuizzes(interest);

            if (quizzes != null) {
                quizList = quizzes;
                new Handler(Looper.getMainLooper()).post(this::displayQuestions);
            } else {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(QuestionActivity.this, "Failed to fetch quiz data", Toast.LENGTH_SHORT).show();
                    loadingSpinner.setVisibility(View.GONE);
                });
            }
        });
    }

    private void displayQuestions() {
       
        loadingSpinner.setVisibility(View.GONE);

        for (int i = 0; i < quizList.size(); i++) {
            Quiz quiz = quizList.get(i);

            LinearLayout questionLayout = new LinearLayout(this);
            questionLayout.setOrientation(LinearLayout.VERTICAL);
            questionLayout.setPadding(24, 24, 24, 24);

            GradientDrawable background = new GradientDrawable();
            background.setColor(Color.parseColor("#FFFFFF"));
            background.setCornerRadius(24);
            questionLayout.setBackground(background);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 24, 0, 0);
            questionLayout.setLayoutParams(params);

            TextView questionTextView = new TextView(this);
            questionTextView.setText((i + 1) + ". " + quiz.getQuestion());
            questionTextView.setTextSize(18);
            questionTextView.setPadding(0, 0, 0, 16);
            questionLayout.addView(questionTextView);

            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(RadioGroup.VERTICAL);

            List<String> options = quiz.getOptions();
            for (int j = 0; j < options.size(); j++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(options.get(j));
                radioButton.setId(j);
                radioGroup.addView(radioButton);
            }

            final int questionIndex = i;
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                userAnswers.put(questionIndex, checkedId);
            });

            questionLayout.addView(radioGroup);

            questionContainer.addView(questionLayout);
            radioGroupList.add(radioGroup);
        }

        submitButton = new Button(this);
        submitButton.setText("Submit");
        submitButton.setBackgroundColor(Color.parseColor("#4CAF50"));
        submitButton.setTextColor(Color.WHITE);
        submitButton.setPadding(0, 32, 0, 32);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(0, 48, 0, 48);
        submitButton.setLayoutParams(buttonParams);

        submitButton.setOnClickListener(v -> {
            if (!submitted) {
                checkAnswers();
                submitted = true;
                submitButton.setText("Continue");
                interestTextView.setText("Result");
                interestTextView.setTextColor(Color.BLACK);
                interestTextView.setTextSize(30);
                interestTextView.setTypeface(null, android.graphics.Typeface.BOLD);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) interestTextView.getLayoutParams();
                params.setMargins(0, 100, 0, 0); // Top margin increased to 100
                interestTextView.setLayoutParams(params);
            } else {
                Intent intent = new Intent(QuestionActivity.this, QuizHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        questionContainer.addView(submitButton);
    }

    private void checkAnswers() {
        for (int i = 0; i < radioGroupList.size(); i++) {
            RadioGroup group = radioGroupList.get(i);
            int selectedId = userAnswers.getOrDefault(i, -1);
            int correctId = quizList.get(i).getCorrectAnswerIndex();

            for (int j = 0; j < group.getChildCount(); j++) {
                RadioButton radioButton = (RadioButton) group.getChildAt(j);
                radioButton.setEnabled(false);

                if (j == correctId) {
                    radioButton.setTextColor(Color.GREEN);
                } else if (j == selectedId && selectedId != correctId) {
                    radioButton.setTextColor(Color.RED);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
