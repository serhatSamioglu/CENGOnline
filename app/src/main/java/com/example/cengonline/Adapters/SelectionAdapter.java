package com.example.cengonline.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cengonline.Classes.Upload;
import com.example.cengonline.R;

import java.util.ArrayList;

public class SelectionAdapter extends ArrayAdapter<Upload> {

    private final ArrayList<Upload> uploads;
    private final Activity context;

    public SelectionAdapter(ArrayList<Upload> uploads, Activity context) {
        super(context, R.layout.selection_list_view,uploads);
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();

        View customView = layoutInflater.inflate(R.layout.selection_list_view,null,true );

        TextView courseName = customView.findViewById(R.id.selectionViewTextName);
        courseName.setText(uploads.get(position).getContent());
        return customView;
    }
}