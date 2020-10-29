package com.example.medicalsuppliesdelivery.ActivitiesClasses;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.DataClasses.Users;
import com.example.medicalsuppliesdelivery.MainActivity;
import com.example.medicalsuppliesdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;

public class Register extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView toolBack;
    private TextInputEditText email;
    private TextInputEditText password;
    private Button register;
    private TextView back;

    private TextView username;
    private TextView name;
    private TextView phone;
    private TextView language;
    private TextView date;
    private RadioGroup gender;
    private Button image;
    private ImageView selectDate;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    private String genderM = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setNavigationBarColor(Color.WHITE);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        init();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() == null){
                    registerUser();
                }
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolRegister);
        toolBack = (ImageView) findViewById(R.id.imgBackRegister);
        email = (TextInputEditText) findViewById(R.id.emailRegister);
        password = (TextInputEditText) findViewById(R.id.passwordRegister);
        register = (Button) findViewById(R.id.btnRegister);
        username = (TextView) findViewById(R.id.usernameReg);
        name = (TextView) findViewById(R.id.nameRegister);
        phone = (TextView) findViewById(R.id.phoneRegister);
        language = (TextView) findViewById(R.id.languageRegister);
        date = (TextView) findViewById(R.id.dateRegister);
        gender = (RadioGroup) findViewById(R.id.genderRegister);
        selectDate = (ImageView) findViewById(R.id.imgRegister);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cl = Calendar.getInstance();
                int day = cl.get(Calendar.DAY_OF_MONTH);
                int month = cl.get(Calendar.MONTH);
                int year = cl.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.maleR:
                        genderM = "Male";
                        break;
                    case R.id.femaleR:
                        genderM = "Female";
                        break;
                }
            }
        });

        back = (TextView) findViewById(R.id.txtBackLogin);

        setSupportActionBar(toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }
    private void registerUser() {
        user = firebaseAuth.getCurrentUser();
        if (user == null){
            String lEmail = email.getText().toString().trim();
            String lPassword = password.getText().toString().trim();

            if (TextUtils.isEmpty(lEmail)){
                email.setError("Enter Email");
            }
            else if (TextUtils.isEmpty(lPassword)){
                password.setError("Enter Password");
            }
            else {
                firebaseAuth.createUserWithEmailAndPassword(lEmail, lPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    sendData();
                                    startActivity(new Intent(Register.this, ProfileImage.class));
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                                else {
                                    Toast.makeText(Register.this, "Registration Unsuccessful, Check network Connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

    }

    private void sendData() {
        String user = username.getText().toString().trim();
        String nm = name.getText().toString().trim();
        String pn = phone.getText().toString().trim();
        String lang = language.getText().toString().trim();
        String dat = date.getText().toString().trim();
        String lEmail = email.getText().toString().trim();

        if (TextUtils.isEmpty(user)){
            username.setError("Username");
        }
        else if (TextUtils.isEmpty(nm)){
            name.setError("Full Name");
        }
        else if (TextUtils.isEmpty(pn)){
            phone.setError("Phone Number");
        }
        else if (TextUtils.isEmpty(lang)){
            language.setError("Language");
        }
        else if (TextUtils.isEmpty(dat)){
            date.setError("Date of Birth");
        }
        else if (TextUtils.isEmpty(lEmail)){
            email.setError("Email");
        }
        else {
            databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
            Users users = new Users(user, nm, pn, genderM, dat, lang);
            databaseReference.setValue(users);
            databaseReference.child("email").setValue(lEmail);
            //databaseReference.child("userImage").setValue("");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    @Override
    protected void onStart() {
        if (firebaseAuth.getCurrentUser() != null){
            firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            });
        }
        else {
            super.onStart();
        }
    }
}
