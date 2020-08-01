package com.example.phm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SetImportance extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerBodyTemp, spinnerBloodPressure, spinnerBodyWeight,
    spinnerHeartRate, spinnerGlycemicIndex, spinnerSleepTime;
    Button btnSave;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_importance_layout);

        db = new DatabaseHelper(this);

        btnSave = findViewById(R.id.btnSave);
        spinnerBodyTemp = findViewById(R.id.spinnerBodyTemp);
        spinnerBloodPressure = findViewById(R.id.spinnerBloodPressure);
        spinnerBodyWeight = findViewById(R.id.spinnerBodyWeight);
        spinnerGlycemicIndex = findViewById(R.id.spinnerGlycemicIndex);
        spinnerHeartRate = findViewById(R.id.spinnerHeartRate);
        spinnerSleepTime = findViewById(R.id.spinnerSleepTime);


        String qBodyTemp = "SELECT importance_level FROM importance_table WHERE info_name = 'body_temperature_table'";
        String qBloodPressure = "SELECT importance_level FROM importance_table WHERE info_name = 'blood_pressure_table'";
        String qBodyWeight = "SELECT importance_level FROM importance_table WHERE info_name = 'body_weight_table'";
        String qSleepTime = "SELECT importance_level FROM importance_table WHERE info_name = 'sleep_time_table'";
        String qHeartRate = "SELECT importance_level FROM importance_table WHERE info_name = 'heart_rate_table'";
        String qGlycemicIndex = "SELECT importance_level FROM importance_table WHERE info_name = 'glycemic_index_table'";

        ArrayList<String> arrayBodyTemp, arrayBloodPressure, arrayBodyWeight,
                arrayHeartRate, arraySleepTime, arrayGlycemicIndex;

        //for the spinner to work
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.importance, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //get importance level for every table
        arrayBodyTemp = db.getImportanceLevel(qBodyTemp);
        int bodyTempImp = Integer.parseInt(arrayBodyTemp.get(0));
        spinnerBodyTemp.setAdapter(adapter);

        //STRANGE: if default value is 5, it appears something else on the
        //spinner initial value, that's why if importance is > 4 insert number 5
        if(bodyTempImp > 4)
            spinnerBodyTemp.setSelection(4);
        else
            spinnerBodyTemp.setSelection(bodyTempImp-1);
        spinnerBodyTemp.setOnItemSelectedListener(this);

        arrayBloodPressure = db.getImportanceLevel(qBloodPressure);
        int bloodPressureImp = Integer.parseInt(arrayBloodPressure.get(0));
        spinnerBloodPressure.setAdapter(adapter);
        if(bloodPressureImp > 4)
            spinnerBloodPressure.setSelection(4);
        else
            spinnerBloodPressure.setSelection(bloodPressureImp-1);
        spinnerBloodPressure.setOnItemSelectedListener(this);

        arrayBodyWeight = db.getImportanceLevel(qBodyWeight);
        int bodyWeightImp = Integer.parseInt(arrayBodyWeight.get(0));
        spinnerBodyWeight.setAdapter(adapter);
        if(bodyWeightImp > 4)
            spinnerBodyWeight.setSelection(4);
        else
            spinnerBodyWeight.setSelection(bodyWeightImp-1);
        spinnerBodyWeight.setOnItemSelectedListener(this);

        arrayGlycemicIndex = db.getImportanceLevel(qGlycemicIndex);
        int glycemicIndexImp = Integer.parseInt(arrayGlycemicIndex.get(0));
        spinnerGlycemicIndex.setAdapter(adapter);
        if(glycemicIndexImp > 4)
            spinnerGlycemicIndex.setSelection(4);
        else
            spinnerGlycemicIndex.setSelection(glycemicIndexImp);
        spinnerGlycemicIndex.setOnItemSelectedListener(this);

        arrayHeartRate = db.getImportanceLevel(qHeartRate);
        int heartRateImp = Integer.parseInt(arrayHeartRate.get(0));
        spinnerHeartRate.setAdapter(adapter);
        if(heartRateImp > 4)
            spinnerHeartRate.setSelection(4);
        else
            spinnerHeartRate.setSelection(heartRateImp);
        spinnerHeartRate.setOnItemSelectedListener(this);

        arraySleepTime = db.getImportanceLevel(qSleepTime);
        int sleepTimeImp = Integer.parseInt(arraySleepTime.get(0));
        spinnerSleepTime.setAdapter(adapter);
        if(sleepTimeImp > 4)
            spinnerSleepTime.setSelection(4);
        else
            spinnerHeartRate.setSelection(sleepTimeImp);
            spinnerSleepTime.setOnItemSelectedListener(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on save button click, update the importance level of each table
                //if it has been changed
        String impBodyTemp = spinnerBodyTemp.getSelectedItem().toString();
        String infoBodyTemp = "body_temperature_table";

        switch (impBodyTemp) {
            case "1":
                db.updateImportance(infoBodyTemp, "1");
                break;
            case "2":
                db.updateImportance(infoBodyTemp, "2");
                break;
            case "3":
                db.updateImportance(infoBodyTemp, "3");
                break;
            case "4":
                db.updateImportance(infoBodyTemp, "4");
                break;
            case "5":
                db.updateImportance(infoBodyTemp, "5");
                break;
        }

        String impBloodPressure = spinnerBloodPressure.getSelectedItem().toString();
        String infoBloodPressure = "blood_pressure_table";

        switch (impBloodPressure) {
            case "1":
                 db.updateImportance(infoBloodPressure, "1");
                 break;
            case "2":
                 db.updateImportance(infoBloodPressure, "2");
                 break;
            case "3":
                 db.updateImportance(infoBloodPressure, "3");
                 break;
            case "4":
                 db.updateImportance(infoBloodPressure, "4");
                 break;
            case "5":
                 db.updateImportance(infoBloodPressure, "5");
                 break;
              }
        String impSleepTime = spinnerSleepTime.getSelectedItem().toString();
        String infoSleepTime = "sleep_time_table";

        switch (impSleepTime) {
            case "1":
                 db.updateImportance(infoSleepTime, "1");
                 break;
            case "2":
                 db.updateImportance(infoSleepTime, "2");
                 break;
            case "3":
                 db.updateImportance(infoSleepTime, "3");
                 break;
            case "4":
                 db.updateImportance(infoSleepTime, "4");
                 break;
            case "5":
                 db.updateImportance(infoSleepTime, "5");
                 break;
              }

        String impGlycemicIndex = spinnerGlycemicIndex.getSelectedItem().toString();
        String infoGlycemicIndex = "glycemic_index_table";

        switch (impGlycemicIndex) {
            case "1":
                db.updateImportance(infoGlycemicIndex, "1");
                break;
            case "2":
                db.updateImportance(infoGlycemicIndex, "2");
                break;
            case "3":
                db.updateImportance(infoGlycemicIndex, "3");
                break;
            case "4":
                db.updateImportance(infoGlycemicIndex, "4");
                break;
            case "5":
                db.updateImportance(infoGlycemicIndex, "5");
                break;
        }

        String impBodyWeight = spinnerBodyWeight.getSelectedItem().toString();
        String infoBodyWeight = "body_weight_table";

        switch (impBodyWeight){
            case "1":
                db.updateImportance(infoBodyWeight, "1");
                break;
            case "2":
                db.updateImportance(infoBodyWeight, "2");
                break;
            case "3":
                db.updateImportance(infoBodyWeight, "3");
                break;
            case "4":
                db.updateImportance(infoBodyWeight, "4");
                break;
            case "5":
                db.updateImportance(infoBodyWeight, "5");
                break;
        }

        String impHeartRate = spinnerHeartRate.getSelectedItem().toString();
        String infoHeartRate = "heart_rate_table";

        switch (impHeartRate) {
            case "1":
                db.updateImportance(infoHeartRate, "1");
                break;
            case "2":
                db.updateImportance(infoHeartRate, "2");
                break;
            case "3":
                db.updateImportance(infoHeartRate, "3");
                break;
            case "4":
                db.updateImportance(infoHeartRate, "4");
                break;
            case "5":
                db.updateImportance(infoHeartRate, "5");
                break;
        }

                Toast.makeText(SetImportance.this, "Settings Saved", Toast.LENGTH_SHORT).show();
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
