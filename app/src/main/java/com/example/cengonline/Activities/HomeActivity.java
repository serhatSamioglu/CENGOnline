package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    //AlertDiaglog
    EditText inputCourseCode;
    EditText inputCourseName;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        toolbar = findViewById(R.id.homeToolBar);
        addButton = findViewById(R.id.homeCourseAddButton);

        /*inputCourseCode = new EditText(this);
        inputCourseCode.setHint("Tap to write course id");
        inputCourseName = new EditText(this);
        inputCourseName.setHint("Tap to write course name");*/


        setSupportActionBar(toolbar);

        listView = findViewById(R.id.homeListView);
        courses = new ArrayList<String>();
        coursesIDs = new ArrayList<Integer>();
        homeListAdapter = new HomeListAdapter(courses,this);
        listView.setAdapter(homeListAdapter);

        checkUserType();
        handleCourses();
        handleAlertDialog();

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
        databaseReference.child("EnrolledCourses").child(mAuth.getCurrentUser().getUid())
                .addChildEventListener(new ChildEventListener() {
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
                intent.putExtra("userType",type);
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
                if(type.equalsIgnoreCase("Student")){
                    addButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addCoursePressed(View view){
        alertDialog.show();
    }

    public void handleAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a course");
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_layout, null);

        inputCourseCode = view.findViewById(R.id.inputCourseCode);
        inputCourseName = view.findViewById(R.id.inputCourseName);

        builder.setView(view);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadCourse(inputCourseCode.getText().toString(),inputCourseName.getText().toString());
                inputCourseCode.getText().clear();
                inputCourseName.getText().clear();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputCourseCode.getText().clear();
                inputCourseName.getText().clear();
                dialog.dismiss();
            }
        });

        alertDialog = builder.create();
    }

    public void uploadCourse(String courseCode, String courseName){
        databaseReference.child("Courses").child(courseCode).setValue(courseName);
        databaseReference.child("EnrolledCourses").child(mAuth.getCurrentUser().getUid())
                .child(courseCode).setValue(courseName);

        Intent intent = new Intent(getApplicationContext(),StudentListActivity.class);
        intent.putExtra("courseID",courseCode);
        intent.putExtra("courseName",courseName);
        startActivity(intent);
    }

}
