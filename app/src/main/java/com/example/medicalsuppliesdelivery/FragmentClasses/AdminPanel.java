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
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.Adapters.AdminPanelAdapter;
import com.example.medicalsuppliesdelivery.Adapters.CompleteOrdersAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.Orders;
import com.example.medicalsuppliesdelivery.Interfaces.AdminInterface;
import com.example.medicalsuppliesdelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminPanel extends Fragment {
    private RecyclerView pending;
    private RecyclerView complete;
    private TextView pend;
    private TextView comp;
    private RecyclerView.LayoutManager layoutManager;
    private CompleteOrdersAdapter adapter;
    private AdminPanelAdapter adminPanelAdapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private ArrayList<Orders> ordersArrayList;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_panel, container, false);
        pending = (RecyclerView) v.findViewById(R.id.recyclerAdminPending);
        complete = (RecyclerView) v.findViewById(R.id.recyclerCompleteAdmin);
        pend = (TextView) v.findViewById(R.id.txtWarnPendingAdmin);
        comp = (TextView) v.findViewById(R.id.txtWarnCompleteAdmin);
        progressBar = (ProgressBar) v.findViewById(R.id.progressOrdersAdmin);
        progressBar.setVisibility(View.VISIBLE);
        layoutManager = new LinearLayoutManager(getContext());
        auth = FirebaseAuth.getInstance();
        pend.setVisibility(View.GONE);
        comp.setVisibility(View.GONE);
        getData();
        return v;
    }

    private void getData() {
        if (auth.getCurrentUser() != null) {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference().child("PendingOrders").child(auth.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressBar.setVisibility(View.GONE);
                    pending.setVisibility(View.VISIBLE);
                    pend.setVisibility(View.GONE);
                    ordersArrayList = new ArrayList<Orders>();
                    if (snapshot.exists()) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Orders orders = new Orders();
                            orders.setOrderId(snap.child("orderId").getValue().toString());
                            orders.setPrice(((Long)snap.child("price").getValue()).intValue());
                            ordersArrayList.add(orders);
                        }
                        adminPanelAdapter = new AdminPanelAdapter(ordersArrayList, getActivity());
                        pending.setAdapter(adminPanelAdapter);
                        pending.setLayoutManager(layoutManager);
                    } else {
                        pending.setVisibility(View.GONE);
                        pend.setVisibility(View.VISIBLE);

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
