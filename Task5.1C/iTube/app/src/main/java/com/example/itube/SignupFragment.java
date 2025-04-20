package com.example.itube;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SignupFragment extends Fragment {
    EditText usernameInput, passwordInput, confirmPasswordInput;
    Button signupBtn;

    Button backBtn;
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        dbHelper = new DBHelper(requireContext());

        usernameInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput);
        signupBtn = view.findViewById(R.id.signupBtn);
        backBtn = view.findViewById(R.id.backBtn); // reference to the back button

        // Back button functionality
        backBtn.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment()) // Replace with HomeFragment
                    .addToBackStack(null) // Optional: Add to back stack if you want to use back navigation
                    .commit();
        });

        signupBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.checkUserExists(username)) {
                Toast.makeText(getContext(), "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = dbHelper.addUser(username, password);
                if (success) {
                    Toast.makeText(getContext(), "User registered!", Toast.LENGTH_SHORT).show();
                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}

