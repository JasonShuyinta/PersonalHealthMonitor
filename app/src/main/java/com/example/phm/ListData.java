package com.example.phm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListData extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_data);

        db = new DatabaseHelper(this);

        listView = findViewById(R.id.listView);

        //queries to retrieve importance level of eache info
        String qBodyTemp = "SELECT importance_level FROM importance_table WHERE info_name = 'body_temperature_table'";
        String qBloodPressure = "SELECT importance_level FROM importance_table WHERE info_name = 'blood_pressure_table'";
        String qBodyWeight = "SELECT importance_level FROM importance_table WHERE info_name = 'body_weight_table'";
        String qSleepTime = "SELECT importance_level FROM importance_table WHERE info_name = 'sleep_time_table'";
        String qHeartRate = "SELECT importance_level FROM importance_table WHERE info_name = 'heart_rate_table'";
        String qGlycemicIndex = "SELECT importance_level FROM importance_table WHERE info_name = 'glycemic_index_table'";

        ArrayList<String> arrayBodyTemp, arrayBloodPressure, arrayBodyWeight,
                arrayHeartRate, arraySleepTime, arrayGlycemicIndex;

        //array with info names
        String[] title = getResources().getStringArray(R.array.info);

        //create rating bar
        RatingBar ratingBar = new RatingBar(this);

        //get levels of each info and insert them in arraylists
        arrayBodyTemp = db.getImportanceLevel(qBodyTemp);
        arrayBloodPressure = db.getImportanceLevel(qBloodPressure);
        arrayBodyWeight = db.getImportanceLevel(qBodyWeight);
        arrayHeartRate = db.getImportanceLevel(qHeartRate);
        arraySleepTime = db.getImportanceLevel(qSleepTime);
        arrayGlycemicIndex = db.getImportanceLevel(qGlycemicIndex);

        //sub array is used to set the rating bar number of stars, through the importance just now retrieved
        String[] sub = {arrayBodyTemp.get(0),arrayBloodPressure.get(0),arrayGlycemicIndex.get(0),
                arrayHeartRate.get(0),arrayBodyWeight.get(0),arraySleepTime.get(0)};

        //array of icons
        int[] image = {R.drawable.thermo, R.drawable.cuff, R.drawable.toff, R.drawable.hearts, R.drawable.weightscale, R.drawable.alarmclock};

        //set listview's adapter
        MyAdapter adapter = new MyAdapter(this, title, ratingBar, image, sub);
        listView.setAdapter(adapter);

        //open ViewListData when you click on one of the 6 rows, and pass the correct tablename
        //to the activity. if no record is inserted in the table, make Toast appear and do nothing
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long numRows = 0;
                Intent intent = new Intent(ListData.this, ViewListData.class);
                if(position == 0) {
                    numRows = db.getNumRows("body_temp_table");
                    if(numRows == 0) {
                        Toast.makeText(ListData.this, "No data to show, please insert data", Toast.LENGTH_SHORT).show();
                    } else {
                        intent.putExtra("table_name", "body_temp_table");
                        startActivity(intent);
                    }
                }
                if(position == 1) {
                    numRows = db.getNumRows("blood_pressure_table");
                    if(numRows == 0) {
                        Toast.makeText(ListData.this, "No data to show, please insert data", Toast.LENGTH_SHORT).show();
                    } else {
                        intent.putExtra("table_name", "blood_pressure_table");
                        startActivity(intent);
                    }
                }
                if(position == 2) {
                    numRows = db.getNumRows("glycemic_index_table");
                    if(numRows == 0) {
                        Toast.makeText(ListData.this, "No data to show, please insert data", Toast.LENGTH_SHORT).show();
                    } else {
                        intent.putExtra("table_name", "glycemic_index_table");
                        startActivity(intent);
                    }
                }
                if(position == 3) {
                    numRows = db.getNumRows("heart_rate_table");
                    if(numRows == 0) {
                        Toast.makeText(ListData.this, "No data to show, please insert data", Toast.LENGTH_SHORT).show();
                    } else {
                        intent.putExtra("table_name", "heart_rate_table");
                        startActivity(intent);
                    }
                }
                if(position == 4) {
                    numRows = db.getNumRows("body_weight_table");
                    if(numRows == 0) {
                        Toast.makeText(ListData.this, "No data to show, please insert data", Toast.LENGTH_SHORT).show();
                    } else {
                        intent.putExtra("table_name", "body_weight_table");
                        startActivity(intent);
                    }
                }
                if(position == 5) {
                    numRows = db.getNumRows("sleep_time_table");
                    if(numRows == 0) {
                        Toast.makeText(ListData.this, "No data to show, please insert data", Toast.LENGTH_SHORT).show();
                    } else {
                        intent.putExtra("table_name", "sleep_time_table");
                        startActivity(intent);
                    }
                }
            }
        });

    }
}

//Adapter class to make each single listview row
class MyAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] rTitle;
    private String[] rSub;
    private int[] rImage;
    private RatingBar rRatingBar;

    //constructor
    MyAdapter(Context c, String[] title, RatingBar ratingBar, int[] image, String[] sub) {
        super(c, R.layout.list_row, R.id.tvRowTitle, title);
        this.context = c;
        this.rTitle = title;
        this.rRatingBar = ratingBar;
        this.rImage = image;
        this.rSub = sub;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.list_row, parent, false);
        ImageView images = row.findViewById(R.id.image);
        TextView myTitle = row.findViewById(R.id.tvRowTitle);
        RatingBar ratingBar = row.findViewById(R.id.ratings);

        //set values through "position"
        images.setImageResource(rImage[position]);
        myTitle.setText(rTitle[position]);
        ratingBar.setRating(Float.parseFloat(rSub[position]));
        return row;
    }
}
