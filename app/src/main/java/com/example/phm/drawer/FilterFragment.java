package com.example.phm.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.phm.CalendarData;
import com.example.phm.ListData;
import com.example.phm.R;
import com.example.phm.ReportImportance;

public class FilterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        CardView listViewData = v.findViewById(R.id.listViewData);
        listViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListData.class);
                startActivity(intent);
            }
        });

        CardView calendarViewData = v.findViewById(R.id.calendarViewData);
        calendarViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalendarData.class);
                startActivity(intent);
            }
        });

        CardView importanceViewData = v.findViewById(R.id.cdImportance);
        importanceViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportImportance.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
