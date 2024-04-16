package com.example.quol;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;

public class signUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button birthdateBtn = findViewById(R.id.userAuth_birthdateBtn);
        TextView loginRedirect = findViewById(R.id.loginRedirect);

        birthdateBtn.setOnClickListener(view -> openDatePicker());

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
                        birthdateText.setText(month+1 + "/" + day + "/" + year),
                year, month, day);
        dialog.show();
    }
}
