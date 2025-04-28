package com.example.self_learning;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SignUpActivity extends AppCompatActivity {

    private Uri avatarUri;

    // Define the launcher for picking images
    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    avatarUri = result.getData().getData();
                    ImageView avatarImageView = findViewById(R.id.avatarImageView);
                    avatarImageView.setImageURI(avatarUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView avatarImageView = findViewById(R.id.avatarImageView);
        avatarImageView.setOnClickListener(v -> openImagePicker());

        Button signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(v -> handleSignup());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);  // Launch the image picker
    }

    private void handleSignup() {
        EditText usernameField = findViewById(R.id.usernameInput);
        EditText emailField = findViewById(R.id.emailInput);
        EditText confirmEmailField = findViewById(R.id.confirmEmailInput);
        EditText passwordField = findViewById(R.id.passwordInput);
        EditText confirmPasswordField = findViewById(R.id.confirmPasswordInput);
        EditText phoneField = findViewById(R.id.phoneInput);

        String username = usernameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String confirmEmail = confirmEmailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (confirmEmail.isEmpty()) {
            Toast.makeText(this, "Confirm Email is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.equals(confirmEmail)) {
            Toast.makeText(this, "Email and Confirm Email do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Confirm Password is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] avatarBytes = null;
        try {
            if (avatarUri != null) {
                InputStream inputStream = getContentResolver().openInputStream(avatarUri);
                avatarBytes = getBytes(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(SignUpActivity.this, ChooseYourInterestActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("phone", phone);
        intent.putExtra("avatarBytes", avatarBytes);

        startActivity(intent);


//        DBHelper dbHelper = new DBHelper(this);
//        boolean success = dbHelper.insertUser(username, email, password, phone, avatarBytes);
//
//        if (success) {
//            Toast.makeText(this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
//            finish(); // go back or navigate to login
//        } else {
//            Toast.makeText(this, "Error registering user.", Toast.LENGTH_SHORT).show();
//        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
