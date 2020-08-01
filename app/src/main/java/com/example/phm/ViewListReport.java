package com.example.phm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class ViewListReport extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    String myTable, date;
    boolean noteTemperature, noteWeight = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list_report);


        listView = findViewById(R.id.listView);

        db = new DatabaseHelper(this);

        myTable = ListReport.getTable();
        date = ListReport.getDate();

        String query = "SELECT * FROM " + myTable + " WHERE date >= '" + date + "'" ;
        ArrayList<String> arraylistValues, arrayListDates;
                arraylistValues = db.getInfo(query);
                arrayListDates = db.getDates(query);


        String[] values, dates;
        values = arraylistValues.toArray(new String[0]);
        dates = arrayListDates.toArray(new String[0]);

        View headerView = ((LayoutInflater) ViewListReport.this.getSystemService(LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.view_list_report_header, null, false);

        TextView tvInfo = headerView.findViewById(R.id.tvInfo);
        TextView tvDate = headerView.findViewById(R.id.tvDate);
        TextView tvNote = headerView.findViewById(R.id.tvNote);
        String info = sqlToReadable(myTable);

        listView.addHeaderView(headerView);

        if(values.length == 0)
            tvInfo.setText("N/A");
        else
            tvInfo.setText(info);

        if(noteTemperature)
            tvNote.setText("*All values are intended in degree Celsius*");

        if(noteWeight)
            tvNote.setText("*All values are intended in Kilograms*");

        tvDate.setText(date);


        ValueAdapter adapter = new ValueAdapter(this, values, dates);
        listView.setAdapter(adapter);
    }

    public String sqlToReadable(String myString) {
        String result = null;
        if(myString.equals("body_temp_table")) {
            result = "Body Temperature";
            noteTemperature = true;
        }
        if(myString.equals("blood_pressure_table"))
            result = "Blood Pressure";
        if(myString.equals("body_weight_table")) {
            result = "Body Weight";
            noteWeight = true;
        }
        if(myString.equals("heart_rate_table"))
            result = "Heart Rate";
        if(myString.equals("glycemic_index_table"))
            result = "Glycemic Index";
        if(myString.equals("sleep_time_table"))
            result = "Sleep Time";
        return result;
    }
}

class ValueAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] rValue, rDates;

    ValueAdapter(Context c, String[] value, String[] dates) {
        super(c, R.layout.view_list_report_row, R.id.tvValue, value);
        this.context = c;
        this.rValue = value;
        this.rDates = dates;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.view_list_report_row, parent, false);
        TextView tvValue = row.findViewById(R.id.tvValue);
        TextView tvDates = row.findViewById(R.id.tvDate);

        tvValue.setText(rValue[position]);
        tvDates.setText(rDates[position]);
        return row;
    }
}
