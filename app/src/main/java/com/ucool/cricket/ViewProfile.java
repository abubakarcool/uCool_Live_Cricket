package com.example.samplestickerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.samplestickerapp.Models.Users;
import com.example.samplestickerapp.databinding.ActivityViewProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewProfile extends AppCompatActivity {
    ActivityViewProfileBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        String userId = getIntent().getStringExtra("userId");
        Users user01 = new Users();

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    user01.setStatus(snapshot.child("status").getValue().toString());
                }catch (Exception e){
                    Log.d("MY_TAG", "ViewProfile : FirebaseDatabase event listener getting status : "+e);
                }
                try {
                    user01.setProfilePic(snapshot.child("profilePic").getValue().toString());
                }catch (Exception e){
                    Log.d("MY_TAG", "ViewProfile : FirebaseDatabase event listener getting profilePic : "+e);
                }
                try {
                    user01.setEemail(snapshot.child("eemail").getValue().toString());
                }catch (Exception e){
                    Log.d("MY_TAG", "ViewProfile : FirebaseDatabase event listener getting email : "+e);
                }try {
                    user01.setUserName(snapshot.child("userName").getValue().toString());
                }catch (Exception e){
                    Log.d("MY_TAG", "ViewProfile : FirebaseDatabase event listener getting username : "+e);
                }

                Picasso.get().load(user01.getProfilePic()).placeholder(R.drawable.avatar).into(binding.profileImageViewProfile);
                binding.tvEmailViewProfile.setText(user01.getEemail());
                binding.tvStatusViewProfile.setText(user01.getStatus());
                binding.tvUserNameViewProfile.setText(user01.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        binding.backArrowViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this , ChatDetailActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("profilePic",user01.getProfilePic());
                intent.putExtra("userName", user01.getUserName());
                startActivity(intent);
            }
        });

        binding.profileImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ViewProfile.this, ViewProfileImage.class);
                intent2.putExtra("userId",userId);
                intent2.putExtra("profilePic",user01.getProfilePic());
                intent2.putExtra("userName",user01.getUserName());
                startActivity(intent2);
            }
        });
    }
}