package com.example.medicalsuppliesdelivery.ActivitiesClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageView;
    private TextInputEditText email;
    private TextView back;
    private Button btn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.bl));
        }
        firebaseAuth = FirebaseAuth.getInstance();
        init();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverPasswrd();
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolForgot);
        imageView = (ImageView) findViewById(R.id.imgBackForgot);
        email = (TextInputEditText) findViewById(R.id.emailForgot);
        back = (TextView) findViewById(R.id.txtBackForgot);
        btn = (Button) findViewById(R.id.btnForgot);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this, Login.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this, Login.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
    private void recoverPasswrd() {
        user = firebaseAuth.getCurrentUser();
        if (user == null){
            String fEmail = email.getText().toString().trim();

            if (TextUtils.isEmpty(fEmail)){
                email.setError("Enter Email");
            }
            else {
                firebaseAuth.sendPasswordResetEmail(fEmail)
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ForgotPassword.this, "Check Email to reset Password", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(ForgotPassword.this, "Check Email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
}
