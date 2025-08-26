package com.ahofama.nextclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        /*Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(v -> {
            // Navigate to MainActivity after login
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);*
            finish();
        });*/
    }
}
