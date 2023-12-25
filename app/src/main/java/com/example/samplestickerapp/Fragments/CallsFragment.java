package com.example.samplestickerapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.samplestickerapp.Adapters.PendingRequestsAdapter;
import com.example.samplestickerapp.Models.Team;
import com.example.samplestickerapp.StandingsScraper;
import com.example.samplestickerapp.databinding.FragmentCallsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CallsFragment extends Fragment {

    public CallsFragment() {
        // Required empty public constructor
    }

    FragmentCallsBinding binding;
    ArrayList<Team> list1 = new ArrayList<>();
    FirebaseDatabase database;
    private PendingRequestsAdapter adapter1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCallsBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        final String senderId = FirebaseAuth.getInstance().getUid();

        // Initialize adapter1 here
        adapter1 = new PendingRequestsAdapter(list1, getContext());
        binding.requestsRecyclerView.setAdapter(adapter1);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        binding.requestsRecyclerView.setLayoutManager(layoutManager1);
        new ScrapeDataTask().execute();

        return binding.getRoot();

    }

    private class ScrapeDataTask extends AsyncTask<Void, Void, List<String[]>> {

        @Override
        protected List<String[]> doInBackground(Void... voids) {
            try {
                List<String[]> scrapedData = StandingsScraper.scrapeStandings();
                if (scrapedData != null) {
                    Log.d("MY_TAG", "CallsFragment :Scraped Data " + scrapedData.toString());
                } else {
                    Log.d("MY_TAG", "CallsFragment : Scraped Data is null.");
                }
                return scrapedData;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String[]> standingsData) {
            if (standingsData != null) {
                List<Team> teamList = new ArrayList<>();

                for (String[] rowData : standingsData) {
                    Team team = new Team(rowData[0], rowData[1], rowData[2], rowData[3], rowData[4]);
                    teamList.add(team);
                }
                Log.d("MY_TAG", "CallsFragment : Team List " + teamList.toString());
                adapter1.updateData(teamList); // Update your adapter1 with the list of Team objects
            } else {
                Log.d("MY_TAG", "CallsFragment : Scraping Standings failed.");
            }
        }
    }
}