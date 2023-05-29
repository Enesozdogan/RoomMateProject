package com.example.roommateproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegActivity extends AppCompatActivity {

    EditText userET,passwordET,emailET;
    Button regBtn;
    FirebaseAuth auth;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        userET=findViewById(R.id.userNameET);
        passwordET=findViewById(R.id.passWordET);
        emailET=findViewById(R.id.regMailET);
        regBtn=findViewById(R.id.regButton);
        auth=FirebaseAuth.getInstance();

        //Butonla Kayit islemini tamamlama
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName_txt=userET.getText().toString();
                String password_txt=passwordET.getText().toString();
                String mail_txt=emailET.getText().toString();
                if(TextUtils.isEmpty(userName_txt)||TextUtils.isEmpty(password_txt) || TextUtils.isEmpty(mail_txt)){
                    Toast.makeText(RegActivity.this, "Butun Degerleri Doldurun veya std uzantili mail girin.", Toast.LENGTH_SHORT).show();
                }
                else{
                    SignUp(userName_txt,mail_txt,password_txt);
                }
            }
        });
    }
    private boolean isValidEmail(String email) {
        return email.endsWith("std.yildiz.edu.tr");
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(RegActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegActivity.this, "Doğrulama e-postası gönderildi!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegActivity.this, "Doğrulama e-postası gönderilemedi.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SignUp(final String userName,String mail,String password){
        auth.createUserWithEmailAndPassword(mail,password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser fbUser=auth.getCurrentUser();

                            String userId=fbUser.getUid();
                            sendEmailVerification(fbUser);
                            dbRef= FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(userId);

                            //HashMap yapisi ile kullanici eslestirme
                            HashMap<String,String> regMap=new HashMap<>();
                            regMap.put("id",userId);
                            regMap.put("username",userName);
                            regMap.put("imageURL","default");
                            regMap.put("department","2024");
                            regMap.put("grade","default");
                            regMap.put("phone","44444444");
                            regMap.put("distance","default");
                            regMap.put("duration","default");
                            regMap.put("status","default");
                            regMap.put("mail",mail);
                            //Ana Aktiviteye Gecis
                            dbRef.setValue(regMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegActivity.this, "Sonraki Giriste Dogrulama Yapin.", Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(RegActivity.this,MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegActivity.this, "Geçersiz İşlem", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}