package com.example.medicalsuppliesdelivery.FragmentClasses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.medicalsuppliesdelivery.Adapters.CompleteOrdersAdapter;
import com.example.medicalsuppliesdelivery.Adapters.OrdersAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.Orders;
import com.example.medicalsuppliesdelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CompletedOrders extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CompleteOrdersAdapter completeOrdersAdapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private ArrayList<Orders> ordersArrayList;
    private TextView textView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_completed_orders, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progressComplete);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerCompleteOrders);
        layoutManager = new LinearLayoutManager(getContext());
        textView = (TextView) v.findViewById(R.id.warnComp);
        textView.setVisibility(View.GONE);
        getPending();
        return v;
    }

    private void getPending() {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            database = FirebaseDatabase.getInstance();
            reference = database.getReference().child("CompleteOrders").child(auth.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressBar.setVisibility(View.GONE);
                    ordersArrayList = new ArrayList<Orders>();
                    if (snapshot.exists()){
                        textView.setVisibility(View.GONE);
                        for (DataSnapshot snap2 : snapshot.getChildren()){
                            Orders orders2 = new Orders();
                            orders2.setOrderId(snap2.child("orderId").getValue().toString());
                            orders2.setPrice(((Long)snap2.child("price").getValue()).intValue());
                            ordersArrayList.add(orders2);
                        }
                        completeOrdersAdapter = new CompleteOrdersAdapter(ordersArrayList, getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(completeOrdersAdapter);
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
}
