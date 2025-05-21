package com.example.self_learning;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChooseYourInterestActivity extends AppCompatActivity {

    private String username, email, password, phone;
    private byte[] avatarBytes;
    private DBHelper dbHelper;
    private ChipGroup interestChipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_interest);


        dbHelper = new DBHelper(this);


        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone");
        avatarBytes = getIntent().getByteArrayExtra("avatarBytes");


        interestChipGroup = findViewById(R.id.interestChipGroup);
        Button continueButton = findViewById(R.id.continueButton);


        Toast.makeText(this, "Welcome " + username + ", please choose your interests.", Toast.LENGTH_LONG).show();

        continueButton.setOnClickListener(v -> {

            ArrayList<String> selectedInterests = new ArrayList<>();
            for (int i = 0; i < interestChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) interestChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    selectedInterests.add(chip.getText().toString());
                }
            }


            if (selectedInterests.isEmpty()) {
                Toast.makeText(ChooseYourInterestActivity.this, "Please select at least one interest.", Toast.LENGTH_SHORT).show();
            } else {

                boolean isSuccess = dbHelper.insertUser(username, email, password, phone, avatarBytes, selectedInterests);

                if (isSuccess) {

                    Intent intent = new Intent(ChooseYourInterestActivity.this, MainActivity.class); // Replace with your next activity
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(ChooseYourInterestActivity.this, "Sign-up failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
