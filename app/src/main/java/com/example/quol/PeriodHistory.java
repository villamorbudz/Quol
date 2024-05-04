package com.example.quol;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class PeriodHistory extends AppCompatActivity {

    ArrayList<Button> baseButtons;
    ArrayList<Button> dayButtons;

    Button prevMonth;
    Button nextMonth;
    int currMonth;
    int currYear;
    int currDay;
    int dayOffset;

    // Year -> Month -> Day -> Status
    HashMap<Integer,HashMap<Integer,HashMap<Integer,Integer>>> yearHash;
    HashMap<Integer,Integer> finalDayHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_history);

        // Set the maximum selectable date to today
        baseButtons = new ArrayList<>();
        dayButtons = new ArrayList<>();

        for (int i = 1; i <= 42; i++) {
            int resId = getResources().getIdentifier("D" + i, "id", getPackageName());
            baseButtons.add(findViewById(resId));
        }

        Calendar cd = Calendar.getInstance();
        yearHash = new HashMap<>();
        currYear = cd.get(Calendar.YEAR);
        currMonth = cd.get(Calendar.MONTH);
        currDay = cd.get(Calendar.DATE);
        moveMonth(currYear, currMonth);

        nextMonth = findViewById(R.id.NextMonthBtn);
        prevMonth = findViewById(R.id.PrevMonthBtn);

        nextMonth.setOnClickListener(v -> setMonth(+1));
        prevMonth.setOnClickListener(v -> setMonth(-1));

        findViewById(R.id.trackPeriodBtn).setOnClickListener(view -> {
            Intent openActivity = new Intent(PeriodHistory.this, TrackPeriod.class);
            finish();
            startActivity(openActivity);
        });

        findViewById(R.id.overviewBtn).setOnClickListener(view -> {
            Intent openActivity = new Intent(PeriodHistory.this, Overview.class);
            finish();
            startActivity(openActivity);
        });
    }

    private void moveMonth(int year, int month) {
        TextView myText = findViewById(R.id.MYtext);
        String[] monthsString = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        myText.setText(monthsString[month] + " " + year);
        finalDayHash = getStatus(year, month);

        Calendar cal = new GregorianCalendar(year, month, 1);
        int startDayInWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOffset = startDayInWeek;
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (Button btn : baseButtons) {
            btn.setText("");
            btn.setVisibility(View.INVISIBLE);
            btn.setEnabled(false);
        }

        dayButtons = new ArrayList<>();

        for (int x = 0; x < daysInMonth; x++) {
            dayButtons.add(baseButtons.get(startDayInWeek + x - 1));
        }

        for (int x = 0; x < daysInMonth; x++) {
            Button btn = dayButtons.get(x);
            btn.setText(Integer.toString(x + 1));
            btn.setTextColor(Color.BLACK);
            btn.setVisibility(View.VISIBLE);

            int status = finalDayHash.getOrDefault(x + 1, 0);
            switch (status) {
                case 0: // None
                    btn.setBackgroundColor(Color.TRANSPARENT); // No background color
                    break;
                case 1: // Light
                    btn.setBackgroundColor(Color.parseColor("#80FF0000")); // Light-opacity red
                    btn.setTextColor(Color.WHITE);
                    break;
                case 2: // Medium
                    btn.setBackgroundColor(Color.RED); // Medium-opacity red
                    btn.setTextColor(Color.WHITE);
                    break;
                case 3: // Heavy
                    btn.setBackgroundColor(Color.parseColor("#FF8B0000")); // Red
                    btn.setTextColor(Color.WHITE);
                    break;
                default:
                    break;
            }
        }
    }

    private void setMonth(int Month){
        currMonth = Month + currMonth;
        if(currMonth == 0 && Month == -1){          //lazy fix
            currMonth = 11;
            currYear -= 1;
        }
        if(currMonth / 12 > 0){
            currYear += Month;
            currMonth %= 2;
        }
        moveMonth(currYear, currMonth);
    }

    private HashMap<Integer, Integer> getStatus(int year, int month) {
        HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> yearHash = PeriodStatusManager.getYearHash();
        return yearHash
                .computeIfAbsent(year, k -> new HashMap<>())
                .computeIfAbsent(month, k -> new HashMap<>());
    }
}
