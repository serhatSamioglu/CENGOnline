package com.example.cengonline.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.cengonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    String courseID;
    String uploadID;
    String content;

    EditText submitPostTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth=FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            courseID= bundle.getString("courseID");
            uploadID= bundle.getString("uploadID");
            content= bundle.getString("content");
        }


        submitPostTextView = findViewById(R.id.submitPostTextView);
        submitPostTextView.setText(content);

    }

    public void deletePostPressed(View view){
        databaseReference.child("Posts/Contents").child(courseID).child(uploadID).removeValue();//ödevi
        finish();
    }

    public void submitPostPressed(View view){
        databaseReference.child("Posts/Contents").child(courseID).child(uploadID).setValue(submitPostTextView.getText().toString());//upload
        finish();
    }

}
