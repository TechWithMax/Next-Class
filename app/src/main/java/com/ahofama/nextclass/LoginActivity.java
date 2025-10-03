package com.ahofama.nextclass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private ActivityResultLauncher<Intent> signInLauncher;
    private CircularProgressIndicator progressIndicator;
    private MaterialButton btnGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity); // Make sure this matches your XML filename

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize views
        initializeViews();

        // Check if user is already logged in
        checkExistingLogin();

        // Configure Google Sign-In
        configureGoogleSignIn();

        // Set up activity result launcher
        setupActivityResultLauncher();

        // Set up button click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        btnGoogleLogin = findViewById(R.id.btn_google_login);
        progressIndicator = findViewById(R.id.progress_indicator);

        // Initialize other buttons if needed
        MaterialButton btnEmailLogin = findViewById(R.id.btn_email_login);
        if (btnEmailLogin != null) {
            btnEmailLogin.setOnClickListener(v -> {
                // Handle email login - you can implement this later
                Toast.makeText(this, "Email login not implemented yet", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void checkExistingLogin() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, go to dashboard
            navigateToDashboard();
        }
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupActivityResultLauncher() {
        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    hideProgressIndicator();
                    if (result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleGoogleSignInResult(task);
                    } else {
                        Log.e("GoogleSignIn", "Sign-in result data is null");
                        Toast.makeText(this, "Sign-in cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setupClickListeners() {
        btnGoogleLogin.setOnClickListener(v -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        showProgressIndicator();
        Intent signInIntent = googleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d("GoogleSignIn", "Sign-in successful for: " + account.getEmail());
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Log.e("GoogleSignIn", "Sign-in failed with status code: " + e.getStatusCode(), e);
            String errorMessage = getGoogleSignInErrorMessage(e.getStatusCode());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private String getGoogleSignInErrorMessage(int statusCode) {
        switch (statusCode) {
            case 12501: // GoogleSignInStatusCodes.SIGN_IN_CANCELLED
                return "Sign-in was cancelled";
            case 12500: // GoogleSignInStatusCodes.SIGN_IN_FAILED
                return "Sign-in failed. Please try again";
            case 12502: // GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS
                return "Sign-in is already in progress";
            default:
                return "Google sign-in failed. Error code: " + statusCode;
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("FirebaseAuth", "Authenticating with Firebase using Google account");
        showProgressIndicator();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    hideProgressIndicator();
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Log.d("FirebaseAuth", "Firebase authentication successful");
                        if (user != null) {
                            Toast.makeText(this, "Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            navigateToDashboard();
                        }
                    } else {
                        Log.e("FirebaseAuth", "Firebase authentication failed", task.getException());
                        Toast.makeText(this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToDashboard() {
        // Check if DashboardActivity exists, otherwise use ProfileActivity
        Intent intent;
        try {
            intent = new Intent(this, DashboardActivity.class);
        } catch (Exception e) {
            // DashboardActivity doesn't exist, use ProfileActivity
            intent = new Intent(this, ProfileFragment2.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showProgressIndicator() {
        if (progressIndicator != null) {
            progressIndicator.setVisibility(android.view.View.VISIBLE);
        }
        btnGoogleLogin.setEnabled(false);
        btnGoogleLogin.setText("Signing in...");
    }

    private void hideProgressIndicator() {
        if (progressIndicator != null) {
            progressIndicator.setVisibility(android.view.View.GONE);
        }
        btnGoogleLogin.setEnabled(true);
        btnGoogleLogin.setText(getString(R.string.sign_in_google));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in when activity starts
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToDashboard();
        }
    }
}