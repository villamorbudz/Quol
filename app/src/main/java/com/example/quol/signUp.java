package com.example.quol;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class signUp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button birthdateBtn = findViewById(R.id.userAuth_birthdateBtn);
        birthdateBtn.setOnClickListener(view -> openDatePicker());
        TextView loginRedirect = findViewById(R.id.loginRedirect);
        loginRedirect.setOnClickListener(view -> {
            Intent redirect = new Intent(signUp.this, login.class);
            startActivity(redirect);
            finish();
        });
    }

    private void openDatePicker() {
        TextView birthdateText = findViewById(R.id.signUp_birthdateText);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,
                (datePicker, selectedYear, selectedMonth, selectedDay) ->
                        birthdateText.setText((selectedMonth+1) + "/" + selectedDay + "/" + selectedYear),
                year, month, day);

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        dialog.show();
    }
}
