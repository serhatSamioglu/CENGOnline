package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
    TextView whichpageTextView;

    //AlertDiaglog
    EditText AnnouncementInputView;
    EditText AssigmentInputView;
    EditText PostInputView;

    AlertDialog AnnouncementAlertDialog;
    AlertDialog AssigmentAlertDialog;
    AlertDialog PostAlertDialog;


    Integer courseID;
    String type;
    Boolean whichScreen;//false = Announcements,true = Assignments
    Boolean isStream;//true = stream page open

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
        isStream = false;

        whichpageTextView = findViewById(R.id.whichpageTextView);
        whichpageTextView.setText("Announcements");
        addButton = findViewById(R.id.addButton);

        AnnouncementInputView = new EditText(this);
        AnnouncementInputView.setHint("Tap to write");

        AssigmentInputView = new EditText(this);
        AssigmentInputView.setHint("Tap to write");

        PostInputView = new EditText(this);
        PostInputView.setHint("Tap to write");

        if(type.equalsIgnoreCase("Student") && !isStream){
            addButton.setVisibility(View.GONE);
        }/*else if(type.equalsIgnoreCase("Teacher")){
            addButton.setVisibility(View.VISIBLE);
        }*/

        selectionAdapter = new SelectionAdapter(uploads,this);
        listView.setAdapter(selectionAdapter);

        handleUploads("Announcements");

        handleAnnouncementAlertDialog();
        handleAssignmentAlertDialog();
        handlePostAlertDialog();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(!isStream){
                    Intent intent = new Intent(getApplicationContext(),SubmitActivity.class);
                    intent.putExtra("userType",type);
                    intent.putExtra("whichScreen",whichScreen);
                    intent.putExtra("courseID",courseID.toString());
                    intent.putExtra("uploadID",uploads.get(position).getId());
                    intent.putExtra("content",uploads.get(position).getContent());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(),StreamActivity.class);
                    intent.putExtra("userType",type);
                    intent.putExtra("courseID",courseID.toString());
                    intent.putExtra("uploadID",uploads.get(position).getId());
                    intent.putExtra("content",uploads.get(position).getContent());
                    startActivity(intent);
                }
            }
        });
    }

    public void handleUploads(String uploadType){
        databaseReference.child(uploadType+"/"+courseID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Upload upload = new Upload(dataSnapshot.getKey(),dataSnapshot.getValue(String.class));
                //uploads.add(upload);
                uploads.add(0,upload);
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
        if(type.equalsIgnoreCase("Student")){
            addButton.setVisibility(View.GONE);
        }
        whichpageTextView.setText("Announcements");
        whichScreen=false;
        isStream = false;
        uploads.clear();
        selectionAdapter.notifyDataSetChanged();
        handleUploads("Announcements");
    }

    public void AssignmentsPressed(View view){
        if(type.equalsIgnoreCase("Student")){
            addButton.setVisibility(View.GONE);
        }
        whichpageTextView.setText("Assignments");
        whichScreen=true;
        isStream = false;
        uploads.clear();
        selectionAdapter.notifyDataSetChanged();
        handleUploads("Assignments");
    }

    public void StreamPressed(View view){
        addButton.setVisibility(View.VISIBLE);
        whichpageTextView.setText("Stream");
        isStream = true;
        uploads.clear();
        selectionAdapter.notifyDataSetChanged();
        handleUploads("Posts/Contents");
    }

    public void addPressed(View view){
        if(isStream){
            PostAlertDialog.show();
        }else{
            if(whichScreen){//assigments
                AssigmentAlertDialog.show();
            }else{
                AnnouncementAlertDialog.show();
            }
        }
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

    public void handleAnnouncementAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add an announcement");
        builder.setView(AnnouncementInputView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadDatabase(AnnouncementInputView.getText().toString());
                AnnouncementInputView.getText().clear();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AnnouncementInputView.getText().clear();
                dialog.dismiss();
            }
        });

        AnnouncementAlertDialog = builder.create();
    }

    public void handleAssignmentAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add an assignment");
        builder.setView(AssigmentInputView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadDatabase(AssigmentInputView.getText().toString());
                AssigmentInputView.getText().clear();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AssigmentInputView.getText().clear();
                dialog.dismiss();
            }
        });

        AssigmentAlertDialog = builder.create();
    }

    public void handlePostAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a post");
        builder.setView(PostInputView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadDatabase(PostInputView.getText().toString());
                PostInputView.getText().clear();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PostInputView.getText().clear();
                dialog.dismiss();
            }
        });

        PostAlertDialog = builder.create();
    }

    public void uploadDatabase(final String input){
        final String rootName;
        if(isStream){
            rootName = "Posts/Contents";
        }else{
            if(whichScreen){
                rootName = "Assignments";
            }else{
                rootName = "Announcements";
            }
        }

        databaseReference.child("ServerTime/time").setValue(ServerValue.TIMESTAMP);

        databaseReference.child("ServerTime/time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot timeSnapshot) {
                Long timeLongValue = timeSnapshot.getValue(Long.class);
                String timeStringValue = String.valueOf(timeLongValue);

                databaseReference.child(rootName).child(courseID.toString()).child(timeStringValue).setValue(input);//upload

                if(isStream){//kimin yolladığını tutuyor
                    databaseReference.child("Posts/PublishedPosts").child(timeStringValue).setValue(mAuth.getCurrentUser().getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
