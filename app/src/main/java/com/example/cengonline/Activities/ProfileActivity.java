package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cengonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    String type;

    TextView mailTextView;
    EditText courseidtodelete;
    Button signOutButton;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        mailTextView = findViewById(R.id.profileMailTextView);
        signOutButton = findViewById(R.id.profileSignOutButton);
        deleteButton = findViewById(R.id.button2);
        courseidtodelete = findViewById(R.id.courseidtodelete);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            type =bundle.getString("userType");

            if(type.equalsIgnoreCase("Student")){
                courseidtodelete.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            }
        }

        handleCurrentUserMail();


    }

    public void deletePressed(View view){
        databaseReference.child("EnrolledCourses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.hasChild(courseidtodelete.getText().toString())){
                    databaseReference.child("EnrolledCourses").child(dataSnapshot.getKey())
                            .child(courseidtodelete.getText().toString()).removeValue();
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

    public void handleCurrentUserMail(){
        mailTextView.setText(mAuth.getCurrentUser().getEmail());
    }

    public void signOutPressed(View view){
        mAuth.signOut();
        finish();
    }
}
