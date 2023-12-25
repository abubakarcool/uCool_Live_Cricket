package com.example.samplestickerapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.samplestickerapp.Adapters.MatchListAdapter;
import com.example.samplestickerapp.Models.wwosnine;
import com.example.samplestickerapp.databinding.FragmentFixturesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FixturesFragment extends Fragment {

    public FixturesFragment() {
        // Required empty public constructor
    }

    FragmentFixturesBinding binding;
    ArrayList<wwosnine> list = new ArrayList<>();
    FirebaseDatabase database;
    private MatchListAdapter matchListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFixturesBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        final String senderId = FirebaseAuth.getInstance().getUid();

        // Initialize adapter1 here

        matchListAdapter = new MatchListAdapter(list, getContext());
        binding.fixturesRecyclerView.setAdapter(matchListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.fixturesRecyclerView.setLayoutManager(layoutManager);

        // Load data from JSON and update the adapter
        new ScrapeDataTask_ww().execute();

        return binding.getRoot();

    }



    private class ScrapeDataTask_ww extends AsyncTask<Void, Void, List<wwosnine>> {
        @Override
        protected List<wwosnine> doInBackground(Void... voids) {
            List<wwosnine> scrapedData = new ArrayList<>();

            String jjjj = getYesterdayDate(); // get yesterday date
            String kkkk = addOneWeek(jjjj);

            // Replace with the actual URL you want to scrape
            String url = "https://assets-wwos.sportz.io/sifeeds/multisport/?methodtype=3&client=37f6777763&sport=1&league=0&timezone="+
                    getFourDigitTimeZone()+"&language=en&daterange="+jjjj+"2023-"+kkkk+"2023";

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    JsonArray matchesArray = JsonParser.parseString(jsonData).getAsJsonObject().getAsJsonArray("matches");

                    for (int i = 0; i < matchesArray.size(); i++) {
                        JsonObject matchObject = matchesArray.get(i).getAsJsonObject();
                        if (matchObject.has("tour_id") && matchObject.get("tour_id").getAsString().equals("4223")) {
                            // Extract participants as an array
                            JsonArray participantsArray = matchObject.has("participants") ? matchObject.get("participants").getAsJsonArray() : null;

                            // Check for null values before extracting data
                            String tourName = matchObject.has("tour_name") ? matchObject.get("tour_name").getAsString() : "";
                            String tourId = matchObject.has("tour_id") ? matchObject.get("tour_id").getAsString() : "";
                            String matchId = matchObject.has("match_id") ? matchObject.get("match_id").getAsString() : "";
                            String eventSubStatus = matchObject.has("event_sub_status") ? matchObject.get("event_sub_status").getAsString() : "";
                            String eventState = matchObject.has("event_state") ? matchObject.get("event_state").getAsString() : "";
                            String eventName = matchObject.has("event_name") ? matchObject.get("event_name").getAsString() : "";
                            String venueName = matchObject.has("venue_name") ? matchObject.get("venue_name").getAsString() : "";
                            String startDate = matchObject.has("start_date") ? matchObject.get("start_date").getAsString() : "";
                            String eventFormat = matchObject.has("event_format") ? matchObject.get("event_format").getAsString() : "";
                            String team1ShortName = participantsArray != null && participantsArray.size() > 0 && participantsArray.get(0).getAsJsonObject().has("short_name_eng") ? participantsArray.get(0).getAsJsonObject().get("short_name_eng").getAsString() : "";
                            String team1Value = participantsArray != null && participantsArray.size() > 0 && participantsArray.get(0).getAsJsonObject().has("value") ? participantsArray.get(0).getAsJsonObject().get("value").getAsString() : "";
                            String team2ShortName = participantsArray != null && participantsArray.size() > 1 && participantsArray.get(1).getAsJsonObject().has("short_name_eng") ? participantsArray.get(1).getAsJsonObject().get("short_name_eng").getAsString() : "";
                            String team2Value = participantsArray != null && participantsArray.size() > 1 && participantsArray.get(1).getAsJsonObject().has("value") ? participantsArray.get(1).getAsJsonObject().get("value").getAsString() : "";

                            wwosnine data = new wwosnine(
                                    tourName,
                                    tourId,
                                    matchId,
                                    eventSubStatus,
                                    eventState,
                                    eventName,
                                    venueName,
                                    startDate,
                                    eventFormat,
                                    team1ShortName,
                                    team1Value,
                                    team2ShortName,
                                    team2Value
                            );

                            scrapedData.add(data);
                        }}
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Sort the list based on matchState1: 'L' -> 'R' -> 'U'
            Collections.sort(scrapedData, new Comparator<wwosnine>() {
                @Override
                public int compare(wwosnine o1, wwosnine o2) {
                    return o1.getMatchState1().compareTo(o2.getMatchState1());
                }
            });

            return scrapedData;
        }

        @Override
        protected void onPostExecute(List<wwosnine> wwosnineList) {
            if (wwosnineList != null) {
                // Update your MatchListAdapter with the fetched data
                matchListAdapter.updateData(wwosnineList);
            } else {
                Log.d("MY_TAG", "CallsFragment: JSON data fetching failed.");
            }
        }
    }

    // Add a method to fetch and parse JSON data into a List<wwosnine>
    private List<wwosnine> fetchDataFromJson() throws IOException {
        // Fetch and parse JSON data here and return a List<wwosnine>
        // Replace this with your JSON data fetching logic
        List<wwosnine> wwosnineList = new ArrayList<>();
        // Sample data for testing
        wwosnineList.add(new wwosnine(/* Initialize your wwosnine object here */));
        return wwosnineList;
    }
    public static String getFourDigitTimeZone() {
        // Get the default timezone
        TimeZone timeZone = TimeZone.getDefault();

        // Get the raw offset from GMT in milliseconds
        int rawOffsetInMilliseconds = timeZone.getRawOffset();

        // Convert the raw offset to hours and minutes
        int hours = rawOffsetInMilliseconds / (60 * 60 * 1000);
        int minutes = (Math.abs(rawOffsetInMilliseconds) / (60 * 1000)) % 60;

        // Create a string representation of the numerical timezone offset
        String numericalTimeZoneOffset = String.format("%+03d%02d", hours, minutes);

        return numericalTimeZoneOffset;
    }


    public String addOneWeek(String inputDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMM", Locale.getDefault());
            Date date = dateFormat.parse(inputDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 7);

            return dateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMM", Locale.getDefault());
        return dateFormat.format(new Date());
    }
    public String getYesterdayDate() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // Subtract 1 day to get yesterday's date
        calendar.add(Calendar.DAY_OF_YEAR, -1);

        // Format the date as "ddMM"
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMM", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}