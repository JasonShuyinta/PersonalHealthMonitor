package com.example.phm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

public class Threshold extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseHelper db;
    final String CHANNEL_ID = "777";
    int notifBodyTemp = 1;
    int notifBloodPressure  =2 ;
    int notifBodyWeight = 3;
    int notifSleepTime = 4;
    int notifHeartRate = 5;
    int notifGlycemicIndex = 6;


    EditText etBodyTemp, etBloodPressure, etSleepTime, etBodyWeight, etHeartRate, etGlycemicIndex;
    Switch switchBodyTemp, switchBloodPressure, switchSleepTime, switchBodyWeight, switchHeartRate, switchGlycemicIndex;
    boolean bodyTemp, bloodPressure, bodyWeight, glycemicIndex, heartRate, sleepTime = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threshold);

        db = new DatabaseHelper(this);

        final Spinner spinnerTimePeriod = findViewById(R.id.spinnerTimePeriod);
        Button btnSave = findViewById(R.id.btnSave);
        //Set initial values for the different tables
        etBodyTemp = findViewById(R.id.etBodyTempThresh);
        etBodyTemp.setText("36");
        etBloodPressure = findViewById(R.id.etBloodPressureThresh);
        etBloodPressure.setText("80");
        etBodyWeight = findViewById(R.id.etBodyWeightThresh);
        etBodyWeight.setText("70");
        etHeartRate = findViewById(R.id.etHeartRateThresh);
        etHeartRate.setText("80");
        etSleepTime = findViewById(R.id.etSleepTimeThresh);
        etSleepTime.setText("8");
        etGlycemicIndex = findViewById(R.id.etGlycemicIndexThresh);
        etGlycemicIndex.setText("120");
        switchBodyTemp = findViewById(R.id.switchBodyTemp);
        switchBloodPressure = findViewById(R.id.switchBloodPressure);
        switchSleepTime = findViewById(R.id.switchSleepTime);
        switchBodyWeight = findViewById(R.id.switchBodyWeight);
        switchHeartRate = findViewById(R.id.switchHeartRate);
        switchGlycemicIndex = findViewById(R.id.switchGlycemicIndex);


        String qBodyTemp = "SELECT importance_level FROM importance_table WHERE info_name = 'body_temperature_table'";
        String qBloodPressure = "SELECT importance_level FROM importance_table WHERE info_name = 'blood_pressure_table'";
        String qBodyWeight = "SELECT importance_level FROM importance_table WHERE info_name = 'body_weight_table'";
        String qSleepTime = "SELECT importance_level FROM importance_table WHERE info_name = 'sleep_time_table'";
        String qHeartRate = "SELECT importance_level FROM importance_table WHERE info_name = 'heart_rate_table'";
        String qGlycemicIndex = "SELECT importance_level FROM importance_table WHERE info_name = 'glycemic_index_table'";

        ArrayList<String> arrayBodyTemp, arrayBloodPressure, arrayBodyWeight,
                arrayHeartRate, arraySleepTime, arrayGlycemicIndex;


        //creates notification channel
        createNotificationChannel();

        //for the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_period, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimePeriod.setAdapter(adapter);
        spinnerTimePeriod.setOnItemSelectedListener(this);


        //get importance level for each info
        arrayBodyTemp = db.getImportanceLevel(qBodyTemp);
        arrayBloodPressure = db.getImportanceLevel(qBloodPressure);
        arrayBodyWeight = db.getImportanceLevel(qBodyWeight);
        arrayHeartRate = db.getImportanceLevel(qHeartRate);
        arraySleepTime = db.getImportanceLevel(qSleepTime);
        arrayGlycemicIndex = db.getImportanceLevel(qGlycemicIndex);


        //set the switch automatically to ON if importance level is greater than 3
        if(Integer.parseInt(arrayBodyTemp.get(0)) >= 3)
            switchBodyTemp.setChecked(true);
        if(Integer.parseInt(arrayBloodPressure.get(0)) >= 3)
            switchBloodPressure.setChecked(true);
        if(Integer.parseInt(arrayBodyWeight.get(0)) >= 3)
            switchBodyWeight.setChecked(true);
        if(Integer.parseInt(arrayHeartRate.get(0)) >= 3)
            switchHeartRate.setChecked(true);
        if(Integer.parseInt(arraySleepTime.get(0)) >= 3)
            switchSleepTime.setChecked(true);
        if(Integer.parseInt(arrayGlycemicIndex.get(0)) >= 3)
            switchGlycemicIndex.setChecked(true);

        //if spinners are checked, it means the user wants to receive notifications
        //of the info, set boolean to true
        switchBodyTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    bodyTemp = true;
                else
                    bodyTemp = false;
            }
        });

        switchBloodPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    bloodPressure = true;
                else
                    bloodPressure = false;
            }
        });

        switchHeartRate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    heartRate = true;
                else
                    heartRate = false;
            }
        });

        switchGlycemicIndex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    glycemicIndex = true;
                else
                    glycemicIndex = false;
            }
        });

        switchBodyWeight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    bodyWeight = true;
                else
                    bodyWeight = false;
            }
        });

        switchSleepTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    sleepTime = true;
                else
                    sleepTime = false;
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            String startDate = null;

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //get period of time the user wants
                String timePeriod = spinnerTimePeriod.getSelectedItem().toString();
                LocalDate today = LocalDate.now();
                switch (timePeriod){
                    case "5 days":
                        startDate = today.minusDays(5).toString();
                        break;
                    case "1 week":
                        startDate = today.minusWeeks(1).toString();
                        break;
                    case "2 weeks":
                        startDate = today.minusWeeks(2).toString();
                        break;
                    case "3 weeks":
                        startDate = today.minusWeeks(3).toString();
                        break;
                    case "1 month":
                        startDate = today.minusMonths(1).toString();
                        break;
                    case "2 months":
                        startDate = today.minusMonths(2).toString();
                        break;
                }

                //make queries and get average, to verify if notification needs to be sent
                String avgQueryBodyTemp = "SELECT AVG(info) FROM body_temp_table WHERE date >= '" + startDate + "'";
                float avgBodyTemp = db.getAvgInfo(avgQueryBodyTemp);

                String avgQueryBodyWeight = "SELECT AVG(info) FROM body_weight_table WHERE date >= '" + startDate + "'";
                float avgBodyWeight = db.getAvgInfo(avgQueryBodyWeight);

                String avgQueryBloodPressure = "SELECT AVG(info) FROM blood_pressure_table WHERE date >= '" + startDate + "'";
                float avgBloodPressure = db.getAvgInfo(avgQueryBloodPressure);

                String avgQuerySleepTime = "SELECT AVG(info) FROM sleep_time_table WHERE date >= '" + startDate + "'";
                float avgSleepTime = db.getAvgInfo(avgQuerySleepTime);

                String avgQueryGlycemicIndex = "SELECT AVG(info) FROM glycemic_index_table WHERE date >= '" + startDate + "'";
                float avgGlycemicIndex = db.getAvgInfo(avgQueryGlycemicIndex);

                String avgQueryHeartRate = "SELECT AVG(info) FROM heart_rate_table WHERE date >= '" + startDate + "'";
                float avgHeartRate = db.getAvgInfo(avgQueryHeartRate);

                //open mainactivity if user click on notification
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                //build notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.bg_calend)
                        .setContentTitle("Threshold Reached!")
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                //if average is greater (or smaller for sleeptime) than the threshold, and the switch is on, than
                //send notification with the correct notificationID
                if(avgBodyTemp >= Integer.parseInt(etBodyTemp.getText().toString()) && bodyTemp ) {
                    builder.setContentText("Keep your body temperature under control!");
                    notificationManager.notify(notifBodyTemp, builder.build());
                }
                if(avgBloodPressure >= Integer.parseInt(etBloodPressure.getText().toString()) && bloodPressure ) {
                    builder.setContentText("Keep your blood pressure under control!");
                    notificationManager.notify(notifBloodPressure, builder.build());
                }
                if(avgSleepTime <= Integer.parseInt(etSleepTime.getText().toString()) && sleepTime ) {
                    builder.setContentText("Keep your sleeping time under control!");
                    notificationManager.notify(notifSleepTime, builder.build());
                }
                if(avgBodyWeight <= Integer.parseInt(etBodyWeight.getText().toString()) && bodyWeight ) {
                    builder.setContentText("Keep your body weight under control!");
                    notificationManager.notify(notifBodyWeight, builder.build());
                }
                if(avgGlycemicIndex >= Integer.parseInt(etGlycemicIndex.getText().toString()) && glycemicIndex ) {
                    builder.setContentText("Keep your glycemic index under control!");
                    notificationManager.notify(notifGlycemicIndex, builder.build());
                }
                if(avgHeartRate >= Integer.parseInt(etHeartRate.getText().toString()) && heartRate ) {
                    builder.setContentText("Keep your heart rate under control!");
                    notificationManager.notify(notifHeartRate, builder.build());
                }
                Toast.makeText(getApplicationContext(), "Settings Saved!", Toast.LENGTH_SHORT).show();
            }


        });





    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ChannelName";
            String description = "ChannelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
