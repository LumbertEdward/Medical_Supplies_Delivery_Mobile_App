package com.example.medicalsuppliesdelivery.FragmentClasses;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.DataClasses.Users;
import com.example.medicalsuppliesdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Account extends Fragment {
    private TextView username;
    private TextView full;
    private TextView number;
    private TextView language;
    private TextView date;
    private TextView gender;
    private Button btn;
    private ImageView upload;
    private CircleImageView circleImageView;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    String genderEdit = "";
    private Uri filePath;
    private final int IMAGE_STATUS_CODE = 22;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        username = (TextView) v.findViewById(R.id.username);
        full = (TextView) v.findViewById(R.id.fullName);
        number = (TextView) v.findViewById(R.id.number);
        language = (TextView) v.findViewById(R.id.language);
        date = (TextView) v.findViewById(R.id.date);
        gender = (TextView) v.findViewById(R.id.gender);
        btn = (Button) v.findViewById(R.id.btnProfile);
        linearLayout = (LinearLayout) v.findViewById(R.id.linearAccount);
        progressBar = (ProgressBar) v.findViewById(R.id.progressAccount);
        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        circleImageView = (CircleImageView) v.findViewById(R.id.imgAccount);
        upload = (ImageView) v.findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getImage();
        if (auth.getCurrentUser() != null){
            firebaseDatabase = FirebaseDatabase.getInstance();
            reference = firebaseDatabase.getReference().child("Users").child(auth.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        Users users = snapshot.getValue(Users.class);
                        username.setText(users.getUsername());
                        full.setText(users.getFullName());
                        number.setText(users.getNumber());
                        language.setText(users.getLanguage());
                        date.setText(users.getDate());
                        gender.setText(users.getGender());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                }
            });

        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editData();
            }
        });
        return v;
    }

    private void getImage() {
        if (auth.getCurrentUser() != null){
            reference = firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("userImage");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        String imgUrl = snapshot.getValue().toString();
                        Picasso.Builder builder = new Picasso.Builder(getContext());
                        builder.downloader(new OkHttp3Downloader(getContext()));
                        builder.build().load(imgUrl).into(circleImageView);
                    }
                    else {

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void uploadPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Select image here...."),
                IMAGE_STATUS_CODE
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_STATUS_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                ContentResolver contentResolver = getActivity().getContentResolver();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath);
                sendToStorage();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void sendToStorage() {
        if (filePath != null){
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference().child("images/" + UUID.randomUUID().toString());
            storageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String downImage = task.getResult().toString();
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            reference = firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("userImage");
                            reference.setValue(downImage);
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    reference = firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("userImage");
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                String imgUrl = snapshot.getValue().toString();
                                                Picasso.Builder builder = new Picasso.Builder(getContext());
                                                builder.downloader(new OkHttp3Downloader(getContext()));
                                                builder.build().load(imgUrl).into(circleImageView);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * (snapshot.getBytesTransferred()/snapshot.getTotalByteCount()));
                   // Toast.makeText(getContext(), "Upload " + progress + "%", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void editData() {
        if (auth.getCurrentUser() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.profile_edit, null);
            TextInputEditText usernameE = (TextInputEditText) v.findViewById(R.id.usernameEdit);
            TextInputEditText fullE = (TextInputEditText) v.findViewById(R.id.fullNameEdit);
            TextInputEditText dateE = (TextInputEditText) v.findViewById(R.id.dateEdit);
            TextInputEditText langaugeE = (TextInputEditText) v.findViewById(R.id.langaugeEdit);
            TextInputEditText numberE = (TextInputEditText) v.findViewById(R.id.phoneEdit);
            RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.genderEdit);
            ImageView imageView = (ImageView) v.findViewById(R.id.imgEdit);
            Button button = (Button) v.findViewById(R.id.btnEdit);
            builder.setView(v);
            AlertDialog alertDialog = builder.create();
            if (alertDialog != null){
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.slidingDialog;
            }
            alertDialog.show();
            dateE.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    Calendar cl = Calendar.getInstance();
                    int day = cl.get(Calendar.DAY_OF_MONTH);
                    int month = cl.get(Calendar.MONTH);
                    int year = cl.get(Calendar.YEAR);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            dateE.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.male:
                            genderEdit = "Male";
                            break;
                        case R.id.female:
                            genderEdit = "Female";
                            break;
                    }
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    Calendar cl = Calendar.getInstance();
                    int day = cl.get(Calendar.DAY_OF_MONTH);
                    int month = cl.get(Calendar.MONTH);
                    int year = cl.get(Calendar.YEAR);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            dateE.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("username");
                    String user = usernameE.getText().toString();
                    reference.setValue(user);
                    reference = firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("fullName");
                    String fullName = fullE.getText().toString();
                    reference.setValue(fullName);
                    reference = firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("language");
                    String language = langaugeE.getText().toString();
                    reference.setValue(language);
                    reference = firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("number");
                    String number = numberE.getText().toString();
                    reference.setValue(number);
                    reference = firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("date");
                    String date = dateE.getText().toString();
                    reference.setValue(date);
                    reference = firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("gender");
                    reference.setValue(genderEdit);

                    alertDialog.dismiss();
                }
            });
        }
    }
}
