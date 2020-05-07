package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.cengonline.R;
import com.example.cengonline.Adapters.HomeListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Toolbar toolbar;
    Button addButton;

    ListView listView;
    HomeListAdapter homeListAdapter;
    ArrayList<String > courses;
    ArrayList<Integer> coursesIDs;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        toolbar = findViewById(R.id.homeToolBar);
        addButton = findViewById(R.id.homeCourseAddButton);

        if(type.equalsIgnoreCase("Student")){
            addButton.setVisibility(View.GONE);
        }

        setSupportActionBar(toolbar);

        listView = findViewById(R.id.homeListView);
        courses = new ArrayList<String>();
        coursesIDs = new ArrayList<Integer>();
        homeListAdapter = new HomeListAdapter(courses,this);
        listView.setAdapter(homeListAdapter);

        checkUserType();
        handleCourses();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SelectionActivity.class);
                intent.putExtra("courseID",coursesIDs.get(position));
                intent.putExtra("userType",type);
                startActivity(intent);
            }
        });

    }

    public void handleCourses(){
        databaseReference.child("Dersler").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                courses.add(dataSnapshot.getValue(String.class));
                coursesIDs.add(Integer.parseInt(dataSnapshot.getKey()));
                homeListAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.profilItem:
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.messageItem:
                Intent intent2 = new Intent(getApplicationContext(),UsersActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }

    public void checkCurrentUser(){
        if(mAuth.getCurrentUser()==null){
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkCurrentUser();
    }

    public void checkUserType(){
        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("type")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                type = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addCoursePressed(){
        
    }
}
