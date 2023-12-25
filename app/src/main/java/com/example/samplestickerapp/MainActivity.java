package com.example.samplestickerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.samplestickerapp.Adapters.FragmentsAdapter;
import com.example.samplestickerapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
//Main Activity open whatever is written in FragmentAdapter in its ViewPager
//ViewPager is a component that allows users to flip left and right through pages of data.
// onOptionsItemsSelected is also here which shows settings and logout
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setElevation(0);

        auth = FirebaseAuth.getInstance();

        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent2);
                break;
            case R.id.suggestions:
                Intent intent3 = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent3);
                break;
            case R.id.logout:
                auth.signOut();
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}