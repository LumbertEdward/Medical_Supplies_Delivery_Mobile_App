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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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


public class LaboratoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
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
        View v = inflater.inflate(R.layout.fragment_laboratory, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progressP);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerLab);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.grid_from_bottom);
        recyclerView.setLayoutAnimation(controller);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("laboratory");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    productsArrayList = new ArrayList<Products>();
                    for (DataSnapshot shot : snapshot.getChildren()){
                        Products products = new Products();
                        products.setName(shot.child("name").getValue().toString());
                        products.setPrice(((Long)shot.child("price").getValue()).intValue());
                        products.setRating(shot.child("rating").getValue().toString());
                        products.setDescription(shot.child("description").getValue().toString());
                        products.setImgUrl(shot.child("imgurl").getValue().toString());
                        productsArrayList.add(products);
                    }

                    productsAdapter = new ProductsAdapter(getActivity());
                    productsAdapter.addAll(productsArrayList);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(productsAdapter);
                }
                else {
                    Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
