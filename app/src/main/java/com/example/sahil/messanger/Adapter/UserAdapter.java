package com.example.sahil.messanger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahil.messanger.MessageActivity;
import com.example.sahil.messanger.Model.User;
import com.example.sahil.messanger.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(Context mContext,List<User> mUsers){
        this.mUsers=mUsers;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final User user=mUsers.get(position);
        viewHolder.username.setText(user.getUsername());

        if(user.getImageURL().equals("default")){
            viewHolder.profile_pic.setImageResource(R.mipmap.ic_launcher);
        }else{
            viewHolder.profile_pic.setImageResource(R.mipmap.ic_launcher);//Glide.with(mContext).load(user.getImageURL()).into(viewHolder.profile_pic);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_pic;
        public ViewHolder(View itemView){
            super(itemView);
            username=itemView.findViewById(R.id.username);
            profile_pic=itemView.findViewById(R.id.profile_pic);

        }
    }
}
