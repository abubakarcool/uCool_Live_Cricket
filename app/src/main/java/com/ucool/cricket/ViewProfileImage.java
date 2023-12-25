package com.example.samplestickerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.samplestickerapp.databinding.ActivityViewProfileImageBinding;
import com.squareup.picasso.Picasso;

public class ViewProfileImage extends AppCompatActivity {
    ActivityViewProfileImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProfileImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        String userId = getIntent().getStringExtra("userId");
        String profilePic = getIntent().getStringExtra("profilePic");
        String userName = getIntent().getStringExtra("userName");

        binding.backArrowViewProImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ViewProfileImage.this, ViewProfile.class);
                intent2.putExtra("userId",userId);
                startActivity(intent2);
            }
        });

        binding.tvViewProImg.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileImgViewProImg);
    }
}