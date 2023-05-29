package com.example.roommateproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogActivity extends AppCompatActivity {

    EditText userNameET,passwordET;
    Button loginBtn,logRegBtn;
    FirebaseUser currUser;
    FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        currUser=auth.getCurrentUser();

//        if(currUser!=null){
//           Intent i =new Intent(LogActivity.this,MainActivity.class);
//           startActivity(i);
//           finish();
//       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        userNameET=findViewById(R.id.loginUserET);
        passwordET=findViewById(R.id.loginPasswordET);
        loginBtn=findViewById(R.id.loginButton);
        logRegBtn=findViewById(R.id.loginRegBtn);
        //Firebase
        auth=FirebaseAuth.getInstance();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt=userNameET.getText().toString();
                String password_txt=passwordET.getText().toString();
                if(TextUtils.isEmpty(email_txt)|| TextUtils.isEmpty(password_txt)){
                    Toast.makeText(LogActivity.this, "Bos alan vardir", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(email_txt,password_txt)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        if(email_txt.equals("enes.ozdogan000@gmail.com")){
                                            if(auth.getCurrentUser().isEmailVerified()){
                                                Intent i=new Intent(LogActivity.this,MainActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(i);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(LogActivity.this, "Dogrulama Yapin", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                        }
                                        else{
                                            Intent i=new Intent(LogActivity.this,MainActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                            finish();
                                        }


                                    }
                                    else{
                                        Toast.makeText(LogActivity.this, "Gecersiz Bilgi", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        logRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LogActivity.this,RegActivity.class);
                startActivity(i);
            }
        });
    }
}