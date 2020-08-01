package com.example.phm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListReport extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    String importance;
    static String myTable, sendTable, date;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_report);

        listView = findViewById(R.id.listView);

        db = new DatabaseHelper(this);

        //get date and importance from the intent
        Bundle b = getIntent().getExtras();
        if( b != null) {
            date = b.getString("date");
            importance = b.getString("importance");
        }
        //queries to get importance of each info
        String qBodyTemp = "SELECT importance_level FROM importance_table WHERE info_name = 'body_temperature_table'";
        String qBloodPressure = "SELECT importance_level FROM importance_table WHERE info_name = 'blood_pressure_table'";
        String qBodyWeight = "SELECT importance_level FROM importance_table WHERE info_name = 'body_weight_table'";
        String qSleepTime = "SELECT importance_level FROM importance_table WHERE info_name = 'sleep_time_table'";
        String qHeartRate = "SELECT importance_level FROM importance_table WHERE info_name = 'heart_rate_table'";
        String qGlycemicIndex = "SELECT importance_level FROM importance_table WHERE info_name = 'glycemic_index_table'";

        //filter tables based on importance, get only tables whose importance is greater than
        //the one chosen
        String query = "SELECT info_name FROM importance_table WHERE importance_level >= " + importance;
        final ArrayList<String> importantTables = db.getImportanceLevel(query);
        final ArrayList<String> readableTable = new ArrayList<>();
        ArrayList<String> importance = new ArrayList<>();

        //simply transform sql table names to readable table names
        for(int i=0; i < importantTables.size(); i++) {
            if(importantTables.get(i).equals("body_temperature_table")) {
                readableTable.add("Body Temperature");
                importance.add(db.getImportanceLevel(qBodyTemp).get(0));
            }
            if(importantTables.get(i).equals("blood_pressure_table")) {
                readableTable.add("Blood Pressure");
                importance.add(db.getImportanceLevel(qBloodPressure).get(0));
            }
            if(importantTables.get(i).equals("body_weight_table")) {
                readableTable.add("Body Weight");
                importance.add(db.getImportanceLevel(qBodyWeight).get(0));
            }
            if(importantTables.get(i).equals("sleep_time_table")) {
                readableTable.add("Sleep Time");
                importance.add(db.getImportanceLevel(qSleepTime).get(0));
            }
            if(importantTables.get(i).equals("heart_rate_table")) {
                readableTable.add("Heart Rate");
                importance.add(db.getImportanceLevel(qHeartRate).get(0));
            }
            if(importantTables.get(i).equals("glycemic_index_table")) {
                readableTable.add("Glycemic Index");
                importance.add(db.getImportanceLevel(qGlycemicIndex).get(0));
            }

        }

        //transform arraylist to arrays
        String[] arrayImportantTables = readableTable.toArray(new String[0]);
        String[] arrayImportanceLevel = importance.toArray(new String[0]);

        //pass parameters to the adapter and set the listview
        final ImportantTablesAdapter adapter = new ImportantTablesAdapter(this, arrayImportantTables, arrayImportanceLevel);
        listView.setAdapter(adapter);


        //when user click on a row, open ViewListReport
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListReport.this, ViewListReport.class );
                myTable = adapter.getItem(position);
                sendTable = readableToSql(myTable);
                startActivity(intent);
            }
        });

    }

    //get info to send to ViewListReport
    public static String getTable() {
        return sendTable;
    }
    public static String getDate() {
        return date;
    }


    //method to transform readable names to sql table names
    public String readableToSql(String myString) {
        String result = null;
        if(myString.equals("Body Temperature"))
            result = "body_temp_table";
        if(myString.equals("Blood Pressure"))
            result = "blood_pressure_table";
        if(myString.equals("Body Weight"))
            result = "body_weight_table";
        if(myString.equals("Heart Rate"))
            result = "heart_rate_table";
        if(myString.equals("Glycemic Index"))
            result = "glycemic_index_table";
        if(myString.equals("Sleep Time"))
            result = "sleep_time_table";
        return result;
    }

}
//adapter of the single listview row
class ImportantTablesAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] rTable, rImportance;

    ImportantTablesAdapter(Context c, String[] tables, String[] importanceLevel) {
        super(c, R.layout.list_report_row, R.id.tvTable, tables);
        this.context = c;
        this.rTable = tables;
        this.rImportance = importanceLevel;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.list_report_row, parent, false);
        TextView tvTableName = row.findViewById(R.id.tvTableName);
        TextView tvImportance = row.findViewById(R.id.tvImportanceLevel);

        tvTableName.setText(rTable[position]);
        tvImportance.setText(rImportance[position]);
        return row;
    }


}
