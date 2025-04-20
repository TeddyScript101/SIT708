package com.example.itube;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {
    EditText usernameEditText, passwordEditText;
    Button loginBtn, signupBtn;
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DBHelper(requireContext());

        usernameEditText = view.findViewById(R.id.usernameInput);
        passwordEditText = view.findViewById(R.id.passwordInput);
        loginBtn = view.findViewById(R.id.loginBtn);
        signupBtn = view.findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {

                if (dbHelper.checkUserExists(username)) {
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("currentUsername", username);
                    editor.apply();


                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, new MenuFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(getContext(), "Invalid login credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupBtn.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.fragment_container, new SignupFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}


