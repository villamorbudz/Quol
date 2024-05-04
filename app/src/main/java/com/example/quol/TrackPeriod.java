package com.example.quol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class TrackPeriod extends AppCompatActivity {
    HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> YearHash;
    CalendarView periodHistoryCalendar;
    TextView selectedDateText;
    RadioGroup bleedingStatus;

    int CurrMonth;
    int CurrYear;
    int CurrDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_period);

        Calendar cd = Calendar.getInstance();
        CurrYear = cd.get(Calendar.YEAR);
        CurrMonth = cd.get(Calendar.MONTH);
        CurrDay = cd.get(Calendar.DATE);
        YearHash = new HashMap<>();

        final int noBleedingRbId = R.id.noBleedingRb;
        final int lightBleedingRbId = R.id.lightBleedingRb;
        final int mediumBleedingRbId = R.id.mediumBleedingRb;
        final int heavyBleedingRbId = R.id.heavyBleedingRb;

        periodHistoryCalendar = findViewById(R.id.periodHistoryCalendar);
        selectedDateText = findViewById(R.id.selectedDateTv);
        bleedingStatus = findViewById(R.id.bleedingRateRb);

        bleedingStatus.check(R.id.noBleedingRb);

        findViewById(R.id.historyBtn).setOnClickListener(view -> {
            Intent openActivity = new Intent(TrackPeriod.this, PeriodHistory.class);
            finish();
            startActivity(openActivity);
        });

        setCurrentDay();
        periodHistoryCalendar.setMaxDate(System.currentTimeMillis());
        periodHistoryCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            CurrYear = year;
            CurrMonth = month;
            CurrDay = dayOfMonth;
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.US);
            String formattedDate = sdf.format(selectedDate.getTime());
            selectedDateText.setText(formattedDate);
            setSelectedStatus(year, month, dayOfMonth);
        });

        bleedingStatus.setOnCheckedChangeListener((group, checkedId) -> {
            int status = 0;
            if (checkedId == noBleedingRbId) {
                status = 0;
            } else if (checkedId == lightBleedingRbId) {
                status = 1;
            } else if (checkedId == mediumBleedingRbId) {
                status = 2;
            } else if (checkedId == heavyBleedingRbId) {
                status = 3;
            }

            setStatus(CurrYear, CurrMonth, CurrDay, status);
            Toast.makeText(TrackPeriod.this, "Period Symptom Recorded", Toast.LENGTH_SHORT).show();
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
    }

    private void setSelectedStatus(int year, int month, int day) {
        HashMap<Integer, HashMap<Integer, Integer>> monthHash = PeriodStatusManager.getYearHash().get(year);

        if (monthHash != null) {
            HashMap<Integer, Integer> dayHash = monthHash.get(month);
            if (dayHash != null) {
                Integer status = dayHash.get(day);
                if (status != null) {
                    bleedingStatus.setOnCheckedChangeListener(null); // Remove listener temporarily
                    bleedingStatus.check(getBleedingStatusRadioButtonId(status));
                    bleedingStatus.setOnCheckedChangeListener((group, checkedId) -> {
                        int newStatus = 0;
                        if (checkedId == R.id.noBleedingRb) {
                            newStatus = 0;
                        } else if (checkedId == R.id.lightBleedingRb) {
                            newStatus = 1;
                        } else if (checkedId == R.id.mediumBleedingRb) {
                            newStatus = 2;
                        } else if (checkedId == R.id.heavyBleedingRb) {
                            newStatus = 3;
                        }
                        setStatus(year, month, day, newStatus);
                        Toast.makeText(TrackPeriod.this, "Period Symptom Recorded", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    bleedingStatus.setOnCheckedChangeListener(null); // Remove listener temporarily
                    bleedingStatus.check(R.id.noBleedingRb);
                    bleedingStatus.setOnCheckedChangeListener((group, checkedId) -> {
                        int newStatus = 0;
                        if (checkedId == R.id.noBleedingRb) {
                            newStatus = 0;
                        } else if (checkedId == R.id.lightBleedingRb) {
                            newStatus = 1;
                        } else if (checkedId == R.id.mediumBleedingRb) {
                            newStatus = 2;
                        } else if (checkedId == R.id.heavyBleedingRb) {
                            newStatus = 3;
                        }
                        setStatus(year, month, day, newStatus);
                        Toast.makeText(TrackPeriod.this, "Period Symptom Recorded", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }
    }

    private int getBleedingStatusRadioButtonId(int status) {
        switch (status) {
            case 0:
                return R.id.noBleedingRb;
            case 1:
                return R.id.lightBleedingRb;
            case 2:
                return R.id.mediumBleedingRb;
            case 3:
                return R.id.heavyBleedingRb;
            default:
                return R.id.noBleedingRb;
        }
    }

    private void setCurrentDay() {
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.US);
        String formattedDate = sdf.format(currentDate.getTime());
        selectedDateText.setText(formattedDate);
    }
}
