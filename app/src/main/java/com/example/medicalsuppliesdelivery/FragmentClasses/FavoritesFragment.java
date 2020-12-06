package com.example.medicalsuppliesdelivery.FragmentClasses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.Adapters.FavouritesAdapter;
import com.example.medicalsuppliesdelivery.Adapters.ProductsAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FavoritesFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FavouritesAdapter favouritesAdapter;
    private ProgressBar progressBar;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private ArrayList<Products> productsArrayList;

    private TextView textView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerFav);
        progressBar = (ProgressBar) v.findViewById(R.id.progressFav);
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutManager = new LinearLayoutManager(getContext());
        auth = FirebaseAuth.getInstance();
        textView = (TextView) v.findViewById(R.id.warnFav);
        textView.setVisibility(View.GONE);
        getFavorites();
        return v;
    }

    private void getFavorites() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Favourites").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                productsArrayList = new ArrayList<>();
                if (snapshot.exists()){
                    textView.setVisibility(View.GONE);
                    for (DataSnapshot snap : snapshot.getChildren()){
                        Products products = new Products();
                        products.setName(snap.child("name").getValue().toString());
                        products.setPrice(((Long)snap.child("price").getValue()).intValue());
                        products.setRating(snap.child("rating").getValue().toString());
                        products.setDescription(snap.child("description").getValue().toString());
                        products.setImgUrl(snap.child("imgUrl").getValue().toString());
                        productsArrayList.add(products);
                    }
                    favouritesAdapter = new FavouritesAdapter(getContext());
                    favouritesAdapter.addAll(productsArrayList);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(favouritesAdapter);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }
}
