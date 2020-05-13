package com.example.talapehuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class adapterAddList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<itmeList>list;
    TextView pricetxt;
    TextView items;
    TextView counter;
    ImageView remove;
    Button add ;
    CheckBox checkBox;
    int position;
   static   int num;
   boolean aBoolean;

    public adapterAddList(Context context, List<itmeList>itmeLists){
        this.context=context;
        this.list=itmeLists;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.textviewitem,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        items.setText(list.get(position).getName());
        pricetxt.setText(list.get(position).getPrice());
        counter.setText(list.get(position).getI()+"");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            num++;
                Toast.makeText(context, "ds", Toast.LENGTH_SHORT).show();
            }
        });


        checkBox.setChecked(aBoolean);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p =list.get(position).getPrice();
                int price =Integer.parseInt(p);
                int cunt =list.get(position).getI();
                String name =list.get(position).getName();
                ordar(name,price,cunt);

            }
        });
       this.position=position;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View item){
            super(item);
           pricetxt =item.findViewById(R.id.price);
           items =item.findViewById(R.id.item);
           counter=item.findViewById(R.id.count);
           add =item.findViewById(R.id.additem);
           remove=item.findViewById(R.id.remove);
           checkBox =item.findViewById(R.id.box);
        }
    }
        public void ordar(String item,int price,int counter){

        HashMap<String,Object>hashMap =new HashMap<>();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Ordar").child(id);
        price=price*counter;
        hashMap.put("Item",item);
        hashMap.put("Price",price);
        if (counter==0){
            aBoolean =false;
            Toast.makeText(context, "قم بأختيار عدد الكراتين", Toast.LENGTH_SHORT).show();
        }
        else {
            aBoolean =true;
        reference.updateChildren(hashMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(context, "تم تسجيل طلبك", Toast.LENGTH_SHORT).show();
            }
        });}

        }
}
