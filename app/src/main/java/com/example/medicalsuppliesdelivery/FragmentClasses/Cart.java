package com.example.medicalsuppliesdelivery.FragmentClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.ActivitiesClasses.Login;
import com.example.medicalsuppliesdelivery.Adapters.CartAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.Orders;
import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.Interfaces.AdminInterface;
import com.example.medicalsuppliesdelivery.Interfaces.SuppliesInterface;
import com.example.medicalsuppliesdelivery.R;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cart extends Fragment {
    private Button btn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CartAdapter cartAdapter;
    private TextView total;
    private TextView taxes;
    private TextView charges;
    private TextView discount;
    private TextView payable;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private ArrayList<Products> productsArrayList;
    private Orders orders;
    private TextView warn;
    private TextView toT;
    private int valueToPass;
    private ScrollView scrollView;

    private SuppliesInterface suppliesInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        btn = (Button) v.findViewById(R.id.btnCart);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerCart);
        layoutManager = new LinearLayoutManager(getContext());
        total = (TextView) v.findViewById(R.id.txtTotal);
        taxes = (TextView) v.findViewById(R.id.txtTaxes);
        charges = (TextView) v.findViewById(R.id.txtCharges);
        payable = (TextView) v.findViewById(R.id.txtPayable);
        warn = (TextView) v.findViewById(R.id.txtWarn);
        warn.setVisibility(View.GONE);
        scrollView = (ScrollView) v.findViewById(R.id.scrollCart);
        scrollView.setVisibility(View.GONE);

        progressBar = (ProgressBar) v.findViewById(R.id.progressCart);
        progressBar.setVisibility(View.VISIBLE);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            showCart();
        }
        else {
            startActivity(new Intent(getContext(), Login.class));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            getActivity().finish();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payOrder();
            }
        });
        return v;
    }

    private void payOrder() {
        if (auth.getCurrentUser() != null && valueToPass > 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.payment_modes, null);
            RelativeLayout mP = (RelativeLayout) v.findViewById(R.id.mpesa);
            RelativeLayout payP = (RelativeLayout) v.findViewById(R.id.paypal);
            RelativeLayout payD = (RelativeLayout) v.findViewById(R.id.payDel);
            RadioButton mpesa = (RadioButton) v.findViewById(R.id.radioMpesa);
            RadioButton paypal = (RadioButton) v.findViewById(R.id.radioPaypal);
            RadioButton del = (RadioButton) v.findViewById(R.id.radioPayDel);
            builder.setView(v);
            AlertDialog alertDialog = builder.create();
            if (alertDialog != null){
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.slidingDialog;
            }
            alertDialog.show();
            mpesa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    int a = Integer.parseInt(total.getText().toString());
                    double totalPrice = ((a * 0.01) - 100);
                    int sub = (int) totalPrice;
                    suppliesInterface.orderMpesa(valueToPass);
                }
            });
            paypal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    int a = Integer.parseInt(total.getText().toString());
                    double totalPrice = ((a * 0.01) - 100);
                    int sub = (int) totalPrice;
                    suppliesInterface.orderPaypal(valueToPass);
                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();

                }
            });
        }
        else {
            Toast.makeText(getContext(), "Buy", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCart() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Cart").child(auth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                warn.setVisibility(View.GONE);
                productsArrayList = new ArrayList<Products>();
                if (snapshot.exists()){
                    for (DataSnapshot snap : snapshot.getChildren()){
                        Products products = new Products();
                        products.setName(snap.child("name").getValue().toString());
                        products.setPrice(((Long)snap.child("price").getValue()).intValue());
                        products.setRating(snap.child("rating").getValue().toString());
                        products.setDescription(snap.child("description").getValue().toString());
                        products.setImgUrl(snap.child("imgUrl").getValue().toString());
                        productsArrayList.add(products);

                        if (!productsArrayList.isEmpty()){
                            int grandTotal = 0;
                            for (int i = 0; i < productsArrayList.size(); i++){
                                grandTotal += productsArrayList.get(i).getPrice();
                            }
                            total.setText(String.valueOf(grandTotal));
                            taxes.setText(String.valueOf(grandTotal * 0.01));
                            charges.setText(String.valueOf(100));
                            double tot = (grandTotal * 0.01);
                            valueToPass = (int) (grandTotal - 100 - tot);
                            payable.setText(String.valueOf(grandTotal - 100 - tot));
                        }


                    }
                    cartAdapter = new CartAdapter(productsArrayList, getActivity());
                    recyclerView.setAdapter(cartAdapter);
                    recyclerView.setLayoutManager(layoutManager);

                }
                else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    warn.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        suppliesInterface = (SuppliesInterface) context;
    }
}
