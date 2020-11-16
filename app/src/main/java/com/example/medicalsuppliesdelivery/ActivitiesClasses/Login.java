package com.example.medicalsuppliesdelivery.ActivitiesClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.MainActivity;
import com.example.medicalsuppliesdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private TextInputEditText email;
    private TextInputEditText password;
    private Button login;
    private CheckBox checkBox;
    private TextView account;
    private TextView forget;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.bl));
        }
        firebaseAuth = FirebaseAuth.getInstance();
        init();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginMethod();
            }
        });
    }

    private void init() {
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btnLogin);
        checkBox = (CheckBox) findViewById(R.id.checkboxLogin);
        account = (TextView) findViewById(R.id.account);
        forget = (TextView) findViewById(R.id.forgot);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
    private void loginMethod() {
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
            else{
                firebaseAuth.signInWithEmailAndPassword(lEmail, lPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                }
                                else {
                                    Toast.makeText(Login.this, "Confirm Email and Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(Login.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

    }
}
