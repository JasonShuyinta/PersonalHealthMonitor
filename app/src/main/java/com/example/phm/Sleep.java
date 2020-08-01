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

public class Sleep extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DatabaseHelper myDb;
    private TextView tvDateChosen;
    private Switch mySwitch;
    private EditText etOptionalNote;
    private Button btnConfirm;
    private ImageButton btnChooseDate;
    private EditText etSleep;
    private TextView lastUpdate;
    boolean futureDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep);

        tvDateChosen = findViewById(R.id.tvDateChosen);
        etOptionalNote = findViewById(R.id.etOptionalNote);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnChooseDate = findViewById(R.id.btnChooseDate);
        etSleep = findViewById(R.id.etSleep);
        mySwitch = findViewById(R.id.mySwitch);
        lastUpdate = findViewById(R.id.lastUpdate);

        myDb = new DatabaseHelper(this);

        String table_name = "sleep_time_table";

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String maxDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Date today, chosenDate;

        try {
            today = sdf.parse(maxDay);
            chosenDate = sdf.parse(date);
            if(chosenDate.after(today)) {
                Toast.makeText(Sleep.this, "You can't choose a date in the future!", Toast.LENGTH_SHORT).show();
                futureDate = true;
                tvDateChosen.setText("");
            } else
                futureDate = false;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!futureDate)
            tvDateChosen.setText(date);
    }

    public void addData() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etSleep.getText().toString().isEmpty() && !tvDateChosen.getText().toString().equals("")) {
                    boolean isInserted = myDb.insertSleepTime(tvDateChosen.getText().toString(),
                            etSleep.getText().toString(),
                            etOptionalNote.getText().toString());
                    if(Integer.parseInt(etSleep.getText().toString()) > 24) {
                        Toast.makeText(Sleep.this, "You cannot sleep more than 24 hours!", Toast.LENGTH_SHORT).show();

                    }
                    if (isInserted && Integer.parseInt(etSleep.getText().toString()) < 24)
                        Toast.makeText(Sleep.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                    else if(Integer.parseInt(etSleep.getText().toString()) > 24)
                        Toast.makeText(Sleep.this, "You cannot sleep more than 24 hours!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Sleep.this, "Could not insert data!", Toast.LENGTH_SHORT).show();
                } else {
                    if(etSleep.getText().toString().isEmpty()) {
                        etSleep.setError("Please insert a sleep time");
                    }
                    if(tvDateChosen.getText().toString().equals("")) {
                        Toast.makeText(Sleep.this, "Please choose a date",  Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
