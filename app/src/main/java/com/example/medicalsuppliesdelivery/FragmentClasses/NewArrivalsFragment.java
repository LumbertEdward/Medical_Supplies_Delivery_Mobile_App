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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.Adapters.NewArrivalsAdapter;
import com.example.medicalsuppliesdelivery.Adapters.ProductsAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NewArrivalsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private ProductsAdapter productsAdapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<Products> productsArrayList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_arrivals, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerNewArrivals);
        progressBar = (ProgressBar) v.findViewById(R.id.progressNew);
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutManager = new LinearLayoutManager(getContext());
        getNewArrivals();
        return v;
    }

    private void getNewArrivals() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("NewArrivals");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                productsArrayList = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()){
                    Products products = new Products();
                    products.setName(snap.child("name").getValue().toString());
                    products.setPrice(((Long)snap.child("price").getValue()).intValue());
                    products.setRating(snap.child("rating").getValue().toString());
                    products.setDescription(snap.child("description").getValue().toString());
                    products.setImgUrl(snap.child("imgurl").getValue().toString());
                    productsArrayList.add(products);
                }
                productsAdapter = new ProductsAdapter(getActivity());
                productsAdapter.addAll(productsArrayList);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(productsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
