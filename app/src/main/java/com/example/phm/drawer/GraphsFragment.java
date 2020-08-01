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

import com.example.phm.MyBarChart;
import com.example.phm.MyLineChart;
import com.example.phm.MyPieChart;
import com.example.phm.MyRadarChart;
import com.example.phm.R;

public class GraphsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graphs, container, false);

        CardView cdHistogram = v.findViewById(R.id.cdHistogram);
        cdHistogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyBarChart.class);
                startActivity(intent);
            }
        });

        CardView cdPieChart = v.findViewById(R.id.cdPieChart);
        cdPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPieChart.class);
                startActivity(intent);
            }
        });

        CardView cdLineChart = v.findViewById(R.id.cdLineChart);
        cdLineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyLineChart.class);
                startActivity(intent);
            }
        });

        CardView cdRadarChart = v.findViewById(R.id.cdRadarChart);
        cdRadarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyRadarChart.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
