package com.example.cengonline.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cengonline.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextView mailTextView;
    Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth=FirebaseAuth.getInstance();

        mailTextView = findViewById(R.id.profileMailTextView);
        signOutButton = findViewById(R.id.profileSignOutButton);

        handleCurrentUserMail();
    }

    public void handleCurrentUserMail(){
        mailTextView.setText(mAuth.getCurrentUser().getEmail());
    }

    public void signOutPressed(View view){
        mAuth.signOut();
        finish();
    }
}
