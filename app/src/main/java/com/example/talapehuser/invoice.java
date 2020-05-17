package com.example.talapehuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.util.JsonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class invoice extends AppCompatActivity {
    RecyclerView invoicerec;
    public ArrayList <List<itmeList>> list;
    invoice_adapter invoice_adapter;
    FirebaseDatabase firebaseDatabase;
    static ArrayList<String> datelist , uidlist;
    static ArrayList<itmeList> ncdlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        invoicerec = findViewById(R.id.invoicrec);
        firebaseDatabase = FirebaseDatabase.getInstance();
        invoicerec.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(invoice.this,LinearLayoutManager.VERTICAL,false);
        invoicerec.setLayoutManager(linearLayoutManager);
        invoicerec.setItemAnimator(new DefaultItemAnimator());
        {
            datelist = new ArrayList<>();
            uidlist = new ArrayList<>();
            ncdlist = new ArrayList<>();
        }
        list = new ArrayList<>();
        getdate();
        new android.os.Handler().postDelayed(new Runnable() {
           @Override
            public void run() {
                invoice_adapter = new invoice_adapter(invoice.this,list);
                invoicerec.setAdapter(invoice_adapter);
            }
        },2000);

    }

    public void getdate (){
        firebaseDatabase.getReference("Order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    datelist.add(ds.getKey().toString());
                }
                after_date(datelist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void after_date (final ArrayList<String>datelist){
        final String[] uid = new String[1];
        for(int i = 0 ; i<datelist.size();i++) {
            final int finalI = i;
            firebaseDatabase.getReference("Order").child(datelist.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                       uid[0] = ds.getKey();
                    }
                    if (uid[0].equals(FirebaseAuth.getInstance().getUid())) {
                        afteruid(uid[0], datelist.get(finalI));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void afteruid (String uid , final String date){
                firebaseDatabase.getReference("Order").child(date).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ncdlist.clear();
                        for ( DataSnapshot ds : dataSnapshot.getChildren()){
                            if (ds.getKey().equals("ID") || ds.getKey().equals("date") || ds.getKey().equals("name")){ }
                            else {
                                ncdlist.add(new itmeList(ds.getKey() , Integer.parseInt(ds.getValue(String.class)) , date));
                            }
                        }
                        getprice(ncdlist);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
    public void getprice (final ArrayList<itmeList>ncdlist){
        for (int i = 0 ; i<ncdlist.size() ; i++) {
            final int finalI = i;
            firebaseDatabase.getReference("Apps").child("List").child(ncdlist.get(i).getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ncdlist.get(finalI).setPrice(dataSnapshot.getValue(String.class));
                        if (ncdlist.get(finalI).getPrice() == null){
                            ncdlist.get(finalI).setPrice(dataSnapshot.getValue(String.class));
                        }
                        System.out.println(ncdlist.get(finalI).getName() + " price " + ncdlist.get(finalI).getPrice());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                }
            });
        }
        ArrayList <itmeList> ncdlist2 = new ArrayList<>();
        ncdlist2.addAll(ncdlist);
        list.add(ncdlist2);
    }
}