package com.example.medicalsuppliesdelivery.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class NewArrivalsAdapter extends RecyclerView.Adapter<NewArrivalsAdapter.MyViewHolder> {
    private ArrayList<Products> products;
    private Context context;
    private SuppliesInterface suppliesInterface;

    public NewArrivalsAdapter(ArrayList<Products> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.types_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(products.get(position).getName());
        holder.price.setText("Ksh " + String.valueOf(products.get(position).getPrice()));
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(products.get(position).getImgUrl()).into(holder.imageView);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.addToCart(products.get(position));
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView price;
        private Button btn;
        private ImageView imageView;
        private CardView cardView;
        private LikeButton likeButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.newTitle);
            price = (TextView) itemView.findViewById(R.id.newPrice);
            btn = (Button) itemView.findViewById(R.id.btnNew);
            imageView = (ImageView) itemView.findViewById(R.id.imageNew);
            cardView = (CardView) itemView.findViewById(R.id.cardNew);
            likeButton = (LikeButton) itemView.findViewById(R.id.likeArr);

        }
    }
}
