package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class StreamActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    TextView postTextView;
    EditText CommentInputView;

    ListView listView;
    SelectionAdapter commentAdapter;
    ArrayList<Upload> commentList;

    AlertDialog CommentAlertDialog;

    String type;
    String courseID;
    String uploadID;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth=FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            type= bundle.getString("userType");
            courseID= bundle.getString("courseID");
            uploadID= bundle.getString("uploadID");
            content= bundle.getString("content");
        }

        CommentInputView = new EditText(this);
        CommentInputView.setHint("Tap to write");

        listView = findViewById(R.id.streamListView);
        commentList = new ArrayList<Upload>();
        commentAdapter = new SelectionAdapter(commentList,this);
        listView.setAdapter(commentAdapter);

        handleCommentAlertDialog();

        postTextView = findViewById(R.id.postTextView);
        postTextView.setText(content);
        handleComments();
    }

    public void postTextViewPressed(View view){
        if(type.equalsIgnoreCase("Teacher")){
            Intent intent = new Intent(getApplicationContext(),PostActivity.class);
            intent.putExtra("courseID",courseID);
            intent.putExtra("uploadID",uploadID);
            intent.putExtra("content",content);
            startActivity(intent);
        }
    }

    public void addCommentPressed(View view){
        CommentAlertDialog.show();
    }

    public void handleCommentAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a comment");
        builder.setView(CommentInputView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadComment(CommentInputView.getText().toString());
                CommentInputView.getText().clear();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommentInputView.getText().clear();
                dialog.dismiss();
            }
        });

        CommentAlertDialog = builder.create();
    }

    public void uploadComment(final String input){

        databaseReference.child("ServerTime/time").setValue(ServerValue.TIMESTAMP);

        databaseReference.child("ServerTime/time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot timeSnapshot) {
                Long timeLongValue = timeSnapshot.getValue(Long.class);
                String timeStringValue = String.valueOf(timeLongValue);

                databaseReference.child("Posts/Comments").child(courseID).child(uploadID).child(timeStringValue)
                        .setValue(mAuth.getCurrentUser().getEmail() + ": "+input);//upload

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void handleComments(){
        databaseReference.child("Posts/Comments/"+courseID).child(uploadID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Upload upload = new Upload(snapshot.getKey(),snapshot.getValue(String.class));
                    commentList.add(0,upload);
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
