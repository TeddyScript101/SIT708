package com.example.self_learning;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PlansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        Button btnStarter = findViewById(R.id.btnStarter);
        Button btnIntermediate = findViewById(R.id.btnIntermediate);
        Button btnAdvanced = findViewById(R.id.btnAdvanced);

        btnStarter.setOnClickListener(v -> goToCheckout("Starter Plan", 10));
        btnIntermediate.setOnClickListener(v -> goToCheckout("Intermediate Plan", 20));
        btnAdvanced.setOnClickListener(v -> goToCheckout("Advanced Plan", 30));
    }

    private void goToCheckout(String planName, int amount) {
        Intent intent = new Intent(PlansActivity.this, CheckoutActivity.class);
        intent.putExtra("planName", planName);
        intent.putExtra("amount", amount);
        startActivity(intent);
    }
}
