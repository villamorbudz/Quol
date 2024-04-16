package com.example.quol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView signUpRedirect = findViewById(R.id.signUpRedirect);

        signUpRedirect.setOnClickListener(view -> {
            Intent redirect = new Intent(login.this, signUp.class);
            startActivity(redirect);
            finish();
        });
    }
}