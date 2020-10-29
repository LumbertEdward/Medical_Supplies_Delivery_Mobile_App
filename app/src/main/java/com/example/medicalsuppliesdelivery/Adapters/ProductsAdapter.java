package com.example.medicalsuppliesdelivery.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.Interfaces.SuppliesInterface;
import com.example.medicalsuppliesdelivery.R;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> implements Filterable{
    private ArrayList<Products> products;
    private Context context;
    private SuppliesInterface suppliesInterface;
    private ArrayList<Products> productsFilter;

    public ProductsAdapter(Context context) {
        this.context = context;
        products = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.try_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(products.get(position).getName());
        holder.price.setText("Ksh " + String.valueOf(products.get(position).getPrice()));
        holder.rate.setText(products.get(position).getRating());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(products.get(position).getImgUrl()).into(holder.photo);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.getData(products.get(position));
            }
        });
        /**holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.getFavorites(products.get(position));
            }
        });**/
        holder.like.setOnLikeListener(new OnLikeListener() {
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
                ArrayList<Products> products1 = new ArrayList<>();
                if (constraint == null || constraint.length() == 0){
                    products1.addAll(productsFilter);
                }
                else {
                    String nm = constraint.toString().toLowerCase().trim();
                    for (Products p : productsFilter){
                        if (p.getName().toLowerCase().contains(nm)){
                            products1.add(p);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = products1;
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
    public void add(Products products2){
        products.add(products2);
        notifyItemInserted(products.size()-1);
    }
    public void addAll(ArrayList<Products> productsArrayList){
        for (Products products1 : productsArrayList){
            add(products1);
        }
        productsFilter = new ArrayList<>(productsArrayList);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView rate;
        private TextView price;
        private LinearLayout linearLayout;
        private ImageView cart;
        private ImageView photo;
        private ImageView fav;
        private LikeButton like;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            /**name = (TextView) itemView.findViewById(R.id.titleLaboratory);
            rate = (TextView) itemView.findViewById(R.id.rateLaboratory);
            price = (TextView) itemView.findViewById(R.id.priceLaboratory);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.cardProduct);
            cart = (ImageView) itemView.findViewById(R.id.cartProduct);
            photo = (ImageView) itemView.findViewById(R.id.imageProduct);**/

            name = (TextView) itemView.findViewById(R.id.titleTry);
            rate = (TextView) itemView.findViewById(R.id.rateTry);
            price = (TextView) itemView.findViewById(R.id.priceTry);
             price = (TextView) itemView.findViewById(R.id.priceTry);
             photo = (ImageView) itemView.findViewById(R.id.imgTry);
             linearLayout = (LinearLayout) itemView.findViewById(R.id.linearTry);
             //fav =
            like = (LikeButton) itemView.findViewById(R.id.likePro);

        }
    }

}
