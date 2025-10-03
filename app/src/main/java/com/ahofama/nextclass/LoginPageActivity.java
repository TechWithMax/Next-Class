package com.ahofama.nextclass;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ahofama.nextclass.database.DBHelper;

public class LoginPageActivity extends AppCompatActivity{
    TextView signUpText;
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvSignUp;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This links the Java class with your login_page.xml
        setContentView(R.layout.login_page);


        // Initialize views
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignUp = findViewById(R.id.sign_up);

        dbHelper = new DBHelper(this);

        // Login button
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (dbHelper.checkUser(username, pass)) {
                // ✅ Get user details (username + email)
                Cursor cursor = dbHelper.getUserDetails(username);
                if (cursor != null && cursor.moveToFirst()) {
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                    Intent intent = new Intent(LoginPageActivity.this, DashboardActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(LoginPageActivity.this, "Invalid login", Toast.LENGTH_SHORT).show();
            }
        });

        signUpText = findViewById(R.id.sign_up);

        // Example after successful login
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("username", "Admin"); // example, store user data
        editor.apply();

        // Navigate to RegisterActivity
        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPageActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
