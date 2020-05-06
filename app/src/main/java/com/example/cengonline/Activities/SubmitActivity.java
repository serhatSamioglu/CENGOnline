package com.example.cengonline.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cengonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SubmitActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    EditText textView;
    Button delete;
    Button submit;

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
        handleButtons();

        textView.setText(content);

    }

    public void handleButtons(){
        if(type.equalsIgnoreCase("Teacher")){
            delete.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
        }else{//Student
            delete.setVisibility(View.GONE);
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
                upload("Announcements");
            }else{//ödev
                upload("Assignments");
            }
        }else{//Student
            upload("Uploads");
        }
    }

    public void deletePressed(View view){

    }

    public void upload(String dataType){

    }
}
