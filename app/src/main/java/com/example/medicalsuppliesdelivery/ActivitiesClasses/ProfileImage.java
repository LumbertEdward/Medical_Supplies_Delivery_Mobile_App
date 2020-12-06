package com.example.medicalsuppliesdelivery.ActivitiesClasses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.MainActivity;
import com.example.medicalsuppliesdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileImage extends AppCompatActivity {
    private CircleImageView circleImageView;
    private Button next;
    private Toolbar toolbar;
    private ImageView prof;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private final int IMAGE_REQUEST_CODE = 22;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.bl));
        }

        circleImageView = (CircleImageView) findViewById(R.id.imgProf);
        next = (Button) findViewById(R.id.profileAct);
        toolbar = (Toolbar) findViewById(R.id.toolProfile);
        prof = (ImageView) findViewById(R.id.profUpload);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        setSupportActionBar(toolbar);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null){
                    reference = database.getReference().child("Users").child(auth.getUid()).child("userImage");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                startActivity(new Intent(ProfileImage.this, MainActivity.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }
                            else {
                                Toast.makeText(ProfileImage.this, "Choose Profile Photo", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
         prof.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                     getImage();
             }
         });

    }

    private void getImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                setImage();

            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void setImage() {
        if (filePath != null){
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference().child("Images/" + UUID.randomUUID().toString());
            storageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String downImage = task.getResult().toString();
                            database = FirebaseDatabase.getInstance();
                            reference = database.getReference().child("Users").child(auth.getUid());
                            reference.child("userImage").setValue(downImage);
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    reference = database.getReference().child("Users").child(auth.getUid());
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String imgUrl = snapshot.child("userImage").getValue().toString();
                                            Picasso.Builder builder = new Picasso.Builder(ProfileImage.this);
                                            builder.downloader(new OkHttp3Downloader(ProfileImage.this));
                                            builder.build().load(imgUrl).into(circleImageView);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            //Toast.makeText(getContext(), "Upload Successful" + downImage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }


}
