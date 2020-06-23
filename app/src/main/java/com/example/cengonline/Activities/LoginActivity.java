package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cengonline.Classes.Student;
import com.example.cengonline.Classes.Teacher;
import com.example.cengonline.Classes.User;
import com.example.cengonline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView mail;
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        mail = findViewById(R.id.mailText);//mailBox
        password = findViewById(R.id.passwordText);//password

        checkCurrentUser();

    }

    public void checkCurrentUser(){
        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
        }
    }

    public void signinPressed(View view){//signin Button
        mAuth.signInWithEmailAndPassword(mail.getText().toString(),password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //giriş başarılı -anasayfaya yönlendir
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*public void signinPressed(View view){//signUp Button
        mAuth.createUserWithEmailAndPassword(mail.getText().toString(),password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Student user = new Student(mail.getText().toString(),password.getText().toString(),
                                    mAuth.getCurrentUser().getUid());

                            databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(user);
                        }
                    }
                });
    }*/
}
