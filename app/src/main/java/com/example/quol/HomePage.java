package com.example.quol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomePage extends AppCompatActivity {

    private TextView fromDateTextView;
    private TextView toDateTextView;
    private TextView mlTextView;
    private ArrayList<Long> selectedDates = new ArrayList<>();
    private long selectedFromDate = -1; // Initialize to a default value that indicates no date selected yet
    private long selectedToDate = -1;   // Initialize to a default value that indicates no date selected yet


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        CalendarView appCalendar = findViewById(R.id.appCalendar);

        // Set the maximum selectable date to today
        long maxDate = Calendar.getInstance().getTimeInMillis();
        appCalendar.setMaxDate(maxDate);

        mlTextView = findViewById(R.id.mlText);
        fromDateTextView = findViewById(R.id.fromDate);
        toDateTextView = findViewById(R.id.toDate);
        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(view -> saveSelectedDates(appCalendar));

        appCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                long selectedDate = getDateInMillis(year, month, dayOfMonth);
                // Check if the selected date is already present in selectedDates
                if (selectedDates.contains(selectedDate)) {
                    // If yes, remove it from the list and update the UI
                    selectedDates.remove(selectedDate);
                    highlightSelectedDates(appCalendar);
                    return;
                }

                if (selectedFromDate == -1) {
                    selectedFromDate = selectedDate; // Store the selected fromDate
                    fromDateTextView.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                } else if (selectedToDate == -1) {
                    // Check if selected toDate is earlier than selected fromDate
                    if (selectedDate < selectedFromDate) {
                        Toast.makeText(HomePage.this, "To date cannot be earlier than From date", Toast.LENGTH_SHORT).show();
                        // Clear all inputs and start listening for the fromDate again
                        selectedFromDate = -1;
                        selectedToDate = -1;
                        fromDateTextView.setText("");
                        toDateTextView.setText("");
                        return; // Exit the method
                    }
                    selectedToDate = selectedDate; // Store the selected toDate
                    toDateTextView.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                } else {
                    // Check if fromDate is later than toDate
                    if (selectedFromDate > selectedToDate) {
                        // Clear both fromDate and toDate
                        selectedFromDate = -1;
                        selectedToDate = -1;
                        fromDateTextView.setText("");
                        toDateTextView.setText("");
                        selectedDates.clear(); // Clear selected dates
                        return; // Exit the method without further updating the text views or selected dates
                    }

                    // Check for overlapping dates
                    boolean overlap = false;
                    for (long date : selectedDates) {
                        if ((date >= selectedFromDate && date <= selectedToDate) || (selectedFromDate >= date && selectedFromDate <= date)) {
                            overlap = true;
                            break;
                        }
                    }

                    if (overlap) {
                        // Handle overlap
                        Toast.makeText(HomePage.this, "Selected dates overlap with existing range", Toast.LENGTH_SHORT).show();
                        selectedFromDate = -1;
                        selectedToDate = -1;
                        fromDateTextView.setText("");
                        toDateTextView.setText("");
                        selectedDates.clear(); // Clear selected dates
                    } else {
                        // No overlap, proceed as usual
                        selectedDates.clear();
                        selectedDates.addAll(getDatesBetween(selectedFromDate, selectedToDate));
                        highlightSelectedDates(appCalendar);
                        selectedFromDate = -1;
                        selectedToDate = -1;
                        fromDateTextView.setText("");
                        toDateTextView.setText("");
                    }
                }
            }
        });
    }

    private long getDateInMillis(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTimeInMillis();
    }

    private ArrayList<Long> getDatesBetween(long startDate, long endDate) {
        ArrayList<Long> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);
        while (calendar.getTimeInMillis() <= endDate) {
            dates.add(calendar.getTimeInMillis());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }

    private void highlightSelectedDates(CalendarView calendarView) {
        long minDate = calendarView.getDate();
        long maxDate = calendarView.getDate();
        for (long date : selectedDates) {
            if (date < minDate) minDate = date;
            if (date > maxDate) maxDate = date;
        }
        calendarView.setDate(minDate, true, false);
        calendarView.setDate(maxDate, true, false);
        calendarView.invalidate();
    }


    private long getDateInMillisFromDateText(String dateText) {
        String[] parts = dateText.split("/");
        int month = Integer.parseInt(parts[0]) - 1; // Subtract 1 because Calendar months are zero-based
        int dayOfMonth = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        return getDateInMillis(year, month, dayOfMonth);
    }

    private void displaySelectedDates() {
        if (selectedDates.isEmpty()) {
            mlTextView.setText("");
            return;
        }

        // Get the start and end dates
        long startDate = selectedDates.get(0);
        long endDate = selectedDates.get(selectedDates.size() - 1);

        // Format the dates
        String formattedStartDate = getFormattedDate(startDate);
        String formattedEndDate = getFormattedDate(endDate);

        // If mlTextView already has text, append a newline before adding the new dates
        String currentText = mlTextView.getText().toString();
        if (!currentText.isEmpty()) {
            currentText += "\n";
        }

        // Append the formatted start and end dates
        String datesText = formattedStartDate + " - " + formattedEndDate;
        mlTextView.setText(currentText + datesText);
    }



    private void appendDateToTextView(long dateInMillis) {
        String formattedDate = getFormattedDate(dateInMillis);
        String currentText = mlTextView.getText().toString();
        if (!currentText.isEmpty()) {
            currentText += "\n"; // Add newline if text is not empty
        }
        currentText += formattedDate;
        mlTextView.setText(currentText);
    }

    private void saveSelectedDates(CalendarView calendarView) {
        String fromDateText = fromDateTextView.getText().toString();
        String toDateText = toDateTextView.getText().toString();

        if (!fromDateText.isEmpty() && !toDateText.isEmpty()) {
            long fromDate = getDateInMillisFromDateText(fromDateText);
            long toDate = getDateInMillisFromDateText(toDateText);

            if (fromDate <= toDate) {
                boolean hasOverlap = false;
                for (long date : selectedDates) {
                    if (fromDate <= date && toDate >= date) {
                        hasOverlap = true;
                        break;
                    }
                }
                if (!hasOverlap) {
                    selectedDates.add(fromDate);
                    selectedDates.add(toDate);
                    displaySelectedDates(); // Update displayed dates
                } else {
                    Toast.makeText(this, "Selected dates overlap with existing selection", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "To date cannot be earlier than from date", Toast.LENGTH_SHORT).show();
            }
        }

        // Clear all inputs and reset selectedFromDate
        selectedFromDate = -1;
        selectedToDate = -1;
        fromDateTextView.setText("");
        toDateTextView.setText("");
    }


    @Override
    protected void onResume() {
        super.onResume();
        displaySelectedDates(); // Update displayed dates when activity resumes
    }

    private String getFormattedDate(long dateInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
