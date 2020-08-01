package com.example.phm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DeleteDialog extends AppCompatDialogFragment {

    private DatabaseHelper db;
    private String earlyId, selectedTableName;
    private TextView tvRowId;

    //Delete dialog, to confirm the users intention of deleting a record from the database
    //cannot reverse this action
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(getActivity());

        db = new DatabaseHelper(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_delete_dialog, null);

        tvRowId = view.findViewById(R.id.rowId);
        tvRowId.setText(earlyId);

        deleteBuilder.setView(view)
                .setTitle("Delete")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = tvRowId.getText().toString();
                        if(db.deleteData(id, selectedTableName) > 0)
                            Toast.makeText(getActivity(), "Data Successfully deleted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "ERROR: could not delete data", Toast.LENGTH_SHORT).show();
                    }
                });
        return deleteBuilder.create();
    }

    void getData(String id, String table_name) {
        earlyId = id;
        selectedTableName = table_name;
    }
}
