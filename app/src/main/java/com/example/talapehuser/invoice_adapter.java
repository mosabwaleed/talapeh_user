package com.example.talapehuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class invoice_adapter extends RecyclerView.Adapter<invoice_adapter.ViewHolder> {
    Context context;
    ArrayList <List<itmeList>> list;
    adapterAddList addList;

    public invoice_adapter(Context context, ArrayList<List<itmeList>> list  ) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.invoice_card,parent,false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Double totaloftotal = 0.0;
        RecyclerView itemlistrec = holder.itemlistrec;
        TextView totloftotal = holder.totaloftotal;
        TextView date = holder.date;
        itemlistrec.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        itemlistrec.setLayoutManager(linearLayoutManager);
        itemlistrec.setItemAnimator(new DefaultItemAnimator());
        addList = new adapterAddList(context,list.get(position));
        date.setText(list.get(position).get(0).getDate());
        itemlistrec.setAdapter(addList);
        for (int i = 0 ; i<list.get(position).size();i++){
            if (list.get(position).get(i).getPrice() != null){
                totaloftotal +=(Double.parseDouble(list.get(position).get(i).getPrice())*list.get(position).get(i).getI());
            }
        }
        totloftotal.setText(totaloftotal+"");
    }

    @Override
    public int getItemCount() {
         return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView itemlistrec;
        TextView date , totaloftotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemlistrec = itemView.findViewById(R.id.itemlistrec);
            totaloftotal = itemView.findViewById(R.id.totaloftotal);
            date = itemView.findViewById(R.id.date);
        }
    }
}
