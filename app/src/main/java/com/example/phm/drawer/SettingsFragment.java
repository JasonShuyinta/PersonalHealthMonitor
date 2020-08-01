package com.example.phm.drawer;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.phm.NotificationService;
import com.example.phm.R;
import com.example.phm.SetImportance;
import com.example.phm.Threshold;

import java.util.Calendar;

public class SettingsFragment extends Fragment {

    private final boolean[] vibration = new boolean[1];
    private NotificationManager notificationManager;
    String KEY_TEXT_REPLY = "key_text_reply";
    TextView tvChosenTime;
    String CHANNEL_ID = "200";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);


        Button btnSetNotification = v.findViewById(R.id.btnSetNotification);
        Button btnSave = v.findViewById(R.id.btnSave);
        Switch switchVibration = v.findViewById(R.id.switchVibration);
        Button btnSetImportance = v.findViewById(R.id.btnSetImportance);
        Button btnSetThreshold = v.findViewById(R.id.btnSetThreshold);
        tvChosenTime = v.findViewById(R.id.tvChosenTime);


        //set the vibration if the switch is checked
        switchVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vibration[0] = isChecked;
            }
        });

        //instatiate the notification manager
        notificationManager = (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "200";
        //create the channel through which the notification should communicate.
        createNotificationChannel(CHANNEL_ID);


        btnSetNotification.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);
            @Override
            public void onClick(View v) {
                //open TimePickerDialog and adjust output in a standard way, as we cannote have
                //a time expressed in, for example: 9:4, where it should be 09:04
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String realMin, realHour;
                        if(minute < 10 ) {
                            realMin = "0"+minute;
                        } else realMin = String.valueOf(minute);
                        if(hourOfDay < 10) {
                            realHour = "0"+hourOfDay;
                        } else realHour = String.valueOf(hourOfDay);
                        tvChosenTime.setText(realHour+":"+realMin);

                    }
                }, hour, min, true);
                timePickerDialog.show();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager manager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                Intent myIntent;
                PendingIntent pendingIntent;

                //get hours and minutes from the textview
                int hour = Integer.parseInt(tvChosenTime.getText().toString().substring(0,2));
                int min = Integer.parseInt(tvChosenTime.getText().toString().substring(3));

                //Set calendar object to the chosen time, add seconds so notification should start
                //as soon as the right minute clocks in
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, min);
                calendar.set(Calendar.SECOND, 1);

                //Better handle the notification through NotificationService.java, so pass it here
                myIntent = new Intent(getContext(), NotificationService.class);
                pendingIntent = PendingIntent.getBroadcast(getContext(), 0, myIntent, 0);

                //set Alarm manager so the notification pops up everyday at the same time
                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                Toast.makeText(getContext(), "Settings Saved!", Toast.LENGTH_SHORT).show();

            }
        });

        //handleIntent();

        btnSetImportance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SetImportance.class);
                startActivity(i);
            }
        });

        btnSetThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Threshold.class);
                startActivity(i);
            }
        });


        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String id) {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel =
                new NotificationChannel(id, "Direct reply",  importance);
        channel.setDescription("Example");
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400,
                500, 400, 300, 200, 400});


        notificationManager.createNotificationChannel(channel);
    }
/*
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleIntent() {

        Intent intent = getActivity().getIntent();

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if (remoteInput != null) {

            String inputString = remoteInput.getCharSequence(
                    KEY_TEXT_REPLY).toString();

            tvChosenTime.setText(inputString);

            Notification repliedNotification =
                    new Notification.Builder(getContext(), CHANNEL_ID)
                            .setSmallIcon(
                                    android.R.drawable.ic_dialog_info)
                            .setContentText("Reply received")
                            .build();

            notificationManager.notify(1,
                    repliedNotification);
        }
    }
    */
}
