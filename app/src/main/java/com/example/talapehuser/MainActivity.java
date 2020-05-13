package com.example.talapehuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
        FirebaseAuth firebaseAuth;
        DatabaseReference reference;
        EditText name,location;
        Button ok;
        String id,txtname,txtlocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name =findViewById(R.id.name);
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,Main2Activity.class));
        }
        location =findViewById(R.id.location);
        ok =findViewById(R.id.ok);
        firebaseAuth =FirebaseAuth.getInstance();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               firebaseAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                   @Override
                   public void onSuccess(AuthResult authResult) {
                       FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                       id=user.getUid();
                       reference= FirebaseDatabase.getInstance().getReference("User").child(id);
                       txtlocation=location.getText().toString();
                       txtname=name.getText().toString();
                       HashMap<String,String>hashMap =new HashMap<>();
                       hashMap.put("Name",txtname);
                       hashMap.put("Location",txtlocation);
                       hashMap.put("ID",id);
                       reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(MainActivity.this,Main2Activity.class));
                           }
                       });
                   }
               });
            }
        });
    }
}
