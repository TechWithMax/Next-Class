package com.ahofama.nextclass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if onboarding was completed before
        SharedPreferences prefs = getSharedPreferences("NextClassPrefs", MODE_PRIVATE);
        boolean isOnboardingCompleted = prefs.getBoolean("onboarding_completed", false);

        if (isOnboardingCompleted) {
            navigateToMain();
            return;
        }

        // Enable Edge-to-Edge
        EdgeToEdge.enable(this);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.onboarding_activity);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.onboarding), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get Started Button
        Button getStartedButton = findViewById(R.id.btn_get_started);
        getStartedButton.setOnClickListener(v -> {
            // Save onboarding as completed
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("onboarding_completed", true);
            editor.apply();

            // Navigate to MainActivity
            navigateToMain();
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
