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

import java.util.HashMap;
import java.util.List;

public class adapterAddList extends RecyclerView.Adapter<adapterAddList.ViewHolder> {
    Context context;
    List<itmeList> list;
    int position;
    static int num;
    boolean aBoolean;

    public adapterAddList(Context context, List<itmeList> itmeLists) {
        this.context = context;
        this.list = itmeLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.textviewitem, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        LinearLayout totallin = holder.totallin;
        TextView total = holder.total;
        TextView items = holder.items;
        TextView pricetxt = holder.pricetxt;
        final TextView counter = holder.counter;
        Button add = holder.add;
        final CheckBox checkBox = holder.checkBox;
        ImageView remove = holder.remove;
        items.setText(list.get(position).getName());
        pricetxt.setText(list.get(position).getPrice());
        counter.setText(list.get(position).getI() + "");
        if (list.get(position).getI() > 0) {
            if (list.get(position).getPrice() != null) {
                Double total1 = Integer.parseInt(counter.getText().toString()) * Double.parseDouble(pricetxt.getText().toString());
                total.setText(total1 + "");
            }
            totallin.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.parseInt(counter.getText() + "");
                num += 1;
                counter.setText(num + "");
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.parseInt(counter.getText() + "");
                if (num > 0) {
                    num -= 1;
                    counter.setText(num + "");
                }
            }
        });
        checkBox.setChecked(aBoolean);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int num2 = Integer.parseInt(counter.getText() + "");
                    if (num2 == 0) {
                        Toast.makeText(context, "قم بأختيار عدد الكراتين اولا", Toast.LENGTH_SHORT).show();
                        checkBox.toggle();
                    } else {
                        String p = list.get(position).getPrice();
                        Double price = Double.parseDouble(p);
                        int cunt = Integer.parseInt(counter.getText() + "");
                        String name = list.get(position).getName();
                        ordar(name, price, cunt);
                        Main2Activity.order.setVisibility(View.VISIBLE);

                    }
                } else {
                    SharedPreference sharedPreference = new SharedPreference();
                    int pos = sharedPreference.getFavoritewithname(context, list.get(position).getName());
                    counter.setText("0");
                    System.out.println(pos + "                      mosab");
                    sharedPreference.removeFavorite(context, pos);
                    if (sharedPreference.getFavorites(context).size() == 0) {
                        Main2Activity.order.setVisibility(View.GONE);
                    }
                }
            }
        });

        this.position = position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void ordar(String item, Double price, int counter) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SharedPreference sharedPreference = new SharedPreference();
        Double totalprice = price * counter;
        hashMap.put("Item", item);
        hashMap.put("TotalPrice", totalprice);
        hashMap.put("Price", price);
        hashMap.put("Counter", (int) counter);
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
        }
    }
}