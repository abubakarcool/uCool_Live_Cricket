package com.example.samplestickerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.samplestickerapp.Models.Users;
import com.example.samplestickerapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    ProgressDialog progressDialog;
    String phoneNumber = "923235239749"; // Your phone number without '+' and with country code
    String message = "Hello! I need help."; // Your pre-filled message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("opening whatsapp");
        progressDialog.setMessage("opening whatsapp");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() { // open whatsapp
            @Override
            public void onClick(View v) {
                progressDialog.show();
                openWhatsApp(phoneNumber, message);
                progressDialog.dismiss();
            }
        });
        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() { // already have credentials
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });


        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {// whatsapp button clicked
            @Override
            public void onClick(View v) {
                progressDialog.show();
                openWhatsApp(phoneNumber, message);
                progressDialog.dismiss();
            }
        });
    }

    private void openWhatsApp(String phoneNumber, String message) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://wa.me/" + phoneNumber + "?text=" + URLEncoder.encode(message, "UTF-8")));
            startActivity(intent);
        } catch (ActivityNotFoundException | UnsupportedEncodingException e) {
            // If regular WhatsApp is not installed, check for WhatsApp Business
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.whatsapp.w4b");  // Package name of WhatsApp Business
                intent.setData(Uri.parse("https://wa.me/" + phoneNumber + "?text=" + URLEncoder.encode(message, "UTF-8")));
                startActivity(intent);
            } catch (ActivityNotFoundException | UnsupportedEncodingException ex) {
                Toast.makeText(this, "Neither WhatsApp nor WhatsApp Business is installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}