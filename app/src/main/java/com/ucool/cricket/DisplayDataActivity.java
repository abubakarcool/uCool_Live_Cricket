package com.example.samplestickerapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.example.samplestickerapp.Models.wwosnine;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DisplayDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        // Start the AsyncTask to perform network operation in the background
        new ScrapeDataTask().execute();
    }

    private class ScrapeDataTask extends AsyncTask<Void, Void, List<wwosnine>> {
        @Override
        protected List<wwosnine> doInBackground(Void... voids) {
            List<wwosnine> scrapedData = new ArrayList<>();

            // Replace with the actual URL you want to scrape
            String url = "https://assets-wwos.sportz.io/sifeeds/multisport/?methodtype=3&client=37f6777763&sport=1&league=0&timezone=0530&language=en&daterange=04102023-04102023";

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

                        // Extract participants as an array
                        JsonArray participantsArray = matchObject.get("participants").getAsJsonArray();

                        // Extract data from JSON and create a wwosnine object
                        wwosnine data = new wwosnine(
                                matchObject.get("tour_name").getAsString(),
                                matchObject.get("tour_id").getAsString(),
                                matchObject.get("match_id").getAsString(),
                                matchObject.get("event_sub_status").getAsString(),
                                matchObject.get("event_state").getAsString(),
                                matchObject.get("event_name").getAsString(),
                                matchObject.get("venue_name").getAsString(),
                                matchObject.get("start_date").getAsString(),
                                matchObject.get("event_format").getAsString(),
                                participantsArray.get(0).getAsJsonObject().get("short_name_eng").getAsString(),
                                participantsArray.get(0).getAsJsonObject().get("value").getAsString(),
                                participantsArray.get(1).getAsJsonObject().get("short_name_eng").getAsString(),
                                participantsArray.get(1).getAsJsonObject().get("value").getAsString()
                        );

                        scrapedData.add(data);
                    }
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
        protected void onPostExecute(List<wwosnine> scrapedData) {
            super.onPostExecute(scrapedData);

            // Log the scraped data
            for (wwosnine dataItem : scrapedData) {
                Log.d("MY_TAG", dataItem.toString());
            }
        }
    }

}
