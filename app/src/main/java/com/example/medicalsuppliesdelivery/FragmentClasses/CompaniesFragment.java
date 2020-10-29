package com.example.medicalsuppliesdelivery.FragmentClasses;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.Adapters.CompaniesAdapter;
import com.example.medicalsuppliesdelivery.Adapters.NewArrivalsAdapter;
import com.example.medicalsuppliesdelivery.Adapters.PopularAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.Companies;
import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.Interfaces.SuppliesInterface;
import com.example.medicalsuppliesdelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompaniesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NewArrivalsAdapter newArrivalsAdapter;
    private ArrayList<Products> productsArrayList;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    private RecyclerView recyclerViewNew;
    private RecyclerView.LayoutManager layoutManagerNew;
    private PopularAdapter adapter;

    private CardView lab;
    private CardView surgery;
    private CardView sterilization;
    private CardView maternity;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private SuppliesInterface suppliesInterface;
    private TextView newArr;
    private TextView pop;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_companies, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerNew);
        progressBar = (ProgressBar) v.findViewById(R.id.progressAll);
        scrollView = (ScrollView) v.findViewById(R.id.scrollAll);
        scrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        newArr = (TextView) v.findViewById(R.id.newArrivals);
        pop = (TextView) v.findViewById(R.id.popular);
        lab = (CardView) v.findViewById(R.id.labCard);
        surgery = (CardView) v.findViewById(R.id.cardSurgery);
        sterilization = (CardView) v.findViewById(R.id.cardSterilisation);
        maternity = (CardView) v.findViewById(R.id.cardMaternity);
        recyclerViewNew = (RecyclerView) v.findViewById(R.id.recyclerNewPopular);
        layoutManagerNew = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerViewNew.setLayoutAnimation(controller);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        categories();
        newArivals();
        popular();
        return v;
    }
    private void categories() {
        lab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.getLabData();
            }
        });
        surgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.getSurgeryData();
            }
        });
        sterilization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.getSterilisationData();
            }
        });
        maternity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.getMaternityData();
            }
        });
    }

    private void newArivals() {
        newArr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.getNewArrivals();
            }
        });
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("NewArrivals");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                if (snapshot.exists()){
                    scrollView.setVisibility(View.VISIBLE);
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
                    newArrivalsAdapter = new NewArrivalsAdapter(productsArrayList, getActivity());
                    recyclerView.setAdapter(newArrivalsAdapter);
                    recyclerView.setLayoutManager(layoutManager);

                }
                else {
                    Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void popular() {
        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.getPopular();
            }
        });
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Popular");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                if (snapshot.exists()){
                    scrollView.setVisibility(View.VISIBLE);
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
                    adapter = new PopularAdapter(getActivity());
                    adapter.addAll(productsArrayList);
                    recyclerViewNew.setLayoutManager(layoutManagerNew);
                    recyclerViewNew.setAdapter(adapter);

                }
                else {
                    Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
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
