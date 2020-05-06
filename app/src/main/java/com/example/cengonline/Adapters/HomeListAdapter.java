package com.example.cengonline.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cengonline.R;

import java.util.ArrayList;

public class HomeListAdapter extends ArrayAdapter<String> {

    private final ArrayList<String> courses;
    private final Activity context;

    public HomeListAdapter(ArrayList<String> courses, Activity context) {
        super(context, R.layout.home_list_view,courses);
        this.courses = courses;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();

        View customView = layoutInflater.inflate(R.layout.home_list_view,null,true );

        TextView courseName = customView.findViewById(R.id.homeViewCourseName);
        courseName.setText(courses.get(position));
        return customView;
    }
}