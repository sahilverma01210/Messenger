package com.example.sahil.messanger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Login extends AppCompatActivity {
    MaterialEditText email_id,password;
    Button sign_in;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar=findViewById(R.id.toolbar);
        email_id=findViewById(R.id.email);
        password=findViewById(R.id.password);
        sign_in=findViewById(R.id.sign_in);

        //setting up the 'bar' layout on top
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth=FirebaseAuth.getInstance();

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eid=email_id.getText().toString();
                String pass=password.getText().toString();
                if(TextUtils.isEmpty(eid) || TextUtils.isEmpty(pass)){
                    Toast.makeText(Login.this,"Can't be empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signInWithEmailAndPassword(eid,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent=new Intent(Login.this,ChatWindow.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(Login.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}