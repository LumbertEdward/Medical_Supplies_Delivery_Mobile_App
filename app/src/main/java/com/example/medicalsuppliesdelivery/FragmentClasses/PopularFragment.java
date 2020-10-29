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

import com.example.medicalsuppliesdelivery.Adapters.PopularAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PopularFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PopularAdapter popularAdapter;
    private ProgressBar progressBar;
    private ArrayList<Products> productsArrayList;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private LayoutAnimationController controller;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_popular, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerPopular);
        progressBar = (ProgressBar) v.findViewById(R.id.popularProgress);
        layoutManager = new LinearLayoutManager(getContext());
        controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_slide_right);
        recyclerView.setLayoutAnimation(controller);
        progressBar.setVisibility(View.VISIBLE);
        getPopular();
        return v;
    }

    private void getPopular() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Popular");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                productsArrayList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Products products = new Products();
                    products.setName(snapshot1.child("name").getValue().toString());
                    products.setPrice(((Long)snapshot1.child("price").getValue()).intValue());
                    products.setRating(snapshot1.child("rating").getValue().toString());
                    products.setDescription(snapshot1.child("description").getValue().toString());
                    products.setImgUrl(snapshot1.child("imgurl").getValue().toString());
                    productsArrayList.add(products);
                }
                popularAdapter = new PopularAdapter(getActivity());
                popularAdapter.addAll(productsArrayList);
                recyclerView.setAdapter(popularAdapter);
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onStart() {
        recyclerView.setLayoutAnimation(controller);
        super.onStart();
    }
}
