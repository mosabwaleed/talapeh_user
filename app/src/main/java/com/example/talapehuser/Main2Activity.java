package com.example.talapehuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    adapterAddList addList;
    RecyclerView recyclerView;
    List<itmeList> lists;
    String key,price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView =findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        lists =new ArrayList<itmeList>();
        addList = new adapterAddList(this,lists);
        FirebaseDatabase.getInstance().getReference("Apps").child("List")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lists.clear();
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            key= snapshot.getKey();
                            assert key != null;
                            price =snapshot.child(key).getValue(String.class);
                            int i =0;
                            System.out.println(i);
                            lists.add(new itmeList(key,price,i));
                        }
                        recyclerView.setAdapter(addList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
