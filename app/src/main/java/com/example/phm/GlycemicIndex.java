package com.example.phm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
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

public class GlycemicIndex extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{

    DatabaseHelper myDb;
    private SeekBar sbGlycemicLevel;
    private TextView tvGlycemicLevel;
    private TextView tvDateChosen;
    private ImageButton btnChooseDate;
    private Switch mySwitch;
    private Button btnConfirm;
    private EditText etOptionalNote;
    private TextView tvChosenLevel;
    private TextView lastUpdate;
    boolean futureDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glycemic_index);

        sbGlycemicLevel = findViewById(R.id.seekBarGlycemicLevel);
        tvGlycemicLevel = findViewById(R.id.tvGlycemicLevel);
        btnChooseDate = findViewById(R.id.btnChooseDate);
        tvDateChosen = findViewById(R.id.tvDateChosen);
        btnConfirm = findViewById(R.id.btnConfirm);
        etOptionalNote = findViewById(R.id.etOptionalNote);
        tvChosenLevel = findViewById(R.id.tvChosenLevel);
        lastUpdate = findViewById(R.id.lastUpdate);

        myDb = new DatabaseHelper(this);

        String table_name = "glycemic_index_table";

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

        //open DatePickerDialog on click of this button, and set on OFF the switch for "Today"
        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
                mySwitch.setChecked(false);
            }
        });

        mySwitch = (Switch) findViewById(R.id.mySwitch);

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



        //set the 4 possible values of the seekbar
        sbGlycemicLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            String chosenLevel = null;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               switch (progress) {
                   case 0:
                       tvGlycemicLevel.setText(getResources().getString(R.string.very_low_glycemic_level));
                       tvChosenLevel.setText(getResources().getString(R.string.veryLowGlycemic));
                       break;
                   case 1:
                       tvGlycemicLevel.setText(getResources().getString(R.string.low_glycemic_level));
                       tvChosenLevel.setText(getResources().getString(R.string.lowGlycemic));
                       break;
                   case 2:
                       tvGlycemicLevel.setText(getResources().getString(R.string.normal_glycemic_level));
                       tvChosenLevel.setText(getResources().getString(R.string.normalGlycemic));
                       break;
                   case 3:
                       tvGlycemicLevel.setText(getResources().getString(R.string.high_glycemic_level));
                       tvChosenLevel.setText(getResources().getString(R.string.highGlycemic));
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
                Toast.makeText(GlycemicIndex.this, "You can't choose a date in the future!", Toast.LENGTH_SHORT).show();
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
                if(!tvGlycemicLevel.getText().toString().equals("") && !tvDateChosen.getText().toString().equals("")) {
                    boolean isInserted = myDb.insertGlycemicIndex(tvDateChosen.getText().toString(),
                        tvChosenLevel.getText().toString().substring(tvChosenLevel.length() - 2, tvChosenLevel.length()),
                        etOptionalNote.getText().toString());
                if(isInserted)
                    Toast.makeText(GlycemicIndex.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(GlycemicIndex.this, "Data not inserted", Toast.LENGTH_SHORT).show();
               }
                if(tvGlycemicLevel.getText().toString().equals("")) {
                    tvGlycemicLevel.setError("Please enter this field");
                }
                if(tvDateChosen.getText().toString().equals("")) {
                    Toast.makeText(GlycemicIndex.this, "Please choose a date", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
