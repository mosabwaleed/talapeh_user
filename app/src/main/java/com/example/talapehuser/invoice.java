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
        getitem();
        list = new ArrayList<>();
        new android.os.Handler().postDelayed(new Runnable() {
           @Override
            public void run() {
                invoice_adapter = new invoice_adapter(invoice.this,list);
                invoicerec.setAdapter(invoice_adapter);
            }
        },2000);

    }

    public void getitem (){
        FirebaseDatabase.getInstance().getReference("Order").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) // dates
                         {
                             ncdlist.clear();
                        for (DataSnapshot ds1 : ds.getChildren())//items
                        {
                            if (ds1.getKey().equals("ID") || ds1.getKey().equals("date")){ }
                            else {
                                ncdlist.add (new itmeList(ds1.getKey() , ds1.getValue(Integer.class) , ds.getKey()));
                            }
                        }
                             ArrayList<itmeList> ncdlist1 = new ArrayList<>();
                             ncdlist1.addAll(ncdlist);
                             setprice(ncdlist1);
                             System.out.println(ncdlist);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setprice (final ArrayList<itmeList> ncdlist){
        for (int i = 0 ; i<ncdlist.size() ; i++){
            final int finalI = i;
            FirebaseDatabase.getInstance().getReference("Apps").child("List").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ncdlist.get(finalI).setPrice(dataSnapshot.child(ncdlist.get(finalI).getName()).getValue(String.class));
                    System.out.println(dataSnapshot.child(ncdlist.get(finalI).getName()).getKey() +"          "  +dataSnapshot.child(ncdlist.get(finalI).getName()).getValue(String.class));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        ArrayList<itmeList> ncdlist1 = new ArrayList<>();
        ncdlist1.addAll(ncdlist);
        list.add(ncdlist1);
        System.out.println(list);
    }
}