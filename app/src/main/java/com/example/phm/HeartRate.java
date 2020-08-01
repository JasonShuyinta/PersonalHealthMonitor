package com.example.phm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HeartRate extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    DatabaseHelper myDb;
    private TextView tvDateChosen;
    private Button btnConfirm;
    private ImageButton btnChooseDate;
    private EditText etOptionalNote;
    private Switch mySwitch;
    private EditText etHeartRate;
    private TextView lastUpdate;
    boolean futureDate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heart_rate);

        mySwitch = findViewById(R.id.mySwitch);
        tvDateChosen = findViewById(R.id.tvDateChosen);
        btnChooseDate = findViewById(R.id.btnChooseDate);
        btnConfirm = findViewById(R.id.btnConfirm);
        etOptionalNote = findViewById(R.id.etOptionalNote);
        etHeartRate = findViewById(R.id.etHeartRate);
        lastUpdate = findViewById(R.id.lastUpdate);


        myDb = new DatabaseHelper(this);

        String table_name = "heart_rate_table";

        //section to set the text of the last update. If there are no records, print "No records inserted"
        //this query retrieves the most recent date inserted
        String query1 = "SELECT date FROM "+ table_name +" ORDER BY date DESC LIMIT 1";
        ArrayList<String> lastDate;
        long numRows = myDb.getNumRows(table_name);
        lastDate = myDb.getDates(query1);
        if(numRows == 0) {
            lastUpdate.setText("No records inserted");
        } else {
            lastUpdate.setText("Last Update: " + lastDate.get(0));
        }


        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
                mySwitch.setChecked(false);
            }
        });

        //if "Today" switch is pressed, get todays date and put it in the textview
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                if(isChecked) {
                    tvDateChosen.setText(today);
                }
            }
        });

        addData();

    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    //Remember: Months start at 0, adjust output of chosen date to a readable one.
    //you don't want a date to be for example : 2020-7-7 but 2020-07-07
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int realMonth = month+1;
        //String date = year +"-"+realMonth+"-"+dayOfMonth;
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

        //this check is to verify the user doesn't choose a future date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String maxDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Date today, chosenDate;

        try {
            today = sdf.parse(maxDay);
            chosenDate = sdf.parse(date);
            if(chosenDate.after(today)) {
                Toast.makeText(HeartRate.this, "You can't choose a date in the future!", Toast.LENGTH_SHORT).show();
                futureDate = true;
                tvDateChosen.setText("");
            } else
                futureDate = false;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //if the user didn't choose a future date, than you can pass the selected date
        //to the textview
        if(!futureDate)
            tvDateChosen.setText(date);
    }

    //this method is to add data to the database and save it.
    //we must first make sure all the form is filled with necessary information, thus the information
    //must not be empty and neither must be the chosen date.
    public void addData() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etHeartRate.getText().toString().isEmpty() && !tvDateChosen.getText().toString().equals("")) {
                    boolean isInserted = myDb.insertHeartRate(tvDateChosen.getText().toString(),
                            etHeartRate.getText().toString(),
                            etOptionalNote.getText().toString());
                    if(isInserted)
                        Toast.makeText(HeartRate.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(HeartRate.this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
                }
                if(etHeartRate.getText().toString().equals("")) {
                    etHeartRate.setError("Please enter this field");
                }
                if(tvDateChosen.getText().toString().equals("")) {
                    Toast.makeText(HeartRate.this, "Please choose a date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
