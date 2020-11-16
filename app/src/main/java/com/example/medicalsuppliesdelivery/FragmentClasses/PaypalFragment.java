package com.example.medicalsuppliesdelivery.FragmentClasses;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.DataClasses.Orders;
import com.example.medicalsuppliesdelivery.DataClasses.Users;
import com.example.medicalsuppliesdelivery.Interfaces.SuppliesInterface;
import com.example.medicalsuppliesdelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaypalFragment extends Fragment {
    private ImageView back;
    private TextView price;
    private EditText card;
    private TextView name;
    private EditText loc;
    private Button btn;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    private SuppliesInterface suppliesInterface;
    private int priceM;
    private static final int REQUEST_CODE = 1;

    private String location;
    private String cardNum;
    private String pr;
    private String nam;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            priceM = bundle.getInt("PRICE");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_paypal, container, false);
        back = (ImageView) v.findViewById(R.id.backPaypal);
        price = (TextView) v.findViewById(R.id.pricePay);
        card = (EditText) v.findViewById(R.id.cardNum);
        name = (TextView) v.findViewById(R.id.nameP);
        loc = (EditText) v.findViewById(R.id.LocP);
        btn = (Button) v.findViewById(R.id.payButton);
        getDataP();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPayment();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppliesInterface.onBackPressed();
            }
        });


        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+254797466810", null, "Hello", null, null);
                    Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Not sent", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            break;
        }

    }

    private void getDataP() {
        price.setText(String.valueOf(priceM));
        location = loc.getText().toString().toUpperCase();
        cardNum = card.getText().toString();
        pr = price.getText().toString();
        nam = name.getText().toString();
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            databaseReference = database.getReference().child("Users").child(firebaseAuth.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Users users = snapshot.getValue(Users.class);
                        name.setText(users.getFullName().toUpperCase());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (firebaseAuth.getCurrentUser() != null){
            databaseReference = database.getReference().child("Users").child(firebaseAuth.getUid()).child("location");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        loc.setText(snapshot.getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void confirmPayment() {
        if (firebaseAuth.getCurrentUser() != null) {
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("PendingOrders").child(firebaseAuth.getUid()).push();
            String orderId = databaseReference.getKey();
            Orders orders = new Orders(orderId, priceM);
            databaseReference.setValue(orders);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)){

                        }
                        else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE);
                        }
                    }
                    else {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("0797466810", null, "Hello " + nam + ", your order " + orderId + " is in transit", null, null);
                        Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
                    }
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View v1 = inflater.inflate(R.layout.success_message, null);
                    Button track = (Button) v1.findViewById(R.id.track);
                    builder1.setView(v1);
                    AlertDialog dialog = builder1.create();
                    if (dialog != null){
                        dialog.getWindow().getAttributes().windowAnimations = R.style.slidingDialog;
                    }
                    dialog.show();
                    track.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            suppliesInterface.trackOrders();
                            dialog.dismiss();
                        }
                    });
                    database = FirebaseDatabase.getInstance();
                    databaseReference = database.getReference("Cart").child(firebaseAuth.getUid());
                    databaseReference.removeValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater1 = getActivity().getLayoutInflater();
                    View v2 = inflater1.inflate(R.layout.failure_message, null);
                    builder2.setView(v2);
                    AlertDialog dialog1 = builder2.create();
                    if (dialog1 != null){
                        dialog1.getWindow().getAttributes().windowAnimations = R.style.slidingDialog;
                    }
                    dialog1.show();

                }
            });
        }
        else {
            Toast.makeText(getContext(), "Missing data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        suppliesInterface = (SuppliesInterface) context;
    }
}
