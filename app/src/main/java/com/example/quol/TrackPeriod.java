package com.example.quol;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class TrackPeriod extends AppCompatActivity {
    HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> YearHash;
    CalendarView periodHistoryCalendar;
    TextView selectedDateText;

    int currentMonth;
    int currentYear;
    int currentDay;
    Button noBleedingBtn;
    Button lightBleedingBtn;
    Button mediumBleedingBtn;
    Button heavyBleedingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_period);

        findViewById(R.id.historyBtn).setOnClickListener(view -> {
            Intent openActivity = new Intent(TrackPeriod.this, PeriodHistory.class);
            finish();
            startActivity(openActivity);
        });

        findViewById(R.id.overviewBtn).setOnClickListener(view -> {
            Intent openActivity = new Intent(TrackPeriod.this, Overview.class);
            finish();
            startActivity(openActivity);
        });

        Calendar cd = Calendar.getInstance();
        currentYear = cd.get(Calendar.YEAR);
        currentMonth = cd.get(Calendar.MONTH);
        currentDay = cd.get(Calendar.DATE);
        YearHash = new HashMap<>();

        periodHistoryCalendar = findViewById(R.id.periodHistoryCalendar);
        selectedDateText = findViewById(R.id.selectedDateTv);
        noBleedingBtn = findViewById(R.id.noBleedingBtn);
        lightBleedingBtn = findViewById(R.id.lightBleedingBtn);
        mediumBleedingBtn = findViewById(R.id.mediumBleedingBtn);
        heavyBleedingBtn = findViewById(R.id.heavyBleedingBtn);

        noBleedingBtn.setText("");
        lightBleedingBtn.setText("");
        mediumBleedingBtn.setText("");
        heavyBleedingBtn.setText("");

        noBleedingBtn.setOnClickListener(v -> {
            setStatus(currentYear, currentMonth, currentDay, 0);
        });
        lightBleedingBtn.setOnClickListener(v -> {
            setStatus(currentYear, currentMonth, currentDay, 1);
        });
        mediumBleedingBtn.setOnClickListener(v -> {
            setStatus(currentYear, currentMonth, currentDay, 2);
        });
        heavyBleedingBtn.setOnClickListener(v -> {
            setStatus(currentYear, currentMonth, currentDay, 3);
        });

        setCurrentDay();
        setButtonStatus(currentYear, currentMonth, currentDay);

        periodHistoryCalendar.setMaxDate(System.currentTimeMillis());
        periodHistoryCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            currentYear = year;
            currentMonth = month;
            currentDay = dayOfMonth;

            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.US);
            String formattedDate = sdf.format(selectedDate.getTime());

            selectedDateText.setText(formattedDate);
            setButtonStatus(currentYear, currentMonth, currentDay);
        });
    }

    private void setStatus(int year, int month, int day, int status) {
        HashMap<Integer, HashMap<Integer, Integer>> monthHash = PeriodStatusManager.getYearHash().get(year);

        if (monthHash == null) {
            monthHash = new HashMap<>();
            YearHash.put(year, monthHash);
        }
        HashMap<Integer, Integer> dayHash = monthHash.get(month);
        if (dayHash == null) {
            dayHash = new HashMap<>();
            monthHash.put(month, dayHash);
        }

        dayHash.put(day, status);
        setButtonStatus(year, month, day);
    }

    private void setButtonStatus(int year, int month, int day) {
        noBleedingBtn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        lightBleedingBtn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        mediumBleedingBtn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        heavyBleedingBtn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));

        HashMap<Integer, HashMap<Integer, Integer>> monthHash = PeriodStatusManager.getYearHash().get(year);
        if (monthHash != null) {
            HashMap<Integer, Integer> dayHash = monthHash.get(month);
            if (dayHash != null) {
                Integer status = dayHash.get(day);
                if (status != null) {
                    switch (status) {
                        case 1:
                            lightBleedingBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CBAD8D")));
                            break;
                        case 2:
                            mediumBleedingBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CBAD8D")));
                            break;
                        case 3:
                            heavyBleedingBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CBAD8D")));
                            break;
                        default:
                            // No bleeding case
                            noBleedingBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CBAD8D")));
                            break;
                    }
                } else {
                    // If status is null, set it to 0 automatically for said day
                    setStatus(year, month, day, 0);
                }
            }
        } else {
            noBleedingBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CBAD8D")));
            // If monthHash is null, create a new one and set the status to 0 for said day
            monthHash = new HashMap<>();
            PeriodStatusManager.getYearHash().put(year, monthHash);
            setStatus(year, month, day, 0);
        }
    }

    private void setCurrentDay() {
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.US);
        String formattedDate = sdf.format(currentDate.getTime());
        selectedDateText.setText(formattedDate);
    }
}
