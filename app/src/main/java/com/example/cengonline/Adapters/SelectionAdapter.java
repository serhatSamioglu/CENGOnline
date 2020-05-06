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
    private final String type;
    private final String whichScreen;

    public SelectionAdapter(ArrayList<Upload> uploads, Activity context,String type,String whichScreen) {
        super(context, R.layout.selection_list_view,uploads);
        this.uploads = uploads;
        this.context = context;
        this.type = type;
        this.whichScreen = whichScreen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();

        View customView = layoutInflater.inflate(R.layout.selection_list_view,null,true );

        if(type.equalsIgnoreCase("Teacher") && whichScreen.equalsIgnoreCase("Assignments")){
            Button button = customView.findViewById(R.id.selectionViewButton);
            Button button2 = customView.findViewById(R.id.selectionViewButton2);

            button.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);

            button.setOnClickListener(new View.OnClickListener() {//edit
                @Override
                public void onClick(View v) {
                    Log.d("butontest", "onClick: "+v.getId());
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {//show
                @Override
                public void onClick(View v) {
                    Log.d("butontest", "onClick: "+v.getId());
                }
            });
        }

        TextView courseName = customView.findViewById(R.id.selectionViewTextName);
        courseName.setText(uploads.get(position).getContent());
        return customView;
    }
}