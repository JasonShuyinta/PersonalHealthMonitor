package com.example.phm;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.util.ArrayList;

public class MyBarChart extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    BarChart barChart;
    DatabaseHelper db;
    Spinner spinnerDateRange;
    Spinner spinnerInfo;
    ArrayList<String> labelsName;
    Button btnShowGraph;

    //GetInfo type arraylist to create chart
    ArrayList<GetInfo> arrayListGraphData = new ArrayList<>();
    //BarEntry type arraylist
    ArrayList<BarEntry> barEntryArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histogram);

        barChart = findViewById(R.id.histogramGraph);
        spinnerDateRange = findViewById(R.id.dateRange);
        spinnerInfo = findViewById(R.id.spinnerInfo);
        btnShowGraph = findViewById(R.id.btnShowGraph);

        db = new DatabaseHelper(this);

        //for the spinner on info
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.info, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInfo.setAdapter(adapter1);
        spinnerInfo.setOnItemSelectedListener(this);

        //for the spinner on date
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
                //Transform from readable to sql table names
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
                //set the starting day based on spinner item choice
                switch (dateRange){
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

                String query = "SELECT * FROM " +table+ " WHERE date >= '" + startDate + "'";

                ArrayList<String> dates;
                ArrayList<String> information;
                //BarEntry type arraylist
                barEntryArrayList = new ArrayList<>();
                labelsName = new ArrayList<>();
                //get dates and info
                dates = db.getDates(query);
                information = db.getInfo(query);
                //pass arraylists to method fillGraph()
                fillGraph(dates, information);

                //populate barEntryArraylist with the infos retrieved
                //and populate labelsname with the days retrieved
                for(int i=0; i<arrayListGraphData.size(); i++) {
                    String days = arrayListGraphData.get(i).getDate();
                    int infos = arrayListGraphData.get(i).getInformation();
                    barEntryArrayList.add(new BarEntry(i, infos));
                    labelsName.add(days);
                }

                //create Dataset and BarChart
                BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Daily Info");
                Description description = new Description();
                description.setText("Info");
                barChart.setDescription(description);
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextSize(15);


                //Format x axis
                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsName));
                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setGranularity(1f);
                xAxis.setLabelCount(labelsName.size());
                xAxis.setLabelRotationAngle(270);
                barChart.animateY(2000);
                barChart.invalidate();
            }
        });

    }

    //populate Arraylistgraphdata with the values passed
    private void fillGraph(ArrayList<String> dates, ArrayList<String> information) {
        arrayListGraphData.clear();
        for(int i=0; i<dates.size(); i++) {
            arrayListGraphData.add(new GetInfo(dates.get(i).substring(5),Integer.parseInt(information.get(i))));
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}


