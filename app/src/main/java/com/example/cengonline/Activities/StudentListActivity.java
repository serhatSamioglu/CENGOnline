package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cengonline.Adapters.UsersListAdapter;
import com.example.cengonline.Classes.User;
import com.example.cengonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<User> users;
    ListView listView;
    UsersListAdapter usersListAdapter;

    String courseID;
    String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mAuth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        users = new ArrayList<User>();
        listView = findViewById(R.id.usersListView);
        usersListAdapter = new UsersListAdapter(users,this);
        listView.setAdapter(usersListAdapter);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            courseID= bundle.getString("courseID");
            courseName= bundle.getString("courseName");
        }

        handleUsers();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                enrollCourse(position);
            }
        });
    }

    public void handleUsers(){
        databaseReference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(!dataSnapshot.getValue(User.class).geteMail().equalsIgnoreCase(mAuth.getCurrentUser().getEmail())){
                    users.add(dataSnapshot.getValue(User.class));
                    usersListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void enrollCourse(int position){
        databaseReference.child("EnrolledCourses").child(users.get(position).getId()).child(courseID).setValue(courseName);
        //databaseReference.child("EnrolledCourses").child(mAuth.getCurrentUser().getUid()).child(courseID).setValue(courseName);

        Toast.makeText(getApplicationContext(),users.get(position).geteMail()+" added",Toast.LENGTH_LONG).show();
    }
}
