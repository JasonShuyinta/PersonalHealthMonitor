package com.example.phm;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

//View all data for a specific table on a specific date
public class ListDataOnDate extends AppCompatActivity {

    DatabaseHelper db;
    ListView listView;
    String tableNameQuery, date;
    TextView tvDate, tvTable, tvAverage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_data_on_date);

        //get passed values through bundle
        Bundle b = getIntent().getExtras();
        if( b != null) {
            tableNameQuery = b.getString("tableName");
            date = b.getString("date");
            Log.i(date, "Data");
        }

        //convert sql table name to readable name;
        String tableName = null;
        switch (tableNameQuery) {
            case "body_temp_table":
                tableName = "Body Temperature";
                break;
            case "blood_pressure_table":
                tableName = "Blood Pressure";
                break;
            case "glycemic_index_table":
                tableName = "Glycemic Index";
                break;
            case "heart_rate_table":
                tableName = "Heart Rate";
                break;
            case "body_weight_table":
                tableName = "Body Weight";
                break;
            case "sleep_time_table":
                tableName = "Sleep Time";
                break;
        }

        db = new DatabaseHelper(this);

        listView = findViewById(R.id.listViewOnDate);
        ArrayList<String> idList, infoList, optionalNoteList;

        String query = "SELECT * FROM " + tableNameQuery + " WHERE date = '" + date + "'" ;
        String avgQuery = "SELECT AVG(info) FROM " + tableNameQuery + " WHERE date = '" + date + "'";

        //get average value from the specific table on a specific date
        float avgInfo = db.getAvgInfo(avgQuery);

        //get ids, infos and optionalnote of the specific table on the specific day
        idList = db.getIds(query);
        infoList = db.getInfo(query);
        optionalNoteList = db.getOptionalNote(query);

        //transform arraylist to arrays
        String[] id = idList.toArray(new String[0]);
        String[] info = infoList.toArray(new String[0]);
        String[] optionalNote = optionalNoteList.toArray(new String[0]);

        //create a view to insert a header on top of the listview
        View headerView = ((LayoutInflater) ListDataOnDate.this.getSystemService(LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.header, null, false);

        tvDate = headerView.findViewById(R.id.tvDate);
        tvTable = headerView.findViewById(R.id.tvTable);
        tvAverage = headerView.findViewById(R.id.tvAverage);

        //add header to the listview
        listView.addHeaderView(headerView);

        //if no values are present print this
        if(avgInfo == 0) {
            tvAverage.setText("Average Value: N/A");
            tvDate.setText("N/A");
            tvTable.setText("N/A");
        }
        //else print the average, date and tablename
        else {
            tvAverage.setText("Average Value : " + avgInfo);
            tvDate.setText(date);
            tvTable.setText(tableName + " on ");
        }

        //pass values to the adapter and setAdapter to the listview
        CalendarAdapter adapter = new CalendarAdapter(this, id, info, optionalNote);
        listView.setAdapter(adapter);


    }
}

//adapter to create the single listview row
class CalendarAdapter extends ArrayAdapter<String> {

    private String[] rId, rInfo, rOptionalNote;
    Context context;

    CalendarAdapter(Context c, String[] id, String[] info, String[] optionalNote) {
        super(c,R.layout.list_data_on_date_row, R.id.idRow, id);
        this.context = c;
        this.rId = id;
        this.rInfo = info;
        this.rOptionalNote = optionalNote;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_data_on_date_row, parent, false);
        TextView tvId = row.findViewById(R.id.idRow);
        TextView tvInfo = row.findViewById(R.id.rowInfo);
        TextView tvOptionalNote = row.findViewById(R.id.rowOptionalNote);

        //set values based on the position
        tvId.setText(rId[position]);
        tvInfo.setText(rInfo[position]);
        tvOptionalNote.setText(rOptionalNote[position]);

        return row;
    }
}

