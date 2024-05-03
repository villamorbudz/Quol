package com.example.quol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.userLoginBtn).setOnClickListener(view -> {
            Intent openActivity = new Intent(MainActivity.this, login.class);
            startActivity(openActivity);
        });

        findViewById(R.id.userSignUpBtn).setOnClickListener(view -> {
            Intent openActivity = new Intent(MainActivity.this, signUp.class);
            startActivity(openActivity);
        });

        findViewById(R.id.homebtn).setOnClickListener(view -> {
            Intent openActivity = new Intent(MainActivity.this, PeriodHistory.class);
            startActivity(openActivity);
        });
    }


}