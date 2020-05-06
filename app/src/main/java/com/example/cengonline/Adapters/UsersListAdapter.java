package com.example.cengonline.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cengonline.Classes.User;
import com.example.cengonline.R;

import java.util.ArrayList;

public class UsersListAdapter extends ArrayAdapter<User> {

    private final ArrayList<User> users;
    private final Activity context;

    public UsersListAdapter(ArrayList<User> users, Activity context) {
        super(context, R.layout.users_list_view,users);
        this.users = users;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();

        View customView = layoutInflater.inflate(R.layout.users_list_view,null,true );

        TextView userMail = customView.findViewById(R.id.usersViewUsersMail);
        userMail.setText(users.get(position).geteMail());
        return customView;
    }
}