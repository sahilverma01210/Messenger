package com.example.sahil.messanger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahil.messanger.Adapter.MessageAdapter;
import com.example.sahil.messanger.Model.Chat;
import com.example.sahil.messanger.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    ImageView profile_pic;
    TextView username;
    ImageButton send_button;
    EditText send_text;

    FirebaseUser firebaseUser;
    DatabaseReference reference;


    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar=findViewById(R.id.toolbar);
        send_button=findViewById(R.id.send_btn);
        send_text=findViewById(R.id.send_text);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_pic=findViewById(R.id.profile_pic);
        username=findViewById(R.id.username);
        intent=getIntent();
        final String userid=intent.getStringExtra("userid");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=send_text.getText().toString();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,msg);
                }else {
                    Toast.makeText(MessageActivity.this,"Can't send empty message",Toast.LENGTH_SHORT).show();
                }
                send_text.setText("");
            }

        });

        reference=FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_pic.setImageResource(R.mipmap.ic_launcher);
                }else{
                    profile_pic.setImageResource(R.mipmap.ic_launcher);//Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_pic);
                }
                readMessage(firebaseUser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessage(final String myid, final String userid, final String imageURL){

        mchat=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://messenger-716bb.firebaseio.com/Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mchat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);

                    /////problemmmmmmmmmmmmmmmmmmmmm
                        if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                            mchat.add(chat);
                        }
                        messageAdapter = new MessageAdapter( MessageActivity.this, mchat, imageURL);
                        recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
