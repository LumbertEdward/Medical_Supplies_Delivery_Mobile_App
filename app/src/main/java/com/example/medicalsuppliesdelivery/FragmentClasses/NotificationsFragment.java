package com.example.medicalsuppliesdelivery.FragmentClasses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.Adapters.NotificationAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.NotificationsClass;
import com.example.medicalsuppliesdelivery.DataClasses.Orders;
import com.example.medicalsuppliesdelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private NotificationAdapter notificationAdapter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private FirebaseDatabase database;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerNot);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getData();
        return v;
    }

    private void getData() {
        if (firebaseAuth.getCurrentUser() != null){
            reference = database.getReference().child("PendingOrders").child(firebaseAuth.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        List<Orders> notificationsClassesList = new ArrayList<>();
                        for (DataSnapshot snap : snapshot.getChildren()){
                            Orders orders = new Orders();
                            orders.setOrderId(snap.child("orderId").getValue().toString());
                            orders.setPrice(((Long)snap.child("price").getValue()).intValue());
                            orders.setNotification(snap.child("notification").getValue().toString());
                            orders.setDate(snap.child("date").getValue().toString());
                            notificationsClassesList.add(orders);
                        }
                        notificationAdapter = new NotificationAdapter(notificationsClassesList, getContext());
                        recyclerView.setAdapter(notificationAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}
