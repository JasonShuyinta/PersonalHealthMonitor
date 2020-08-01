package com.example.phm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReportImportance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    TextView tvChosenDate, tvImportance;
    boolean futureDate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_importance);

        Button btnDate = findViewById(R.id.btnDate);
        SeekBar seekBarImportance = findViewById(R.id.seekBarImportance);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        tvChosenDate = findViewById(R.id.tvChosenDate);
        tvImportance = findViewById(R.id.tvImportance);

        //set today's date as textview
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvChosenDate.setText(today);

        //open DatePickerdialog on button click
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //Set the 5 level of importance through seekbar
        seekBarImportance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                        tvImportance.setText("1");
                        break;
                    case 1:
                        tvImportance.setText("2");
                        break;
                    case 2:
                        tvImportance.setText("3");
                        break;
                    case 3:
                        tvImportance.setText("4");
                        break;
                    case 4:
                        tvImportance.setText("5");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //pass values to ListReport class through intent on button click
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListReport.class);
                i.putExtra("date", tvChosenDate.getText().toString());
                i.putExtra("importance", tvImportance.getText().toString());
                startActivity(i);
            }
        });
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }


    ///always format the correct way the chosend date
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int realMonth = month+1;
        String date;
        if(month < 10 && dayOfMonth > 9) {
            date = year + "-0"+realMonth+"-"+dayOfMonth;
        }
        else if(month > 9 && dayOfMonth < 10) {
            date = year +"-"+realMonth+"-0"+dayOfMonth;
        }
        else if(month < 10 && dayOfMonth < 10) {
            date = year + "-0"+realMonth+"-0"+dayOfMonth;
        } else {
            date = year+"-"+realMonth+"-"+dayOfMonth;
        }
        //Check its not in the future
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String maxDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Date today, chosenDate;

        try {
            today = sdf.parse(maxDay);
            chosenDate = sdf.parse(date);
            if(chosenDate.after(today)) {
                Toast.makeText(ReportImportance.this, "You can't choose a date in the future!", Toast.LENGTH_SHORT).show();
                futureDate = true;
                tvChosenDate.setText("");
            } else
                futureDate = false;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!futureDate)
            tvChosenDate.setText(date);
    }
}
