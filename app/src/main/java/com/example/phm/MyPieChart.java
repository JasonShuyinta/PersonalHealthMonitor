package com.example.phm;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.util.ArrayList;

public class MyPieChart extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    PieChart pieChart;
    DatabaseHelper db;
    Spinner spinnerDateRange;
    Spinner spinnerInfo;
    Button btnShowGraph;

    ArrayList<PieEntry> value = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart);

        pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        spinnerDateRange = findViewById(R.id.dateRange);
        spinnerInfo = findViewById(R.id.spinnerInfo);
        btnShowGraph = findViewById(R.id.btnShowGraph);
        db = new DatabaseHelper(this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.info, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInfo.setAdapter(adapter1);
        spinnerInfo.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.dateRange, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDateRange.setAdapter(adapter2);
        spinnerDateRange.setOnItemSelectedListener(this);


        btnShowGraph.setOnClickListener(new View.OnClickListener() {
            String table = null;
            String startDate = null;

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String info = spinnerInfo.getSelectedItem().toString();
                switch (info) {
                    case "Body Temperature":
                        table = "body_temp_table";
                        break;
                    case "Blood Pressure":
                        table = "blood_pressure_table";
                        break;
                    case "Glycemic Index":
                        table = "glycemic_index_table";
                        break;
                    case "Heart Rate":
                        table = "heart_rate_table";
                        break;
                    case "Body Weight":
                        table = "body_weight_table";
                        break;
                    case "Sleep Time":
                        table = "sleep_time_table";
                        break;
                }

                String dateRange = spinnerDateRange.getSelectedItem().toString();
                LocalDate today = LocalDate.now();
                switch (dateRange) {
                    case "Last 5 days":
                        startDate = today.minusDays(5).toString();
                        break;
                    case "Last week":
                        startDate = today.minusWeeks(1).toString();
                        break;
                    case "Last 2 weeks":
                        startDate = today.minusWeeks(2).toString();
                        break;
                    case "Last month":
                        startDate = today.minusMonths(1).toString();
                        break;
                    case "Max":
                        startDate = "1970-01-01";
                        break;
                }

                String query = "SELECT * FROM " + table + " WHERE date >= '" + startDate + "'";

                ArrayList<String> dates;
                ArrayList<String> information;
                dates = db.getDates(query);
                information = db.getInfo(query);

                value.clear();

                for(int i=0; i<dates.size(); i++) {
                    value.add(new PieEntry(Integer.parseInt(information.get(i)), dates.get(i).substring(5)));
                }

                PieDataSet pieDataSet = new PieDataSet(value, "months");
                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                pieDataSet.setValueTextSize(15);
                pieChart.animateY(2000);

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
