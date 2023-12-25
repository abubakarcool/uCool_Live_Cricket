package com.example.samplestickerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.samplestickerapp.Models.Users;
import com.example.samplestickerapp.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    public String expDate; // This is the public string to hold the exp_date



    public boolean isInternetConnected() {
        String command = "ping -c 1 google.com";
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitValue = process.waitFor();
            return exitValue == 0; // If exitValue is 0, the ping was successful, indicating internet connectivity.
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false; // An error occurred while trying to ping or waiting for the process.
        }
    }
    private void validateLogin(String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String serverUrl = getString(R.string.SERVER_IP) +":"+ getString(R.string.SERVER_PORT)
                +"/player_api.php?username=" + email + "&password=" + password;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("MY_TAG","SignInActivity : username and password were correct by json data");
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("user_info")) {
                                JSONObject userInfo = jsonResponse.getJSONObject("user_info");
                                expDate = userInfo.getString("exp_date");
                                Toast.makeText(SignInActivity.this, "Login Credentials Valid", Toast.LENGTH_SHORT).show();
                                signIn();
                            } else {
                                Log.d("MY_TAG","SignInActivity : username password expired : "+email+" "+password);
                                Toast.makeText(SignInActivity.this, "Invalid Login Credentials", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("MY_TAG","SignInActivity : No Json was given in response for username and password");
                            Toast.makeText(SignInActivity.this, "Error parsing server response.", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOGIN_DEBUG", "Error: " + error.toString());
                        Log.d("MY_TAG","SignInActivity : Some unknown error has occured");
                        Toast.makeText(SignInActivity.this, "Error occurred while connecting to the server.", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void validateLogin_auto(String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String serverUrl = getString(R.string.SERVER_IP) +":"+ getString(R.string.SERVER_PORT)
                +"/player_api.php?username=" + email + "&password=" + password;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("user_info")) {
                                Log.e("LOGIN_DEBUG", "check credentials good for iptv so move forword to google login ");
                                Intent intent =  new Intent(SignInActivity.this,MainActivity.class);
                                startActivity(intent);
                            } else {
                                Log.e("LOGIN_DEBUG", "check credentials expired or changed ");
                                Toast.makeText(SignInActivity.this, "Invalid Login Credentials or Expired", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("LOGIN_DEBUG", "Error 2: " + e.toString());
                            Toast.makeText(SignInActivity.this, "Invalid Login Credentials or Expired", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOGIN_DEBUG", "Error 3 : " + error.toString());
                        Toast.makeText(SignInActivity.this, "Invalid Login Credentials or Expired", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your Account");
        progressDialog.setCanceledOnTouchOutside(false);
        database = FirebaseDatabase.getInstance();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE); //This is an Android key-value storage system where you can store private
        // primitive data in key-value pairs. However, do note that SharedPreferences stores data in plain text, so it's not ideal for sensitive data unless you
        // encrypt it.

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() { // Sign In Button Click
            @Override
            public void onClick(View v) {
                if(binding.etEmail.getText().toString().isEmpty() ){ // if the email is empty
                    binding.etEmail.setError("Invalid Email!!");
                    return;
                }

                if(binding.etPassword.getText().toString().isEmpty() ){ // if the password is empty
                    binding.etPassword.setError("Enter your Password");
                    return;
                }
                progressDialog.show();
                if (!isInternetConnected()) {
                    // Display error when there's no internet
                    Log.d("MY_TAG","SignInActivity : No internet");
                    Toast.makeText(SignInActivity.this, "No Internet", Toast.LENGTH_LONG).show();
                } else {
                    String email = binding.etEmail.getText().toString();
                    String password = binding.etPassword.getText().toString();
                    Log.d("MY_TAG","SignInActivity : Internet is working so let validateLogin details : "+email+" "+password);
                    validateLogin(email, password);
                }
                progressDialog.dismiss();
            }
        });
        binding.tvclickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        if(auth.getCurrentUser()!=null){ //////**************//////////////// if user has already had account and logged in before
            progressDialog.show();
            String username = MyHelper.retrieveData(this, "username");
            String password = MyHelper.retrieveData(this, "password");
            binding.etEmail.setText(username);
            binding.etPassword.setText(password);
            if (!isInternetConnected()) { // Display error when there's no internet
                Toast.makeText(SignInActivity.this, "No Internet", Toast.LENGTH_LONG).show();
            } else{
                validateLogin_auto(username,password);
            }
            progressDialog.dismiss();
        }
    }
    int RC_SIGN_IN = 65;
    private void signIn(){
        Log.d("MY_TAG","SignInActivity : Inside signIn()");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("MY_TAG","SignInActivity : firebaseAuthWithGoogle : "+ account.getId());
                firebaseAuthWithGoogle(account.getIdToken(),account.getEmail());
            }catch (ApiException e){
                Log.d("MY_TAG", "SignInActivity :  Google Sign In Failed "+e.getStatusCode());
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken, String eemail){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("MY_TAG", "signInWithCredential:success");
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            Users users = new Users();
                            users.setUserId(firebaseUser.getUid());

                            DatabaseReference ref = database.getReference().child("Users").child(firebaseUser.getUid());
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChildren()) {
                                        //Toast.makeText(SignInActivity.this, "User exist", Toast.LENGTH_SHORT).show();
                                        MyHelper.saveData(SignInActivity.this, "username", binding.etEmail.getText().toString());
                                        MyHelper.saveData(SignInActivity.this, "password", binding.etPassword.getText().toString());
                                    } else {
                                        MyHelper.saveData(SignInActivity.this, "username", binding.etEmail.getText().toString());
                                        MyHelper.saveData(SignInActivity.this, "password", binding.etPassword.getText().toString());
                                        users.setUserName(binding.etEmail.getText().toString());
                                        users.setPassword(binding.etPassword.getText().toString());
                                        users.setProfilePic(firebaseUser.getPhotoUrl().toString());
                                        users.setEemail(eemail);
                                        users.setOnline(expDate);
                                        users.setUserId(firebaseUser.getUid());
                                        database.getReference().child("Users").child(firebaseUser.getUid()).setValue(users);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            Intent intent =  new Intent(SignInActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toast.makeText(SignInActivity.this, "Signing In with Google", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("MY_TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
