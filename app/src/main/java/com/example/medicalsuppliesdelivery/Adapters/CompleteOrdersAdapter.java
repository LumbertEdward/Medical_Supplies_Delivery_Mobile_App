package com.example.medicalsuppliesdelivery.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalsuppliesdelivery.DataClasses.Orders;
import com.example.medicalsuppliesdelivery.R;

import java.util.ArrayList;

public class CompleteOrdersAdapter extends RecyclerView.Adapter<CompleteOrdersAdapter.MyViewHolder> {
    private ArrayList<Orders> orders;
    private Context context;

    public CompleteOrdersAdapter(ArrayList<Orders> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public CompleteOrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.orders_pending_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompleteOrdersAdapter.MyViewHolder holder, int position) {
        holder.id.setText(orders.get(position).getOrderId());
        holder.price.setText("Ksh " + String.valueOf(orders.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.orderIdComplete);
            price = (TextView) itemView.findViewById(R.id.amountComplete);
        }
    }
}
