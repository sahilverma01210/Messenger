package com.example.sahil.messanger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sahil.messanger.MessageActivity;
import com.example.sahil.messanger.Model.Chat;
import com.example.sahil.messanger.Model.User;
import com.example.sahil.messanger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context mContext;
    private List<Chat> mChat;
    private String imageURL;
    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext,List<Chat> mChat,String imageURL){
        this.mChat=mChat;
        this.mContext=mContext;
        this.imageURL=imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }else {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int position) {
        Chat chat=mChat.get(position);
        viewHolder.show_msg.setText(chat.getMessage());

        if(imageURL.equals("default")) {
            viewHolder.profile_pic.setImageResource(R.mipmap.ic_launcher);
        }else {
            viewHolder.profile_pic.setImageResource(R.mipmap.ic_launcher);////Glide.with(mContext).load(imageURL).into(viewHolder.profile_pic);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_msg;
        public ImageView profile_pic;
        public ViewHolder(View itemView){
            super(itemView);
            show_msg=itemView.findViewById(R.id.show_msg);
            profile_pic=itemView.findViewById(R.id.profile_pic);

        }
    }
}

