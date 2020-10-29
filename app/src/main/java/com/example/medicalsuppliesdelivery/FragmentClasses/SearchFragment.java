package com.example.medicalsuppliesdelivery.FragmentClasses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.Adapters.NewArrivalsAdapter;
import com.example.medicalsuppliesdelivery.Adapters.PopularAdapter;
import com.example.medicalsuppliesdelivery.Adapters.ProductsAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private GridLayoutManager gridLayoutManager2;
    private PopularAdapter popularAdapter;
    private ProductsAdapter productsAdapter;

    private RecyclerView recycle2;

    private ArrayList<Products> productsAll;
    private ArrayList<Products> products2;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.searchAll);
        recycle2 = (RecyclerView) v.findViewById(R.id.recycler2);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager2 = new GridLayoutManager(getContext(), 2);
        searchView = (SearchView) v.findViewById(R.id.searchButn);
        layoutManager = new LinearLayoutManager(getContext());
        //getData();
        getPopular();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productsAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return v;
    }

    private void getPopular() {
        database = FirebaseDatabase.getInstance();
        reference2 = database.getReference().child("Popular");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products2 = new ArrayList<Products>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Products products = new Products();
                    products.setName(snapshot1.child("name").getValue().toString());
                    products.setPrice(((Long)snapshot1.child("price").getValue()).intValue());
                    products.setRating(snapshot1.child("rating").getValue().toString());
                    products.setDescription(snapshot1.child("description").getValue().toString());
                    products.setImgUrl(snapshot1.child("imgurl").getValue().toString());
                    products2.add(products);
                }
                //popularAdapter = new PopularAdapter(getContext());
                productsAdapter = new ProductsAdapter(getContext());
                productsAdapter.addAll(products2);
                recycle2.setLayoutManager(gridLayoutManager2);
                recycle2.setAdapter(productsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getData() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("NewArrivals");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsAll = new ArrayList<Products>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Products products = new Products();
                    products.setName(snapshot1.child("name").getValue().toString());
                    products.setPrice(((Long)snapshot1.child("price").getValue()).intValue());
                    products.setRating(snapshot1.child("rating").getValue().toString());
                    products.setDescription(snapshot1.child("description").getValue().toString());
                    products.setImgUrl(snapshot1.child("imgurl").getValue().toString());
                    productsAll.add(products);
                }
                //popularAdapter = new PopularAdapter(getContext());
                productsAdapter = new ProductsAdapter(getContext());
                productsAdapter.addAll(productsAll);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(productsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
