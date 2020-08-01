package com.example.phm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class BodyTemp extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    DatabaseHelper myDb;
    private TextView tvDateChosen;
    private Switch mySwitch;
    private EditText etTemp;
    private EditText etOptionalNote;
    private Button btnConfirm;
    private ImageButton btnChooseDate;
    private Spinner spinner;
    private TextView lastUpdate;
    boolean futureDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_temp);

        tvDateChosen = findViewById(R.id.tvDateChosen);
        etTemp = findViewById(R.id.etTemp);
        etOptionalNote = findViewById(R.id.etOptionalNote);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnChooseDate = findViewById(R.id.btnChooseDate);
        spinner = findViewById(R.id.tempScale);
        mySwitch = findViewById(R.id.mySwitch);
        lastUpdate = findViewById(R.id.lastUpdate);

        myDb = new DatabaseHelper(this);

        String table_name = "body_temp_table";

        //section to set the text of the last update. If there are no records, print "No records inserted"
        //this query retrieves the most recent date inserted
        String query1 = "SELECT date FROM " + table_name + " ORDER BY date DESC LIMIT 1";
        ArrayList<String> lastDate;
        long numRows = myDb.getNumRows(table_name);
        lastDate = myDb.getDates(query1);
        if (numRows == 0) {
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

        //this ArrayAdapter is needed to make the spinner work. the spinner is needed in case
        //the user wants to switch from Celsius to Fahrenheit
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tempScale, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //if "Today" switch is pressed, get todays date and put it in the textview
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                if (isChecked) {
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
        int realMonth = month + 1;
        String date;
        if (month < 10 && dayOfMonth > 9) {
            date = year + "-0" + realMonth + "-" + dayOfMonth;
        } else if (month > 9 && dayOfMonth < 10) {
            date = year + "-" + realMonth + "-0" + dayOfMonth;
        } else if (month < 10 && dayOfMonth < 10) {
            date = year + "-0" + realMonth + "-0" + dayOfMonth;
        } else {
            date = year + "-" + realMonth + "-" + dayOfMonth;
        }

        //this check is to verify the user doesn't choose a future date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String maxDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Date today, chosenDate;

        try {
            today = sdf.parse(maxDay);
            chosenDate = sdf.parse(date);
            if(chosenDate.after(today)) {
                Toast.makeText(BodyTemp.this, "You can't choose a date in the future!", Toast.LENGTH_SHORT).show();
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
                if (!etTemp.getText().toString().isEmpty() && !tvDateChosen.getText().toString().equals("") && !futureDate) {
                    String scale = spinner.getSelectedItem().toString();
                    //Whatever the user chooses, the values are passed to the database as Celsius
                    String temperature = null;
                    switch (scale){
                        case "C°":
                            temperature = etTemp.getText().toString();
                            break;
                            //transform fahrenheit to celsius
                        case "F°":
                            temperature = String.valueOf((Integer.parseInt(etTemp.getText().toString()) - 32) * 5/9);
                            break;
                    }
                    boolean isInserted = myDb.insertBodyTemp(tvDateChosen.getText().toString(),
                            temperature,
                            spinner.getSelectedItem().toString(),
                            etOptionalNote.getText().toString());
                    if (isInserted)
                        Toast.makeText(BodyTemp.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(BodyTemp.this, "Data NOT Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    if (etTemp.getText().toString().isEmpty()) {
                        etTemp.setError("Please insert a Temperature");
                    }
                    if (tvDateChosen.getText().toString().equals("")) {
                        Toast.makeText(BodyTemp.this, "Please Choose a Date", Toast.LENGTH_SHORT).show();
                    }
                }
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
