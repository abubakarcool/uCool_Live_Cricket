package com.example.samplestickerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

// this is the first activity it will just open for 0.5 seconds and than it will open SinInActivity
public class SplashActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;

    public boolean isNetworkConnected() {
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
                                Log.e("MY_TAG", "SplashActivity : LOGIN_DEBUG check credentials good for iptv so move forward to google login ");
                                Intent intent =  new Intent(SplashActivity.this,MainActivity.class);
                                startActivity(intent);
                            } else {
                                Log.e("MY_TAG", "SplashActivity : LOGIN_DEBUG check credentials expired or changed ");
                                Toast.makeText(SplashActivity.this, "Invalid login credentials, expired account, or internet firewall restrictions.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SplashActivity.this,SignInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.e("MY_TAG", "SplashActivity : LOGIN_DEBUG Error 2: " + e.toString());
                            Toast.makeText(SplashActivity.this, "Invalid login credentials, expired account, or internet firewall restrictions.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SplashActivity.this,SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MY_TAG", "SplashActivity : LOGIN_DEBUG Error 3 : " + error.toString());
                        Toast.makeText(SplashActivity.this, "Invalid login credentials, expired account, or internet firewall restrictions.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(SplashActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your Account");
        progressDialog.setCanceledOnTouchOutside(false);
        database = FirebaseDatabase.getInstance();

        if(auth.getCurrentUser()!=null){ //////**************//////////////// if user has already had account and logged in before
            progressDialog.show();
            String username = MyHelper.retrieveData(this, "username");
            String password = MyHelper.retrieveData(this, "password");
            if (!isNetworkConnected()) { // Display error when there's no internet
                Toast.makeText(SplashActivity.this, "No Internet", Toast.LENGTH_LONG).show();
                finish();
            } else{
                validateLogin_auto(username,password);
            }
            progressDialog.dismiss();
        } else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); //we don't want user to come back to splash screen when he clicks back button
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable,400);
        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashActivity.this,SignInActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish(); //we don't want user to come back to splash screen when he clicks back button
//            }
//        };
//        Handler handler = new Handler();
//        handler.postDelayed(runnable,500);

    }
}