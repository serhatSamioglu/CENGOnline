package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.cengonline.Adapters.SelectionAdapter;
import com.example.cengonline.Classes.Upload;
import com.example.cengonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectionActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    ListView listView;
    SelectionAdapter selectionAdapter;
    ArrayList<Upload> uploads;

    Integer courseID;

    String type;
    Boolean whichScreen;//false = Announcements,true = Assignments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth=FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            courseID= bundle.getInt("courseID");
            type =bundle.getString("userType");
        }

        listView = findViewById(R.id.selectionListView);
        uploads = new ArrayList<Upload>();
        whichScreen=false;

        selectionAdapter = new SelectionAdapter(uploads,this);
        listView.setAdapter(selectionAdapter);

        handleUploads("Announcements");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SubmitActivity.class);
                intent.putExtra("userType",type);
                intent.putExtra("whichScreen",whichScreen);
                intent.putExtra("courseID",courseID.toString());
                intent.putExtra("uploadID",uploads.get(position).getId());
                intent.putExtra("content",uploads.get(position).getContent());
                startActivity(intent);
            }
        });
    }

    public void handleUploads(String uploadType){
        databaseReference.child(uploadType+"/"+courseID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Upload upload = new Upload(dataSnapshot.getKey(),dataSnapshot.getValue(String.class));
                uploads.add(upload);
                selectionAdapter.notifyDataSetChanged();
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

    public void AnnouncementsPressed(View view){
        whichScreen=false;
        uploads.clear();
        handleUploads("Announcements");
    }

    public void AssignmentsPressed(View view){
        whichScreen=true;
        uploads.clear();
        handleUploads("Assignments");
    }

}
