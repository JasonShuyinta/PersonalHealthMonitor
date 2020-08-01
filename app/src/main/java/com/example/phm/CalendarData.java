package com.example.phm;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CalendarView calendarView;
    TextView tvchosenDate;
    Spinner spinnerInfo;
    Button btnNext;
    boolean futureDate = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        calendarView = findViewById(R.id.calendar_view);
        tvchosenDate = findViewById(R.id.tvChosenDate);
        spinnerInfo = findViewById(R.id.spinnerInfo);
        btnNext = findViewById(R.id.btnNext);

        //set the textview as todays date, as default
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        tvchosenDate.setText(today);

        //needed to make spinners work
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.info, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInfo.setAdapter(spinnerAdapter);
        spinnerInfo.setOnItemSelectedListener(this);

        //Remember: Months start at 0, adjust output of chosen date to a readable one.
        //you don't want a date to be for example : 2020-7-7 but 2020-07-07
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String realMonth;
                String realDay;
                int adjMonth = month+1;
                if(adjMonth < 10)
                    realMonth = "0" + adjMonth;
                else
                    realMonth = String.valueOf(adjMonth);

                if(dayOfMonth < 10)
                    realDay = "0" + dayOfMonth;
                else
                    realDay = String.valueOf(dayOfMonth);

                String date = year + "-" + realMonth + "-" + realDay;


                //this check is to verify the user doesn't choose a future date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String maxDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                Date today, chosenDate;

                try {
                    today = sdf.parse(maxDay);
                    chosenDate = sdf.parse(date);
                    if(chosenDate.after(today)) {
                        Toast.makeText(CalendarData.this, "You can't choose a date in the future!", Toast.LENGTH_SHORT).show();
                        futureDate = true;
                        tvchosenDate.setText("");
                    } else
                        futureDate = false;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //if the chosen date is not in the future, than pass to the textview the chosen date
                //from the calendar
                if(!futureDate)
                    tvchosenDate.setText(date);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTable = spinnerInfo.getSelectedItem().toString();
                //when the user clicks the button, get the selected spinner item
                String tableName = null;
                switch (selectedTable) {
                    case "Body Temperature":
                        tableName = "body_temp_table";
                        break;
                    case "Blood Pressure":
                        tableName = "blood_pressure_table";
                        break;
                    case "Glycemic Index":
                        tableName = "glycemic_index_table";
                        break;
                    case "Heart Rate":
                        tableName = "heart_rate_table";
                        break;
                    case "Body Weight":
                        tableName = "body_weight_table";
                        break;
                    case "Sleep Time":
                        tableName = "sleep_time_table";
                        break;
                }

                String finalDate = tvchosenDate.getText().toString();

                //Pass the date and the table name to the next Activity, which is ListDataOnDate
                Intent i = new Intent(CalendarData.this, ListDataOnDate.class);
                i.putExtra("tableName", tableName);
                i.putExtra("date", finalDate);
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}