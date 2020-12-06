package com.example.medicalsuppliesdelivery.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.Interfaces.SuppliesInterface;
import com.example.medicalsuppliesdelivery.R;
import com.google.android.material.snackbar.Snackbar;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Products> products;
    private Context context;
    private SuppliesInterface suppliesInterface;
    private ArrayList<Products> productsFilter;
    private static final int FADE_DURATION = 1000;
    private int LAST_POSITION = -1;

    public FavouritesAdapter(Context context) {
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.getData(products.get(position));
            }
        });
        holder.like.setLiked(true);
        holder.like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                suppliesInterface.removeFav(products.get(position));
                Snackbar snackbar = Snackbar.make(likeButton, "Removed from Favourites", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(context.getResources().getColor(R.color.bl));
                View view = snackbar.getView();
                snackbar.setTextColor(context.getResources().getColor(R.color.orange));
                TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
                textView.setMaxLines(1);
                textView.setTextSize( 16 );
                textView.setTypeface(ResourcesCompat.getFont(context, R.font.nunitoregular));
                snackbar.show();
                holder.like.setLiked(false);
            }
        });
        setScaleAnimation(holder.itemView);

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView rate;
        private TextView price;
        private CardView cardView;
        private ImageView cart;
        private ImageView photo;
        private ImageView fav;
        private LikeButton like;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.titleTry);
            rate = (TextView) itemView.findViewById(R.id.rateTry);
            price = (TextView) itemView.findViewById(R.id.priceTry);
            price = (TextView) itemView.findViewById(R.id.priceTry);
            photo = (ImageView) itemView.findViewById(R.id.imgTry);
            cardView = (CardView) itemView.findViewById(R.id.linearTry);
            //fav =
            like = (LikeButton) itemView.findViewById(R.id.likePro);
        }
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
    private void setScaleAnimation(View v){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(FADE_DURATION);
        v.startAnimation(scaleAnimation);
    }
}
