package com.example.phm;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewListData extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    String table_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list_data);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            table_name = b.getString("table_name");
        }

        db = new DatabaseHelper(this);

        listView = findViewById(R.id.listViewData);
        ArrayList<String> idList,dateList, infoList, optionalNoteList;

        String query = "SELECT * FROM " + table_name;

        idList = db.getIds(query);
        dateList = db.getDates(query);
        infoList = db.getInfo(query);
        optionalNoteList = db.getOptionalNote(query);

        String[] id = idList.toArray(new String[0]);
        String[] date = dateList.toArray(new String[0]);
        String[] info = infoList.toArray(new String[0]);
        String[] optionalNote = optionalNoteList.toArray(new String[0]);

        View headerView = ((LayoutInflater) ViewListData.this.getSystemService(LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.view_lista_data_header, null, false);

        TextView tvInfo = headerView.findViewById(R.id.tvInfoType);
        ImageView imageView = headerView.findViewById(R.id.imageView);

        String insertableInfo = sqlToReadable(table_name);
        int insertableImage = sqlToImage(table_name);

        tvInfo.setText(insertableInfo);
        imageView.setImageResource(insertableImage);

        listView.addHeaderView(headerView);

        ListViewAdapter adapter = new ListViewAdapter(this, id, date, info, optionalNote, table_name);
        listView.setAdapter(adapter);
    }

    public String sqlToReadable(String myString) {
        String result = null;
        if(myString.equals("body_temp_table")) {
            result = "Body Temperature";
        }
        if(myString.equals("blood_pressure_table"))
            result = "Blood Pressure";
        if(myString.equals("body_weight_table")) {
            result = "Body Weight";
        }
        if(myString.equals("heart_rate_table"))
            result = "Heart Rate";
        if(myString.equals("glycemic_index_table"))
            result = "Glycemic Index";
        if(myString.equals("sleep_time_table"))
            result = "Sleep Time";
        return result;
    }

    public int sqlToImage(String myString) {
        int result = 0;
        if(myString.equals("body_temp_table")) {
            result = R.drawable.thermo;
        }
        if(myString.equals("blood_pressure_table"))
            result = R.drawable.cuff;
        if(myString.equals("body_weight_table")) {
            result = R.drawable.weightscale;
        }
        if(myString.equals("heart_rate_table"))
            result = R.drawable.hearts;
        if(myString.equals("glycemic_index_table"))
            result = R.drawable.toff;
        if(myString.equals("sleep_time_table"))
            result = R.drawable.alarmclock;
        return result;
    }
}

class ListViewAdapter extends ArrayAdapter<String>  {

    private Context context;
    private String[] rId, rDate, rInfo, rOptionalNote;
    private String table_name;

    ListViewAdapter(Context c,String[] id, String[] date, String[] info, String[] optionalNote, String table_name) {
        super(c, R.layout.view_list_row, R.id.rowId, id);
        this.context = c;
        this.rId = id;
        this.rDate = date;
        this.rInfo = info;
        this.rOptionalNote = optionalNote;
        this.table_name = table_name;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.view_list_row, parent, false);
        final TextView tvId = row.findViewById(R.id.rowId);
        final TextView tvDate = row.findViewById(R.id.rowDate);
        final TextView tvInfo = row.findViewById(R.id.rowInfo);
        final TextView tvOptionalNote = row.findViewById(R.id.rowON);
        final TextView tvTableName = row.findViewById(R.id.table_name);
        Button btnModify = row.findViewById(R.id.btnModify);
        Button btnDelete = row.findViewById(R.id.btnDelete);


        tvId.setText(rId[position]);
        tvDate.setText(rDate[position]);
        tvInfo.setText(rInfo[position]);
        tvOptionalNote.setText(rOptionalNote[position]);
        tvTableName.setText(table_name);

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateDialog(tvId.getText().toString(),
                        tvDate.getText().toString(),
                        tvInfo.getText().toString(),
                        tvOptionalNote.getText().toString(),
                        tvTableName.getText().toString());
                Log.i(tvTableName.getText().toString(), "Nome tabella");
                notifyDataSetChanged();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog(tvId.getText().toString(),
                        tvTableName.getText().toString());
                notifyDataSetChanged();
            }
        });


        return row;
    }

    public void openUpdateDialog(String id, String date, String info, String optionalNote, String table_name) {
        UpdateDialog dialog = new UpdateDialog();
        dialog.getData(id,date,info,optionalNote, table_name);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "Update");
    }

    public void openDeleteDialog(String id,String table_name) {
        DeleteDialog dialog = new DeleteDialog();
        dialog.getData(id, table_name);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "Delete");
    }

}
