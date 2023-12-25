package com.example.samplestickerapp.Adapters;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Scraper {

    public interface ScraperListener {
        void onDataScraped(ArrayList<String> data);
    }

    // Function to convert time from source timezone to the device's current timezone
    private String convertTimeToCurrentTimeZone(String sourceTime) {
        TimeZone sourceTimeZone = TimeZone.getTimeZone("GMT+05:30"); // Source timezone
        TimeZone destinationTimeZone = TimeZone.getDefault(); // Destination timezone (device's current timezone)

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        dateFormat.setTimeZone(sourceTimeZone);

        try {
            Date sourceDate = dateFormat.parse(sourceTime);

            dateFormat.setTimeZone(destinationTimeZone);
            return dateFormat.format(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return sourceTime; // Return the original time if there's an error
        }
    }

    public void scrapeData(final ScraperListener listener) {
        new AsyncTask<Void, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Void... voids) {
                ArrayList<String> dataList = new ArrayList<>();
                try {
                    String url = "https://www.hindustantimes.com/cricket/world-cup/schedule";
                    Document document = Jsoup.connect(url).get();
                    Log.d("MY_TAG", "Scraper : Connected to the website");

                    // Select the second table with class 'staticTable2'
                    Elements tables = document.select("table.staticTable2");
                    if (tables.size() >= 2) {
                        Element table = tables.get(1); // hahahhahahah Use the second table
                        Elements rows = table.select("tr");

                        for (int i = 1; i < rows.size(); i++) {
                            Element row = rows.get(i);
                            Elements columns = row.select("td");

                            if (columns.size() >= 4) {
                                String match = columns.get(0).select("a").text();
                                String date = columns.get(1).text();
                                String time = columns.get(2).text();
                                String venue = columns.get(3).text();

                                // Check if the <td> contains <em class="liveBlink">Live</em>
                                if (columns.select("em.liveBlink").size() > 0) {
                                    // Replace venue with "live" if "Live" is present
                                    venue = "live";
                                }

                                // Convert the time to the device's current timezone
                                time = convertTimeToCurrentTimeZone(time);

                                String rowData = match + " | " + date + " | " + time + " | " + venue;
                                dataList.add(rowData);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("MY_TAG", "Scraper : Error while scraping data: " + e.getMessage());
                }
                return dataList;
            }

            @Override
            protected void onPostExecute(ArrayList<String> dataList) {
                super.onPostExecute(dataList);
                if (listener != null) {
                    listener.onDataScraped(dataList);
                }
            }
        }.execute();
    }
}
