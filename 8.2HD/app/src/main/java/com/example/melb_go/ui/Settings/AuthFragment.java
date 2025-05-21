package com.example.melb_go.ui.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.melb_go.MainActivity;
import com.example.melb_go.R;
import com.example.melb_go.repository.AuthRepository;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthFragment extends Fragment {

    private EditText emailInput, passwordInput, confirmPasswordInput;
    private Button authButton, logoutButton;
    private TextView toggleText;

    private boolean isLoginMode = true;

    public AuthFragment() {
        // Required empty constructor
    }

    private String getToken() {
        return getContext().getSharedPreferences("auth_prefs", getContext().MODE_PRIVATE)
                .getString("auth_token", null);
    }

    private void saveToken(String token) {
        if (token == null) return;
        getContext().getSharedPreferences("auth_prefs", getContext().MODE_PRIVATE)
                .edit()
                .putString("auth_token", token)
                .apply();
    }

    private void clearToken() {
        getContext().getSharedPreferences("auth_prefs", getContext().MODE_PRIVATE)
                .edit()
                .remove("auth_token")
                .apply();
    }

    private void navigateToHome() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).switchToHomeTab();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        emailInput = view.findViewById(R.id.editTextEmail);
        passwordInput = view.findViewById(R.id.editTextPassword);
        confirmPasswordInput = view.findViewById(R.id.editTextConfirmPassword);
        authButton = view.findViewById(R.id.buttonAuth);
        toggleText = view.findViewById(R.id.textToggleMode);
        logoutButton = view.findViewById(R.id.buttonLogout);

        String token = getToken();
        if (token != null) {
            // User is logged in, show logout button
            showLogoutUI();
        } else {
            // User not logged in, show login/signup UI
            updateUI();
        }

        authButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            String confirm = confirmPasswordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty() || (!isLoginMode && confirm.isEmpty())) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isLoginMode && !password.equals(confirm)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthRepository authRepository = new AuthRepository();

            if (isLoginMode) {
                authRepository.login(email, password, new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            String token = response.body().getToken();
                            saveToken(token);
                            Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                            navigateToHome();
                            showLogoutUI();
                        } else {
                            String errorMsg = "Unknown error";
                            try {
                                if (response.errorBody() != null) {
                                    String errorJson = response.errorBody().string();
                                    JSONObject jsonObject = new JSONObject(errorJson);
                                    errorMsg = jsonObject.optString("message", "Unknown error");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getContext(), "Login failed: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Login error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                authRepository.signup(email, password, new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            Toast.makeText(getContext(), "Signup successful", Toast.LENGTH_SHORT).show();
                            toggleMode(); // Switch to login mode
                        } else {
                            Toast.makeText(getContext(), "Signup failed: " + (response.body() != null ? response.body().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Signup error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        toggleText.setOnClickListener(v -> toggleMode());

        logoutButton.setOnClickListener(v -> {
            clearToken();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            // Switch back to login/signup UI
            isLoginMode = true;
            updateUI();
            logoutButton.setVisibility(View.GONE);
            authButton.setVisibility(View.VISIBLE);
            toggleText.setVisibility(View.VISIBLE);
            emailInput.setText("");
            passwordInput.setText("");
            confirmPasswordInput.setText("");
        });

        return view;
    }

    private void showLogoutUI() {
        // Hide login/signup fields and buttons
        emailInput.setVisibility(View.GONE);
        passwordInput.setVisibility(View.GONE);
        confirmPasswordInput.setVisibility(View.GONE);
        authButton.setVisibility(View.GONE);
        toggleText.setVisibility(View.GONE);

        // Show logout button
        logoutButton.setVisibility(View.VISIBLE);
    }

    private void toggleMode() {
        isLoginMode = !isLoginMode;
        updateUI();
    }

    private void updateUI() {
        emailInput.setVisibility(View.VISIBLE);
        passwordInput.setVisibility(View.VISIBLE);

        if (isLoginMode) {
            authButton.setText("Login");
            toggleText.setText("Don't have an account? Sign up");
            confirmPasswordInput.setVisibility(View.GONE);
        } else {
            authButton.setText("Sign Up");
            toggleText.setText("Already have an account? Login");
            confirmPasswordInput.setVisibility(View.VISIBLE);
        }

        authButton.setVisibility(View.VISIBLE);
        toggleText.setVisibility(View.VISIBLE);
        logoutButton.setVisibility(View.GONE);
    }
}
