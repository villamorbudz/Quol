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
    int CurrMonth;
    int CurrYear;
    int CurrDay;
    int DayOffset;

    // Year -> Month -> Day -> Status
    HashMap<Integer,HashMap<Integer,HashMap<Integer,Integer>>> YearHash;
    HashMap<Integer,Integer>  FinalDayHash;

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
        YearHash = new HashMap<>();
        CurrYear = cd.get(Calendar.YEAR);
        CurrMonth = cd.get(Calendar.MONTH);
        CurrDay = cd.get(Calendar.DATE);
        moveMonth(CurrYear,CurrMonth);

        Button nextMonth = findViewById(R.id.NextMonthBtn);
        Button prevMonth = findViewById(R.id.PrevMonthBtn);
        nextMonth.setOnClickListener(v -> setMonth(+1));
        prevMonth.setOnClickListener(v -> setMonth(-1));

        findViewById(R.id.trackBtn).setOnClickListener(view -> {
            Intent openActivity = new Intent(PeriodHistory.this, TrackPeriod.class);
            finish();
            startActivity(openActivity);
        });
    }

    private void moveMonth(int Year, int Month) {
        TextView MYText = findViewById(R.id.MYtext);
        String[] MonthsString = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        MYText.setText(MonthsString[Month] + " " + Year);
        FinalDayHash = getStatus(Year, Month);

        Calendar cal = new GregorianCalendar(Year, Month, 1);
        int startdayinWeek = cal.get(Calendar.DAY_OF_WEEK);
        DayOffset = startdayinWeek;
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (Button btn : baseButtons) {
            btn.setText("");
            btn.setVisibility(View.INVISIBLE);
            btn.setEnabled(false);
        }

        dayButtons = new ArrayList<>();

        for (int x = 0; x < daysInMonth; x++) {
            dayButtons.add(baseButtons.get(startdayinWeek + x - 1));
            System.out.println(x);
        }

        for (int x = 0; x < daysInMonth; x++) {
            Button btn = dayButtons.get(x);
            btn.setText(Integer.toString(x + 1));

            btn.setVisibility(View.VISIBLE);

            int status = FinalDayHash.getOrDefault(x + 1, 0);
            switch (status) {
                case 0: // None
                    btn.setBackgroundColor(Color.TRANSPARENT); // No background color
                    break;
                case 1: // Light
                    btn.setBackgroundColor(Color.parseColor("#80FF0000")); // Light-opacity red
                    break;
                case 2: // Medium
                    btn.setBackgroundColor(Color.parseColor("#C0FF0000")); // Medium-opacity red
                    break;
                case 3: // Heavy
                    btn.setBackgroundColor(Color.RED); // Red
                    break;
                default:
                    break;
            }
        }
    }

    private void setMonth(int Month){
        CurrMonth = Month + CurrMonth;
        if(CurrMonth == 0 && Month == -1){          //lazy fix
           CurrMonth = 11;
           CurrYear -= 1;
        }
        if(CurrMonth / 12 > 0){
            CurrYear += Month;
            CurrMonth %= 2;
        }
        moveMonth(CurrYear,CurrMonth);
    }
    private HashMap<Integer, Integer> getStatus(int year, int month) {
        HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> YearHash = PeriodStatusManager.getYearHash();
        if (!YearHash.containsKey(year)) {
            HashMap<Integer, HashMap<Integer, Integer>> Monthhash = new HashMap<>();
            YearHash.put(year, Monthhash);
        }

        HashMap<Integer, HashMap<Integer, Integer>> MonthHash = YearHash.get(year);
        if (!MonthHash.containsKey(month)) {
            HashMap<Integer, Integer> DayHash = new HashMap<>();
            MonthHash.put(month, DayHash);
        }
        return MonthHash.get(month);
    }
}