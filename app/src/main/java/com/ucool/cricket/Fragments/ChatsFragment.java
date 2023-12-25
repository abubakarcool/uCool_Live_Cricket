package com.example.samplestickerapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.samplestickerapp.Adapters.UsersAdapter;
import com.example.samplestickerapp.Models.Friend;
import com.example.samplestickerapp.Models.Link;
import com.example.samplestickerapp.Models.Users;
import com.example.samplestickerapp.R;
import com.example.samplestickerapp.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ChatsFragment extends Fragment {

    FragmentChatsBinding binding;
    ArrayList<Link> list = new ArrayList<>();
    UsersAdapter adapter;
    String JSON_URL;
    private final Handler handler = new Handler();
    private static final long UPDATE_INTERVAL = 60 * 1000; // check after every 1 minutes that if it has changed or not so update that
    private final Runnable periodicTask = new Runnable() {
        @Override
        public void run() {
            fetchAndUpdateJSONData();
            handler.postDelayed(this, UPDATE_INTERVAL);
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        adapter = new UsersAdapter(list, context);
        JSON_URL = getString(R.string.logos);
        loadJSONData();
        handler.postDelayed(periodicTask, UPDATE_INTERVAL); // Start the periodic task
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacks(periodicTask);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);
        binding.chatRecyclerView.setAdapter(adapter);
        return binding.getRoot();
    }
    private void loadJSONData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        list.clear();
                        Iterator<String> keys = response.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            try {
                                JSONObject friendObject = response.getJSONObject(key);
                                Link friend = new Link(
                                        friendObject.getString("name"),
                                        friendObject.getString("logo"),
                                        friendObject.getString("num")
                                );
                                list.add(friend);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                Log.d("MY_TAG", "VolleyError: " + error.toString());
            }
        });

        queue.add(request);
    }

    private void fetchAndUpdateJSONData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Link> newList = new ArrayList<>();

                        Iterator<String> keys = response.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            try {
                                JSONObject friendObject = response.getJSONObject(key);
                                Link friend = new Link(
                                        friendObject.getString("name"),
                                        friendObject.getString("logo"),
                                        friendObject.getString("num")
                                );
                                newList.add(friend);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Compare the new data with the existing data
                        if (!list.equals(newList)) {
                            list.clear();
                            list.addAll(newList);
                            adapter.notifyDataSetChanged();
                            Log.d("MY_TAG", "New Data came on json so updating ");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                Log.d("MY_TAG", "VolleyError: " + error.toString());
            }
        });

        queue.add(request);
    }

}