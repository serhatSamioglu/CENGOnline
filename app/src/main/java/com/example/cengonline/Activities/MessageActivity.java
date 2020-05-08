package com.example.cengonline.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cengonline.Adapters.MessageAdapter;
import com.example.cengonline.Classes.Message;
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
import androidx.annotation.Nullable;
import java.util.List;

public class MessageActivity extends AppCompatActivity{
    ImageView btn_send;
    EditText text_send;

    String currentUserID;
    String chatUserID;

    MessageAdapter messageAdapter;
    List<Message> mChat;
    RecyclerView recyclerView;


    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btn_send=findViewById(R.id.btn_send);
        text_send=findViewById(R.id.text_send);
        mChat=new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        currentUserID=mAuth.getCurrentUser().getUid();

        recyclerView=findViewById(R.id.recyler_View);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            chatUserID= bundle.getString("chatUserID");
        }
        readMessage();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!text_send.getText().toString().equals("")){
                    sendMessage(text_send.getText().toString());
                }
                else{
                    Toast.makeText(MessageActivity.this,"You can't send empty message",Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
    }
    public void sendMessage(final String message){
        FirebaseDatabase.getInstance().getReference().child("ServerTime/time").setValue(ServerValue.TIMESTAMP);
        FirebaseDatabase.getInstance().getReference().child("ServerTime/time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long longTime = dataSnapshot.getValue(Long.class);

                Message tempMessage = new Message(message,currentUserID,longTime);

                //benim adı altıma mesaj yollandı
                FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserID).child(chatUserID).child(String.valueOf(longTime)).setValue(tempMessage);
                //konuştuğum kişi adı altına yollandı
                FirebaseDatabase.getInstance().getReference().child("Messages").child(chatUserID).child(currentUserID).child(String.valueOf(longTime)).setValue(tempMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessage(){
        databaseReference.child("Messages").child(currentUserID).child(chatUserID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot messagesSnapshot, @Nullable String s) {
                mChat.add(messagesSnapshot.getValue(Message.class));
                messageAdapter=new MessageAdapter(MessageActivity.this,mChat);
                recyclerView.setAdapter(messageAdapter);
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
