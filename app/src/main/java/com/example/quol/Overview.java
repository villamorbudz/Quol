package com.example.quol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;

public class Overview extends AppCompatActivity {
    
    TextView lastPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        lastPeriod = findViewById(R.id.lastPeriodText);
        
        findViewById(R.id.trackPeriodBtn).setOnClickListener(view -> {
            Intent openActivity = new Intent(Overview.this, TrackPeriod.class);
            finish();
            startActivity(openActivity);
        });

        findViewById(R.id.historyBtn).setOnClickListener(view -> {
            Intent openActivity = new Intent(Overview.this, PeriodHistory.class);
            finish();
            startActivity(openActivity);
        });

        displayLastPeriodDate();
    }

    private void displayLastPeriodDate() {
        // Get the current date
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        // Iterate through the stored data to find the most recent day with status != 0
        int daysAgo = 0;
        HashMap<Integer, HashMap<Integer, Integer>> yearHash = PeriodStatusManager.getYearHash().get(currentYear);
        if (yearHash != null) {
            // Iterate through months
            for (int month = currentMonth; month >= 0; month--) {
                HashMap<Integer, Integer> monthHash = yearHash.get(month);
                if (monthHash != null) {
                    // Get the last day of the month
                    int lastDayOfMonth = getLastDayOfMonth(currentYear, month);

                    // Iterate through days
                    for (int day = currentDay; day >= 1; day--) {
                        Integer status = monthHash.get(day);
                        if (status != null && status != 0) {
                            // Calculate days ago
                            Calendar lastPeriodDate = Calendar.getInstance();
                            lastPeriodDate.set(currentYear, month, day);
                            long timeDifference = currentDate.getTimeInMillis() - lastPeriodDate.getTimeInMillis();
                            daysAgo = (int) (timeDifference / (1000 * 60 * 60 * 24));
                            break;
                        }
                    }
                    if (daysAgo != 0) {
                        break;
                    }
                }
                // If month is not current month, set current day to last day of the previous month
                currentDay = getLastDayOfMonth(currentYear, month - 1);
            }
        }

        // Display the last period date
        String lastPeriodText = daysAgo > 0 ? daysAgo + " days ago" : "Unknown";
        lastPeriod.setText(lastPeriodText);
    }

    private int getLastDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}