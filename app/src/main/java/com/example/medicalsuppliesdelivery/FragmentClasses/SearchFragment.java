package com.example.medicalsuppliesdelivery.FragmentClasses;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.Adapters.NewArrivalsAdapter;
import com.example.medicalsuppliesdelivery.Adapters.PopularAdapter;
import com.example.medicalsuppliesdelivery.Adapters.ProductsAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.Interfaces.SuppliesInterface;
import com.example.medicalsuppliesdelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageView back;
    private ImageView cancel;
    private EditText search;
    private LinearLayoutManager linearLayoutManager;
    private ProductsAdapter productsAdapter;
    private ArrayList<Products> products2;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private SuppliesInterface suppliesInterface;
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
        linearLayoutManager = new LinearLayoutManager(getContext());
        back = (ImageView) v.findViewById(R.id.backSearch);
        cancel = (ImageView) v.findViewById(R.id.cancelSearch);
        cancel.setVisibility(View.GONE);
        search = (EditText) v.findViewById(R.id.searchEdit);
        //getData();
        getPopular();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.onBackPressed();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cancel.setVisibility(View.VISIBLE);
                productsAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                recyclerView.setAdapter(productsAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        suppliesInterface = (SuppliesInterface) context;
    }
}
