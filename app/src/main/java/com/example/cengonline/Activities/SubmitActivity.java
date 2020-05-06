package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        textView.setText(content);

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

    }
    //load this update.
    public void upload(final String dataPath){
        databaseReference.child(dataPath).setValue(textView.getText().toString());//upload
        /*databaseReference.child("ServerTime/time").setValue(ServerValue.TIMESTAMP);

        databaseReference.child("ServerTime/time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot timeSnapshot) {
                Long timeLongValue = timeSnapshot.getValue(Long.class);
                String timeStringValue = String.valueOf(timeLongValue);

                databaseReference.child(dataPath).child(timeStringValue).setValue(textView.getText());//upload
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

}
