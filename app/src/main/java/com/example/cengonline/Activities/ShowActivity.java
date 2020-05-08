package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cengonline.Adapters.UsersListAdapter;
import com.example.cengonline.Classes.User;
import com.example.cengonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowActivity extends AppCompatActivity {

    String courseID;
    String uploadID;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<User> users;
    //ArrayList<String> usersIDs;
    ListView listView;
    UsersListAdapter usersListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            courseID= bundle.getString("courseID");
            uploadID= bundle.getString("uploadID");
        }

        mAuth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        users = new ArrayList<User>();
        //usersIDs = new ArrayList<String>();
        listView = findViewById(R.id.usersListView);
        usersListAdapter = new UsersListAdapter(users,this);
        listView.setAdapter(usersListAdapter);

        handleUsers();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(),AssignmentActivity.class);
                intent.putExtra("courseID",courseID);
                intent.putExtra("uploadID",uploadID);
                intent.putExtra("studentID",users.get(position).getId());
                startActivity(intent);
            }
        });
    }

    public void handleUsers(){
        databaseReference.child("Uploads").child(courseID).child(uploadID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                /**usersIDs.add(dataSnapshot.getKey());
                Log.d("userlists", ": "+usersIDs);*/

                databaseReference.child("Users").child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                        users.add(userSnapshot.getValue(User.class));
                        usersListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
}
