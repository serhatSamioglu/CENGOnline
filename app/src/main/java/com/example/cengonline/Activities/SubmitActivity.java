package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cengonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class SubmitActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    EditText textView;
    Button delete;
    Button submit;
    Button show;

    String type;
    Boolean whichScreen;
    String courseID;
    String uploadID;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth=FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            type= bundle.getString("userType");
            whichScreen =bundle.getBoolean("whichScreen");
            courseID= bundle.getString("courseID");
            uploadID= bundle.getString("uploadID");
            content= bundle.getString("content");
        }

        textView = findViewById(R.id.submitTextView);
        delete = findViewById(R.id.deleteButton);
        submit = findViewById(R.id.submitButton);
        show = findViewById(R.id.showButton);

        handleButtons();
        loadStudentAssigment();


    }

    public void loadStudentAssigment(){
        if(type.equalsIgnoreCase("Student") && whichScreen){//öğrenci ödevinin submit ekranını açtıysa
            databaseReference.child("Uploads").child(courseID).child(uploadID).child(mAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            textView.setText(dataSnapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }else{
            textView.setText(content);
        }
    }

    public void handleButtons(){
        if(type.equalsIgnoreCase("Teacher")){
            delete.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
            if(whichScreen){//ödev
                show.setVisibility(View.VISIBLE);
            }
        }else{//Student
            delete.setVisibility(View.GONE);
            show.setVisibility(View.GONE);
            if(!whichScreen){//duyuru
                submit.setVisibility(View.GONE);
            }else{//ödev
                submit.setVisibility(View.VISIBLE);
            }
        }
    }

    public void submitPressed(View view){
        if(type.equalsIgnoreCase("Teacher")){
            if(!whichScreen){//duyuru
                upload("Announcements/"+courseID+"/"+uploadID);
            }else{//ödev
                upload("Assignments/"+courseID+"/"+uploadID);
            }
        }else{//Student
            upload("Uploads/"+courseID+"/"+uploadID+"/"+mAuth.getCurrentUser().getUid());
        }
    }

    public void deletePressed(View view){
        String rootName = "";
        if(whichScreen){
            rootName = "Assignments";
        }else{
            rootName = "Announcements";
        }

        databaseReference.child(rootName).child(courseID).child(uploadID).removeValue();
        //databaseReference.child("Uploads").child(courseID).child(uploadID).removeValue();//uploads
        finish();
    }

    public void showPressed(View view){
        Intent intent = new Intent(getApplicationContext(),ShowActivity.class);
        intent.putExtra("courseID",courseID);
        intent.putExtra("uploadID",uploadID);
        startActivity(intent);
    }

    public void upload(final String dataPath){
        databaseReference.child(dataPath).setValue(textView.getText().toString());//upload
    }

}
