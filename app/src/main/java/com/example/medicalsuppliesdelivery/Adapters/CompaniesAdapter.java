package com.example.medicalsuppliesdelivery.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalsuppliesdelivery.DataClasses.Companies;
import com.example.medicalsuppliesdelivery.Interfaces.SuppliesInterface;
import com.example.medicalsuppliesdelivery.R;

import java.util.ArrayList;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.MyViewHolder> {
    private ArrayList<Companies> companies;
    private Context context;
    private SuppliesInterface suppliesInterface;

    public CompaniesAdapter(ArrayList<Companies> companies, Context context) {
        this.companies = companies;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(companies.get(position).getName());
        holder.location.setText(companies.get(position).getLocation());
        holder.rate.setText(companies.get(position).getRating());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.getCompany(companies.get(position));
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        suppliesInterface = (SuppliesInterface) context;
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView location;
        private TextView rate;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            /**name = (TextView) itemView.findViewById(R.id.companyName);
            location = (TextView) itemView.findViewById(R.id.companyLocation);
            rate = (TextView) itemView.findViewById(R.id.rating);
            cardView = (CardView) itemView.findViewById(R.id.card);**/
        }
    }
}
