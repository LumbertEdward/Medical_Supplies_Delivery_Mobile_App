package com.example.medicalsuppliesdelivery.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalsuppliesdelivery.DataClasses.Orders;
import com.example.medicalsuppliesdelivery.R;

import java.util.ArrayList;

public class AdminPanelAdapter extends RecyclerView.Adapter<AdminPanelAdapter.MyViewHolder> {
    private ArrayList<Orders> orders;
    private Context context;

    public AdminPanelAdapter(ArrayList<Orders> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.admin_pending_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.id.setText(orders.get(position).getOrderId());
        holder.price.setText(String.valueOf(orders.get(position).getPrice()));
        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView price;
        private Button complete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.orderIdAdmin);
            price = (TextView) itemView.findViewById(R.id.amountAdmin);
            complete = (Button) itemView.findViewById(R.id.btnComplete);
        }
    }
}
