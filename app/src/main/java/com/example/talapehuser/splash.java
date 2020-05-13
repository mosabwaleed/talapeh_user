package com.example.talapehuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class splash extends AppCompatActivity {
        FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth =FirebaseAuth.getInstance();
        Thread thread =new Thread(){

            @Override
            public void run() {
                try{
                    sleep(3000);
                }
                catch (Exception e){

                }



            }
        };thread.start();
    }
}
