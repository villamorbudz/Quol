package com.example.quol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class HomePage extends AppCompatActivity {

    private TextView fromDateTextView;
    private TextView toDateTextView;
    private TextView mlTextView;
    CalendarView appCalendar;
    // Starts at day 1 of week 1 based on layout
    ArrayList<Button> baseButtons;
    // Starts at day 1 of month
    ArrayList<Button> dayButtons;
    int CurrMonth;
    int CurrYear;
    int CurrDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Set the maximum selectable date to today
        Button saveBtn = findViewById(R.id.saveBtn);

        baseButtons = new ArrayList<>();
        dayButtons = new ArrayList<>();


        baseButtons.add((Button) findViewById(R.id.D1));
        baseButtons.add((Button) findViewById(R.id.D2));
        baseButtons.add((Button) findViewById(R.id.D3));
        baseButtons.add((Button) findViewById(R.id.D4));
        baseButtons.add((Button) findViewById(R.id.D5));
        baseButtons.add((Button) findViewById(R.id.D6));
        baseButtons.add((Button) findViewById(R.id.D7));
        baseButtons.add((Button) findViewById(R.id.D8));
        baseButtons.add((Button) findViewById(R.id.D9));
        baseButtons.add((Button) findViewById(R.id.D10));
        baseButtons.add((Button) findViewById(R.id.D11));
        baseButtons.add((Button) findViewById(R.id.D12));
        baseButtons.add((Button) findViewById(R.id.D13));
        baseButtons.add((Button) findViewById(R.id.D14));
        baseButtons.add((Button) findViewById(R.id.D15));
        baseButtons.add((Button) findViewById(R.id.D16));
        baseButtons.add((Button) findViewById(R.id.D17));
        baseButtons.add((Button) findViewById(R.id.D18));
        baseButtons.add((Button) findViewById(R.id.D19));
        baseButtons.add((Button) findViewById(R.id.D20));
        baseButtons.add((Button) findViewById(R.id.D21));
        baseButtons.add((Button) findViewById(R.id.D22));
        baseButtons.add((Button) findViewById(R.id.D23));
        baseButtons.add((Button) findViewById(R.id.D24));
        baseButtons.add((Button) findViewById(R.id.D25));
        baseButtons.add((Button) findViewById(R.id.D26));
        baseButtons.add((Button) findViewById(R.id.D27));
        baseButtons.add((Button) findViewById(R.id.D28));
        baseButtons.add((Button) findViewById(R.id.D29));
        baseButtons.add((Button) findViewById(R.id.D30));
        baseButtons.add((Button) findViewById(R.id.D31));
        baseButtons.add((Button) findViewById(R.id.D32));
        baseButtons.add((Button) findViewById(R.id.D33));
        baseButtons.add((Button) findViewById(R.id.D34));
        baseButtons.add((Button) findViewById(R.id.D35));
        baseButtons.add((Button) findViewById(R.id.D36));
        baseButtons.add((Button) findViewById(R.id.D37));
        baseButtons.add((Button) findViewById(R.id.D38));
        baseButtons.add((Button) findViewById(R.id.D39));
        baseButtons.add((Button) findViewById(R.id.D40));
        baseButtons.add((Button) findViewById(R.id.D41));
        baseButtons.add((Button) findViewById(R.id.D42));

        Calendar cd = Calendar.getInstance();
        CurrYear = cd.get(Calendar.YEAR);
        CurrMonth = cd.get(Calendar.MONTH);
        CurrDay = cd.get(Calendar.DATE);
        moveMonth(CurrYear,CurrMonth);

        Button nextMonth = (Button)findViewById(R.id.NextMonthBtn);
        Button prevMonth = (Button)findViewById(R.id.PrevMonthBtn);

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonth(+1);
            }
        });

        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonth(-1);
            }
        });

    }

    private void moveMonth(int Year,int Month){
        TextView MYText = findViewById(R.id.MYtext);
        String[] MonthsString = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        MYText.setText(MonthsString[Month] + ", " + Year );

        Calendar cal = new GregorianCalendar(Year,Month,1);
        int startdayinWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(Button btn: baseButtons){
            btn.setText("");
        }
        dayButtons = new ArrayList<>();
        for(int x=0;x<daysInMonth;x++){
            dayButtons.add(baseButtons.get(startdayinWeek+x-1));
            System.out.println(x);
        }
        for(int x=0;x<daysInMonth;x++){
            Button btn = dayButtons.get(x);
            btn.setText(Integer.toString(x+1));
        }
        System.out.println("Done");
    }

    private void setMonth(int Month){
        CurrMonth = Month + CurrMonth;
        if(CurrMonth == 0 && Month == -1){ //lazy fix
           CurrMonth = 11;
           CurrYear-=1;
        }
        if(CurrMonth/12 > 0){
            CurrYear+= Month;
            CurrMonth%=2;
        }
        moveMonth(CurrYear,CurrMonth);
    }

}
