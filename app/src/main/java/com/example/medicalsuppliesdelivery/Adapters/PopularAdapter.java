package com.example.medicalsuppliesdelivery.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.Interfaces.SuppliesInterface;
import com.example.medicalsuppliesdelivery.R;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Products> products;
    private Context context;
    private SuppliesInterface suppliesInterface;
    private ArrayList<Products> productsFiltered;

    public PopularAdapter(Context context) {
        this.context = context;
        products = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.popular_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(products.get(position).getName());
        holder.rate.setText(products.get(position).getRating());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(products.get(position).getImgUrl()).into(holder.imageView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.getData(products.get(position));
            }
        });
        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                suppliesInterface.getFavorites(products.get(position));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                suppliesInterface.removeFav(products.get(position));
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
        return products.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Products> productsArrayList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0){
                    productsArrayList.addAll(productsFiltered);
                }
                else {
                    String nm = constraint.toString().toLowerCase().trim();
                    for (Products p : productsFiltered){
                        if (p.getName().toLowerCase().contains(nm)){
                            productsArrayList.add(p);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = productsArrayList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                products.clear();
                products.addAll((ArrayList) results.values);
                notifyDataSetChanged();

            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView rate;
        private ImageView imageView;
        private ImageView fav;
        private LinearLayout linearLayout;
        private LikeButton likeButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.titlePopular);
            rate = (TextView) itemView.findViewById(R.id.ratePop);
            imageView = (ImageView) itemView.findViewById(R.id.imagePopular);
            //fav = (ImageView) itemView.findViewById(R.id.favPopular);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearPop);
            likeButton = (LikeButton) itemView.findViewById(R.id.likePop);
        }
    }
    public void add(Products productsP){
        products.add(productsP);
        notifyItemInserted(products.size()-1);
    }
    public void addAll(ArrayList<Products> products1){
        for (Products products2 : products1){
            products.add(products2);
        }
        productsFiltered = new ArrayList<>(products1);
    }
}
