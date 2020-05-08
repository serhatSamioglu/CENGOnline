package com.example.cengonline.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cengonline.Classes.Message;
import com.example.cengonline.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>  {

    private Context mContext;
    private List<Message> mMessage;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    public MessageAdapter(Context mContext,List<Message> mMessage){
        this.mMessage=mMessage;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.message_item,parent,false);
        TextView messageTextView=null;

        if (viewType == 1) {//right
            messageTextView= view.findViewById(R.id.right_relative).findViewById(R.id.messageTextViewRight);
            view.findViewById(R.id.right_relative).setVisibility(View.VISIBLE);
        } else {//left
            messageTextView = view.findViewById(R.id.left_relative).findViewById(R.id.messageTextViewLeft);
            view.findViewById(R.id.left_relative).setVisibility(View.VISIBLE);
        }

        return new MessageAdapter.ViewHolder(view, messageTextView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        holder.messageTextView.setText(mMessage.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView messageTextView;
        public ViewHolder(View itemView,TextView messageTextView){
            super(itemView);
            this.messageTextView = messageTextView;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mMessage.get(position).getSender().equals(mAuth.getCurrentUser().getUid())){
            //Right
            return 1;
        }else{
            //Left
            return 0;
        }
    }
}