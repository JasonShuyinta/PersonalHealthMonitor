package com.example.phm.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.phm.BloodPressure;
import com.example.phm.BodyTemp;
import com.example.phm.BodyWeight;
import com.example.phm.DatabaseHelper;
import com.example.phm.GlycemicIndex;
import com.example.phm.HeartRate;
import com.example.phm.R;
import com.example.phm.Sleep;

import java.util.ArrayList;

public class ReportFragment extends Fragment {
    TextView tvILBodyTemp, tvILBloodPressure, tvILHeartRate, tvILSleepTime, tvILGlycemicIndex, tvILBodyWeight;
    DatabaseHelper db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());
        View v =  inflater.inflate(R.layout.fragment_report, container, false);

        tvILBodyTemp = v.findViewById(R.id.tvILBodyTemp);
        tvILBloodPressure = v.findViewById(R.id.tvILBloodPressure);
        tvILHeartRate = v.findViewById(R.id.tvILHeartRate);
        tvILSleepTime = v.findViewById(R.id.tvILSleepTime);
        tvILGlycemicIndex = v.findViewById(R.id.tvILGlycemicIndex);
        tvILBodyWeight = v.findViewById(R.id.tvILBodyWeight);



        LinearLayout llBodyTemp = v.findViewById(R.id.llBodyTemp);
        LinearLayout llBloodPressure = v.findViewById(R.id.llBloodPressure);
        LinearLayout llBodyWeight = v.findViewById(R.id.llBodyWeight);
        LinearLayout llSleepTime = v.findViewById(R.id.llSleepTime);
        LinearLayout llGlycemicIndex = v.findViewById(R.id.llGlycemicIndex);
        LinearLayout llHeartRate = v.findViewById(R.id.llHeartRate);

        ArrayList<String> arrayBodyTemp, arrayBloodPressure, arrayBodyWeight,
                arrayHeartRate, arraySleepTime, arrayGlycemicIndex;

        //Queries to get the importance level of each information
        String qBodyTemp = "SELECT importance_level FROM importance_table WHERE info_name = 'body_temperature_table'";
        String qBloodPressure = "SELECT importance_level FROM importance_table WHERE info_name = 'blood_pressure_table'";
        String qBodyWeight = "SELECT importance_level FROM importance_table WHERE info_name = 'body_weight_table'";
        String qSleepTime = "SELECT importance_level FROM importance_table WHERE info_name = 'sleep_time_table'";
        String qHeartRate = "SELECT importance_level FROM importance_table WHERE info_name = 'heart_rate_table'";
        String qGlycemicIndex = "SELECT importance_level FROM importance_table WHERE info_name = 'glycemic_index_table'";

        //switch implemented to change the color of the layout
        //based on the result of the query. The return value being an arraylist
        //we need to get the first element of the arraylist, thus array.get(0)
        arrayBodyTemp = db.getImportanceLevel(qBodyTemp);
        switch (Integer.parseInt(arrayBodyTemp.get(0))){
            case 1:
                llBodyTemp.setBackgroundResource(R.drawable.gradient_1);
                tvILBodyTemp.setText(R.string.one);
                break;
            case 2:
                llBodyTemp.setBackgroundResource(R.drawable.gradient_2);
                tvILBodyTemp.setText(R.string.two);
                break;
            case 3:
                llBodyTemp.setBackgroundResource(R.drawable.gradient_3);
                tvILBodyTemp.setText(R.string.three);
                break;
            case 4:
                llBodyTemp.setBackgroundResource(R.drawable.gradient_4);
                tvILBodyTemp.setText(R.string.four);
                break;
            case 5:
                llBodyTemp.setBackgroundResource(R.drawable.gradient_5);
                tvILBodyTemp.setText(R.string.five);
                break;

        }

        arrayBloodPressure = db.getImportanceLevel(qBloodPressure);
        switch (Integer.parseInt(arrayBloodPressure.get(0))){
            case 1:
                llBloodPressure.setBackgroundResource(R.drawable.gradient_1);
                tvILBloodPressure.setText(R.string.one);
                break;
            case 2:
                llBloodPressure.setBackgroundResource(R.drawable.gradient_2);
                tvILBloodPressure.setText(R.string.two);
                break;
            case 3:
                llBloodPressure.setBackgroundResource(R.drawable.gradient_3);
                tvILBloodPressure.setText(R.string.three);
                break;
            case 4:
                llBloodPressure.setBackgroundResource(R.drawable.gradient_4);
                tvILBloodPressure.setText(R.string.four);
                break;
            case 5:
                llBloodPressure.setBackgroundResource(R.drawable.gradient_5);
                tvILBloodPressure.setText(R.string.five);
                break;
        }


        arrayBodyWeight = db.getImportanceLevel(qBodyWeight);
        switch (Integer.parseInt(arrayBodyWeight.get(0))){
            case 1:
                llBodyWeight.setBackgroundResource(R.drawable.gradient_1);
                tvILBodyWeight.setText(R.string.one);
                break;
            case 2:
                llBodyWeight.setBackgroundResource(R.drawable.gradient_2);
                tvILBodyWeight.setText(R.string.two);
                break;
            case 3:
                llBodyWeight.setBackgroundResource(R.drawable.gradient_3);
                tvILBodyWeight.setText(R.string.three);
                break;
            case 4:
                llBodyWeight.setBackgroundResource(R.drawable.gradient_4);
                tvILBodyWeight.setText(R.string.four);
                break;
            case 5:
                llBodyWeight.setBackgroundResource(R.drawable.gradient_5);
                tvILBodyWeight.setText(R.string.five);
                break;
        }

        arraySleepTime = db.getImportanceLevel(qSleepTime);
        switch (Integer.parseInt(arraySleepTime.get(0))){
            case 1:
                llSleepTime.setBackgroundResource(R.drawable.gradient_1);
                tvILSleepTime.setText(R.string.one);
                break;
            case 2:
                llSleepTime.setBackgroundResource(R.drawable.gradient_2);
                tvILSleepTime.setText(R.string.two);
                break;
            case 3:
                llSleepTime.setBackgroundResource(R.drawable.gradient_3);
                tvILSleepTime.setText(R.string.three);
                break;
            case 4:
                llSleepTime.setBackgroundResource(R.drawable.gradient_4);
                tvILSleepTime.setText(R.string.four);
                break;
            case 5:
                llSleepTime.setBackgroundResource(R.drawable.gradient_5);
                tvILSleepTime.setText(R.string.five);
                break;

        }

        arrayGlycemicIndex = db.getImportanceLevel(qGlycemicIndex);
        switch (Integer.parseInt(arrayGlycemicIndex.get(0))){
            case 1:
                llGlycemicIndex.setBackgroundResource(R.drawable.gradient_1);
                tvILGlycemicIndex.setText(R.string.one);
                break;
            case 2:
                llGlycemicIndex.setBackgroundResource(R.drawable.gradient_2);
                tvILGlycemicIndex.setText(R.string.two);
                break;
            case 3:
                llGlycemicIndex.setBackgroundResource(R.drawable.gradient_3);
                tvILGlycemicIndex.setText(R.string.three);
                break;
            case 4:
                llGlycemicIndex.setBackgroundResource(R.drawable.gradient_4);
                tvILGlycemicIndex.setText(R.string.four);
                break;
            case 5:
                llGlycemicIndex.setBackgroundResource(R.drawable.gradient_5);
                tvILGlycemicIndex.setText(R.string.five);
                break;
        }

        arrayHeartRate = db.getImportanceLevel(qHeartRate);
        switch (Integer.parseInt(arrayHeartRate.get(0))){
            case 1:
                llHeartRate.setBackgroundResource(R.drawable.gradient_1);
                tvILHeartRate.setText(R.string.one);
                break;
            case 2:
                llHeartRate.setBackgroundResource(R.drawable.gradient_2);
                tvILHeartRate.setText(R.string.two);
                break;
            case 3:
                llHeartRate.setBackgroundResource(R.drawable.gradient_3);
                tvILHeartRate.setText(R.string.three);
                break;
            case 4:
                llHeartRate.setBackgroundResource(R.drawable.gradient_4);
                tvILHeartRate.setText(R.string.four);
                break;
            case 5:
                llHeartRate.setBackgroundResource(R.drawable.gradient_5);
                tvILHeartRate.setText(R.string.five);
                break;
        }


        CardView cdBodyTemp = v.findViewById(R.id.cdBodyTemp);
        cdBodyTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BodyTemp.class);
                startActivity(intent);
            }
        });

        CardView cdBloodPressure = v.findViewById(R.id.cdBloodPressure);
        cdBloodPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BloodPressure.class);
                startActivity(intent);
            }
        });

        CardView cdGlycemicIndex = v.findViewById(R.id.cdGlycemicIndex);
        cdGlycemicIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GlycemicIndex.class);
                startActivity(intent);
            }
        });

        CardView cdHeartRate = v.findViewById(R.id.cdHeartRate);
        cdHeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HeartRate.class);
                startActivity(intent);
            }
        });

        CardView cdBodyWeight = v.findViewById(R.id.cdBodyWeight);
        cdBodyWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BodyWeight.class);
                startActivity(intent);
            }
        });

        CardView cdSleep = v.findViewById(R.id.cdSleep);
        cdSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Sleep.class);
                startActivity(intent);
            }
        });

        return v;
    }

}
