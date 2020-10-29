package com.example.medicalsuppliesdelivery.FragmentClasses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.ActivitiesClasses.Login;
import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.Interfaces.SuppliesInterface;
import com.example.medicalsuppliesdelivery.R;
import com.google.android.gms.common.server.FavaDiagnosticsEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsFragment extends Fragment {

    private Button btnCart;
    private TextView description;
    private TextView price;
    private TextView rate;
    private ImageView image;
    private TextView title;
    private TextView total;
    private ProgressBar progressBar;
    private Products products;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private LikeButton likeButton;
    private SuppliesInterface suppliesInterface;
    private ImageView inc;
    private ImageView dec;
    private TextView tot;
    private int current;
    private int totCurrent = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            products = bundle.getParcelable("data");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        current = 1;
        btnCart = (Button) v.findViewById(R.id.btnAdd);
        description = (TextView) v.findViewById(R.id.descProd);
        price = (TextView) v.findViewById(R.id.priceProd);
        rate = (TextView) v.findViewById(R.id.rateProd);
        title = (TextView) v.findViewById(R.id.titleProd);
        image = (ImageView) v.findViewById(R.id.imageDetails);
        total = (TextView) v.findViewById(R.id.totalDetails);
        inc = (ImageView) v.findViewById(R.id.increase);
        dec = (ImageView) v.findViewById(R.id.decrease);
        tot = (TextView) v.findViewById(R.id.val);
        progressBar = (ProgressBar) v.findViewById(R.id.progressDetails);
        likeButton = (LikeButton) v.findViewById(R.id.likeDet);
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                suppliesInterface.getFavorites(products);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                suppliesInterface.removeFav(products);

            }
        });
        progressBar.setVisibility(View.VISIBLE);
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current < 1){
                    current = 1;
                    totCurrent = current++;
                    tot.setText(String.valueOf(totCurrent));
                    total.setText("Ksh " + String.valueOf(products.getPrice() * totCurrent));
                }
                else {
                    totCurrent = current++;
                    tot.setText(String.valueOf(totCurrent));
                    total.setText("Ksh " + String.valueOf(products.getPrice() * totCurrent));
                }

            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    totCurrent = current--;
                    if (totCurrent < 1){
                        totCurrent = 1;
                        tot.setText(String.valueOf(totCurrent));
                        total.setText("Ksh " + String.valueOf(products.getPrice() * totCurrent));
                    }
                    else {
                        tot.setText(String.valueOf(totCurrent));
                        total.setText("Ksh " + String.valueOf(products.getPrice() * totCurrent));
                    }

            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                database = FirebaseDatabase.getInstance();
                if (auth.getCurrentUser() != null){
                    sendData();
                }
                else {
                    startActivity(new Intent(getContext(), Login.class));
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    getActivity().finish();
                }
            }
        });
        getData();
        return v;
    }

    public int getCur(){
        current++;
        return current;
    }

    private void getData() {
        progressBar.setVisibility(View.GONE);
        title.setText(products.getName());
        price.setText("Ksh " + String.valueOf(products.getPrice()));
        rate.setText(products.getRating());
        description.setText(products.getDescription());
        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.downloader(new OkHttp3Downloader(getContext()));
        builder.build().load(products.getImgUrl()).into(image);
        tot.setText(String.valueOf(totCurrent));
        total.setText("Ksh " + String.valueOf(products.getPrice() * totCurrent));

    }

    private void sendData() {
        databaseReference = database.getReference("Cart").child(auth.getUid()).push();
        String title = products.getName();
        int quantity = totCurrent;
        int price = products.getPrice() * quantity;
        String rating = products.getRating();
        String description1 = products.getDescription();
        String imgUrl = products.getImgUrl();
        products = new Products(title, rating, price, imgUrl, description1);
        databaseReference.setValue(products);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        suppliesInterface = (SuppliesInterface) context;
    }
}
