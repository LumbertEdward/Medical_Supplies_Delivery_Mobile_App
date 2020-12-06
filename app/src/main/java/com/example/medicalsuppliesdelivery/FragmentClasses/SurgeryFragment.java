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

import com.example.medicalsuppliesdelivery.Adapters.ProductsAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SurgeryFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ProductsAdapter productsAdapter;
    private ProgressBar progressBar;
    private ArrayList<Products> productsArrayList;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_surgery, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progressS);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerSur);
        linearLayoutManager = new LinearLayoutManager(getContext());
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Surgery");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    productsArrayList = new ArrayList<Products>();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        Products products = new Products();
                        products.setName(snapshot1.child("name").getValue().toString());
                        products.setPrice(((Long)snapshot1.child("price").getValue()).intValue());
                        products.setRating(snapshot1.child("rating").getValue().toString());
                        products.setDescription(snapshot1.child("description").getValue().toString());
                        products.setImgUrl(snapshot1.child("imgurl").getValue().toString());
                        productsArrayList.add(products);
                    }
                    productsAdapter = new ProductsAdapter(getActivity());
                    productsAdapter.addAll(productsArrayList);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(productsAdapter);
                }
                else {
                    Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }
}
