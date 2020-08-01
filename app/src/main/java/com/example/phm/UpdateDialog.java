package com.example.phm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


public class UpdateDialog extends AppCompatDialogFragment {

    private EditText etRowDate, etRowInfo, etRowON;
    private TextView rowId;
    private DatabaseHelper db;
    private String earlyId, earlyDate, earlyInfo, earlyOptionalNote, selectedTableName;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        db = new DatabaseHelper(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_update_dialog, null);

        rowId = view.findViewById(R.id.rowId);
        rowId.setText(earlyId);
        etRowDate = view.findViewById(R.id.etRowDate);
        etRowDate.setText(earlyDate);
        etRowInfo = view.findViewById(R.id.etRowInfo);
        etRowInfo.setText(earlyInfo);
        etRowON = view.findViewById(R.id.etRowON);
        etRowON.setText(earlyOptionalNote);

        builder.setView(view)
                .setTitle("Modify")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = rowId.getText().toString();
                        String date = etRowDate.getText().toString();
                        String info = etRowInfo.getText().toString();
                        String optionalNote = etRowON.getText().toString();
                        if (db.updateRec(id, date, info, optionalNote, selectedTableName)) {
                            Toast.makeText(getActivity(), "Data updated, refresh to see changes", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "DATA ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return builder.create();
    }

    void getData(String id, String date, String info, String optionalNote, String table_name) {
        earlyId = id;
        earlyDate = date;
        earlyInfo = info;
        earlyOptionalNote = optionalNote;
        selectedTableName = table_name;
    }
}
