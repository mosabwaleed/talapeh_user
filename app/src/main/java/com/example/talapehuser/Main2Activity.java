package com.example.talapehuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Main2Activity extends AppCompatActivity {
    adapterAddList addList;
    RecyclerView recyclerView;
    List<itmeList> lists , listdialog;
    String key,price;
    SharedPreference sharedPreference;
    public static ImageView cart;
    public static Button order;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        progressDialog = new ProgressDialog(Main2Activity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait");
        sharedPreference = new SharedPreference();
        sharedPreference.removeallFavorite(Main2Activity.this);
        cart = findViewById(R.id.cart);
        order = findViewById(R.id.order);
        recyclerView =findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        lists =new ArrayList<itmeList>();
        listdialog =new ArrayList<itmeList>();
        addList = new adapterAddList(this,lists);
        FirebaseDatabase.getInstance().getReference("Apps").child("List")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lists.clear();
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            key= snapshot.getKey();
                            assert key != null;
                            price =snapshot.getValue(String.class);
                            Double i =0.0;
                            lists.add(new itmeList(key,price,i));
                        }
                        recyclerView.setAdapter(addList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this,invoice.class));
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listdialog.clear();
                final Dialog dialog = new Dialog(Main2Activity.this);
                dialog.setContentView(R.layout.orderdialog);
                final RecyclerView orderrec = dialog.findViewById(R.id.orderrec);
                final Button ordernow = dialog.findViewById(R.id.ordernow);
                Button cancle = dialog.findViewById(R.id.cancle);
                orderrec.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager =new LinearLayoutManager(Main2Activity.this,LinearLayoutManager.VERTICAL,false);
                orderrec.setLayoutManager(linearLayoutManager);
                orderrec.setItemAnimator(new DefaultItemAnimator());
                ArrayList<HashMap<String,Object>> arrayList = sharedPreference.getFavorites(Main2Activity.this);
                for (int i = 0 ; i<arrayList.size();i++){
                    HashMap<String,Object> map = arrayList.get(i);
                    listdialog.add(new itmeList(map.get("Item")+"",map.get("Price")+"",  Double.parseDouble(map.get("Counter")+"") ,Double.parseDouble(map.get("TotalPrice")+"")));
                }
                adapterAddList adapterAddList = new adapterAddList(Main2Activity.this,listdialog);
                orderrec.setAdapter(adapterAddList);
                ordernow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        final Date date = new Date();
                       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order").child(FirebaseAuth.getInstance().getUid())
                                .child(formatter.format(date));
                       for (int i = 0 ; i<listdialog.size() ; i++){
                           final int finalI = i;
                           databaseReference.child(listdialog.get(i).getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   if (dataSnapshot.hasChild("count")){
                                       Double newcount = dataSnapshot.child("count").getValue(Double.class);
                                       newcount += listdialog.get(finalI).getI();
                                       FirebaseDatabase.getInstance().getReference("Order").child(FirebaseAuth.getInstance().getUid())
                                               .child(formatter.format(date)).child(listdialog.get(finalI).getName()).child("count").setValue(newcount);
                                   }
                                   else {
                                       FirebaseDatabase.getInstance().getReference("Order").child(FirebaseAuth.getInstance().getUid())
                                               .child(formatter.format(date)).child(listdialog.get(finalI).getName()).child("count").setValue(listdialog.get(finalI).getI());
                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });
                           databaseReference.child(listdialog.get(i).getName()).child("price").setValue(listdialog.get(i).getPrice());

                           FirebaseDatabase.getInstance().getReference("jard").child(formatter.format(date)).child(listdialog.get(i).getName()).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                   Date date = new Date();
                                   if (dataSnapshot.exists()){
                                       Double newi = dataSnapshot.getValue(Integer.class) + listdialog.get(finalI).getI();
                                       FirebaseDatabase.getInstance().getReference("jard").child(formatter.format(date)).child(listdialog.get(finalI).getName()).child("count").setValue(newi);
                                   }
                                   else {
                                       FirebaseDatabase.getInstance().getReference("jard").child(formatter.format(date)).child(listdialog.get(finalI).getName()).child("count").setValue(listdialog.get(finalI).getI());
                                       FirebaseDatabase.getInstance().getReference("jard").child(formatter.format(date)).child(listdialog.get(finalI).getName()).child("price").setValue(listdialog.get(finalI).getPrice());
                                       if (listdialog.get(finalI).getPrice().equals("0.0")){
                                           FirebaseDatabase.getInstance().getReference("jard").child(formatter.format(date)).child(listdialog.get(finalI).getName()).child("dynamicprice").setValue("dynamicprice");
                                       }
                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });
                       }
                       databaseReference.child("ID").setValue(FirebaseAuth.getInstance().getUid());
                       databaseReference.child("date").setValue(formatter.format(date));
                       addList.notifyDataSetChanged();
                       recyclerView.setAdapter(addList);
                       order.setVisibility(View.GONE);
                       dialog.dismiss();
                       sharedPreference.removeallFavorite(Main2Activity.this);

                    }
                });
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        orderrec.setAdapter(new adapterAddList(Main2Activity.this,listdialog));
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
