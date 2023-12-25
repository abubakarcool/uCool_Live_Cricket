package com.example.samplestickerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.samplestickerapp.Models.Users;
import com.example.samplestickerapp.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String[] StoragPermissions = { android.Manifest.permission.READ_EXTERNAL_STORAGE ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE, };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.backArrowSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(ii);
            }
        });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(SettingsActivity.this, StoragPermissions) ){
                    Log.d("MY_TAG","Accept_Button : didn't had storage permission");
                    ActivityCompat.requestPermissions(SettingsActivity.this, StoragPermissions, 105);
                }
                else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*"); // */*
                    startActivityForResult(intent,33);
                }
            }
        });


        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.avatar).into(binding.profileImageSettings);
                        binding.etUserNameSettings.setText(users.getUserName());
                        binding.etStatusSettings.setText(users.getStatus());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = binding.etStatusSettings.getText().toString();
                String userName = binding.etUserNameSettings.getText().toString();
                HashMap<String, Object> obj = new HashMap<>();
                obj.put("userName", userName);
                obj.put("status", status);
                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);
                Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData() != null){
            Uri sFile = data.getData();
            binding.profileImageSettings.setImageURI(sFile);
            final StorageReference reference = storage.getReference().child("profile_pictures")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profilePic").setValue(uri.toString());
                            Toast.makeText(SettingsActivity.this, "Profile Picture Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 105: {
                {
                    Log.d("MY_TAG","SettingsActivity : onRequestPermissionResult : inside case 555");
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*"); // */*
                        startActivityForResult(intent,33);
                    } else {
                        Toast.makeText(this, "Please Allow Permission to Access Media Files", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}