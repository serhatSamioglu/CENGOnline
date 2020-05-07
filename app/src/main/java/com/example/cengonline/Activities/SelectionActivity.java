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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cengonline.Adapters.SelectionAdapter;
import com.example.cengonline.Classes.Upload;
import com.example.cengonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectionActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    ListView listView;
    SelectionAdapter selectionAdapter;
    ArrayList<Upload> uploads;

    Button addButton;

    //AlertDiaglog
    EditText input;
    AlertDialog alertDialog;

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

        addButton = findViewById(R.id.addButton);
        input = new EditText(this);
        input.setHint("Tap to write");

        if(type.equalsIgnoreCase("Student")){
            addButton.setVisibility(View.GONE);
        }

        selectionAdapter = new SelectionAdapter(uploads,this);
        listView.setAdapter(selectionAdapter);

        handleUploads("Announcements");

        handleAlertDialog();

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
        selectionAdapter.notifyDataSetChanged();
        handleUploads("Announcements");
    }

    public void AssignmentsPressed(View view){
        whichScreen=true;
        uploads.clear();
        selectionAdapter.notifyDataSetChanged();
        handleUploads("Assignments");
    }

    public void addPressed(View view){
        alertDialog.show();
    }

    /*@Override
    protected void onStart() {
        super.onStart();

        if(whichScreen){//ödev
            uploads.clear();
            selectionAdapter.notifyDataSetChanged();
            handleUploads("Assignments");
        }else{//duyuru
            uploads.clear();
            selectionAdapter.notifyDataSetChanged();
            handleUploads("Announcements");
        }
    }*/

    public void handleAlertDialog(){

        String rootName = "";
        if(whichScreen){
            rootName = "assignment";
        }else{
            rootName = "announcement";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add an "+rootName);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadDatabase(input.getText().toString());
                input.getText().clear();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                input.getText().clear();
                dialog.dismiss();
            }
        });

        alertDialog = builder.create();
    }

    public void uploadDatabase(final String input){
        final String rootName;
        if(whichScreen){
            rootName = "Assignments";
        }else{
            rootName = "Announcements";
        }

        databaseReference.child("ServerTime/time").setValue(ServerValue.TIMESTAMP);

        databaseReference.child("ServerTime/time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot timeSnapshot) {
                Long timeLongValue = timeSnapshot.getValue(Long.class);
                String timeStringValue = String.valueOf(timeLongValue);

                databaseReference.child(rootName).child(courseID.toString()).child(timeStringValue).setValue(input);//upload
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
