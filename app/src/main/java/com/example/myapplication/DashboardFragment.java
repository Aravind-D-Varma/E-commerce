package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardFragment extends Fragment {

    private CustomAdapter listAdapter;
    private ExpandableListView mExpandableLV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mExpandableLV = (ExpandableListView) v.findViewById(R.id.lvExp);
        listAdapter = new CustomAdapter(getContext());
        mExpandableLV.setAdapter(listAdapter);
        collapseAll();
        FloatingActionButton floatingActionButton = v.findViewById(R.id.add_product);
        floatingActionButton.setScaleType(ImageView.ScaleType.CENTER);
        floatingActionButton.setOnClickListener(v1 -> {
            Intent intent = new Intent(getActivity(), addProductPage.class);
            startActivity(intent);
        });

        TextView collapseAll = (TextView)v.findViewById(R.id.categories_collapse);
        collapseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseAll();
            }
        });
        return v;
    }

    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            mExpandableLV.collapseGroup(i);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listAdapter = new CustomAdapter(getContext());
        mExpandableLV.setAdapter(listAdapter);
    }
}
