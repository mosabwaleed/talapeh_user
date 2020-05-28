package com.example.talapehuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class adapterAddList extends RecyclerView.Adapter<adapterAddList.ViewHolder> {
    Context context;
    List<itmeList> list;
    int position;
    static Double num;
    boolean aBoolean;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date date = new Date();
    String date1;


    public adapterAddList(Context context, List<itmeList> itmeLists) {
        this.context = context;
        this.list = itmeLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,  int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.textviewitem, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final TextView counter = holder.counter;
        holder.items.setText(list.get(position).getName());
        holder.pricetxt.setText(list.get(position).getPrice());
        if (list.get(position).getI() > 0) {
            counter.setText(list.get(position).getI()+"");
            if (list.get(position).getPrice() != null) {
                if (list.get(position).getDate() != null ){date1 = list.get(position).getDate();}
                else {date1 = formatter.format(date);}
                FirebaseDatabase.getInstance().getReference("jard")
                        .child(date1)
                        .child(list.get(position).getName())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("dynamicprice")){

                            holder.total.setText(list.get(position).getPrice());
                        }
                        else {
                            Double total1 = Double.parseDouble(counter.getText().toString()) * Double.parseDouble(holder.pricetxt.getText().toString());
                            holder.total.setText(total1 + "");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            holder.totallin.setVisibility(View.VISIBLE);
            holder.add.setVisibility(View.GONE);
            holder.remove.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Double.parseDouble(counter.getText() + "");
                num += 0.5;
                counter.setText(num + "");
                if (holder.checkBox.isChecked()){
                    SharedPreference sharedPreference = new SharedPreference();
                    int pos = sharedPreference.getFavoritewithname(context, list.get(position).getName());
                    sharedPreference.removeFavorite(context, pos);
                    String p = list.get(position).getPrice();
                    Double price = 0.0;
                    try {
                        price = Double.parseDouble(p);
                    } catch (Exception ignored) { }
                    Double cunt = Double.parseDouble(counter.getText() + "");
                    String name = list.get(position).getName();
                    ordar(name, price, cunt);
                }
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Double.parseDouble(counter.getText() + "");
                if (num > 0) {
                    num -= 0.5;
                    counter.setText(num + "");
                    if (holder.checkBox.isChecked()){
                        SharedPreference sharedPreference = new SharedPreference();
                        int pos = sharedPreference.getFavoritewithname(context, list.get(position).getName());
                        sharedPreference.removeFavorite(context, pos);
                        String p = list.get(position).getPrice();
                        Double price = 0.0;
                        try {
                            price = Double.parseDouble(p);
                        } catch (Exception ignored) { }
                        Double cunt = Double.parseDouble(counter.getText() + "");
                        String name = list.get(position).getName();
                        ordar(name, price, cunt);
                    }
                }
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Double num2 = Double.parseDouble(counter.getText() + "");
                    if (num2 == 0) {
                        Toast.makeText(context, "قم بأختيار عدد الكراتين اولا", Toast.LENGTH_SHORT).show();
                        holder.checkBox.toggle();
                    } else {
                            String p = list.get(position).getPrice();
                            Double price = 0.0;
                            try {
                                price = Double.parseDouble(p);
                            } catch (Exception ignored) {
                            }
                            Double cunt = Double.parseDouble(counter.getText() + "");
                            String name = list.get(position).getName();
                            ordar(name, price, cunt);
                            Main2Activity.order.setVisibility(View.VISIBLE);

                    }
                } else {
                    SharedPreference sharedPreference = new SharedPreference();
                    int pos = sharedPreference.getFavoritewithname(context, list.get(position).getName());
                    counter.setText("0");
                    sharedPreference.removeFavorite(context, pos);
                    if (sharedPreference.getFavorites(context).size() == 0) {
                        Main2Activity.order.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //@Override
    //public long getItemId(int position) {
        //return position;
    //}

    public void ordar(String item, Double price, Double counter) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SharedPreference sharedPreference = new SharedPreference();
        Double totalprice = price * counter;
        hashMap.put("Item", item);
        hashMap.put("TotalPrice", totalprice);
        hashMap.put("Price", price);
        hashMap.put("Counter", (Double) counter);
        sharedPreference.addFavorite(context, hashMap);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pricetxt;
        TextView items;
        TextView counter;
        LinearLayout totallin;
        TextView total;
        ImageView remove;
        Button add;
        CheckBox checkBox;

        public ViewHolder(View item) {
            super(item);
            pricetxt = item.findViewById(R.id.price);
            items = item.findViewById(R.id.item);
            counter = item.findViewById(R.id.count);
            add = item.findViewById(R.id.additem);
            remove = item.findViewById(R.id.remove);
            checkBox = item.findViewById(R.id.box);
            totallin = item.findViewById(R.id.totallin);
            total = item.findViewById(R.id.total);
            counter.setText("0.0");
        }
    }
}