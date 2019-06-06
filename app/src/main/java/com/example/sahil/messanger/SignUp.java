package com.example.sahil.messanger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class SignUp extends AppCompatActivity{
    MaterialEditText username,email_id,password;
    Button sign_up;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username=findViewById(R.id.username);
        email_id=findViewById(R.id.email);
        password=findViewById(R.id.password);
        sign_up=findViewById(R.id.sign_up);

        auth=FirebaseAuth.getInstance();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name=username.getText().toString();
                String eid=email_id.getText().toString();
                String pass=password.getText().toString();
                if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(eid) || TextUtils.isEmpty(pass)){
                    Toast.makeText(SignUp.this,"Can't be empty",Toast.LENGTH_SHORT).show();
                }
                else if (pass.length()<5){
                    Toast.makeText(SignUp.this,"Password must be atleast 5 characters",Toast.LENGTH_SHORT).show();
                }
                else {
                    register(user_name, eid, pass);
                }
            }
        });
    }

    private void register(final String username, String email_id, String password){
        auth.createUserWithEmailAndPassword(email_id,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser=auth.getCurrentUser();
                    assert firebaseUser!=null;
                    String userid=firebaseUser.getUid();

                    reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username",username);
                    hashMap.put("imageURL","default");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent=new Intent(SignUp.this,ChatWindow.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                else{
                    Toast.makeText(SignUp.this,"Can't register using this emai or password",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}